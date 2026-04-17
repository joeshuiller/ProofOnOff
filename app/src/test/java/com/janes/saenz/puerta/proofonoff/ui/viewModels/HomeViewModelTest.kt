package com.janes.saenz.puerta.proofonoff.ui.viewModels

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.useCase.GetPostsUseCase
import com.janes.saenz.puerta.proofonoff.domain.useCase.ObserveFilteredPostsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var getPostsUseCase: GetPostsUseCase
    private lateinit var observeFiltered: ObserveFilteredPostsUseCase
    private lateinit var viewModel: HomeViewModel

    // Usamos UnconfinedTestDispatcher para simplificar la recolección de estados inmediatos
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getPostsUseCase = mockk()
        observeFiltered = mockk()

        // Mock inicial necesario para el Lazy State de stateIn
        every { observeFiltered(any(), any()) } returns flowOf(Resource.Loading)

        viewModel = HomeViewModel(getPostsUseCase, observeFiltered)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `loadProducts debe actualizar uiState y marcar isSuccess como true`() = runTest {
        // GIVEN
        // 1. Mockeamos el Log de Android para evitar el crash en la JVM
        mockkStatic(android.util.Log::class)
        every { android.util.Log.e(any(), any()) } returns 0

        val mockPosts = listOf(mockk<Posts>())
        // Definimos el flujo que emitirá el UseCase
        every { getPostsUseCase() } returns flowOf(Resource.Success(mockPosts))

        // WHEN
        viewModel.loadProducts()

        // Forzamos el avance del despachador para procesar el collect
        advanceUntilIdle()

        // THEN
        val result = viewModel.uiState.value
        assertTrue("El estado debería ser Success", result is Resource.Success)
        assertEquals(mockPosts, (result as Resource.Success).data)
        assertTrue("isSuccess debería ser true tras la carga", viewModel.isSuccess)

        verify { getPostsUseCase() }
    }

    @Test
    fun `postsState debe buscar por Titulo cuando el query no es numerico`() = runTest {
        // GIVEN
        val queryTitle = "Kotlin"
        val mockPosts = listOf(mockk<Posts>())
        every { observeFiltered(id = null, title = queryTitle) } returns flowOf(Resource.Success(mockPosts))

        // --- TRUCO VITAL: Suscribirse al Flow para activarlo ---
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.postsState.collect {}
        }

        // WHEN
        viewModel.onSearchQueryChanged(queryTitle)

        // Avanzamos el reloj para el debounce (300ms)
        advanceTimeBy(301)

        // THEN
        val result = viewModel.postsState.value
        assertTrue("Esperaba Success pero fue $result", result is Resource.Success)
        verify { observeFiltered(id = null, title = queryTitle) }

        collectJob.cancel() // Limpieza
    }

    @Test
    fun `postsState debe aplicar debounce y no buscar inmediatamente`() = runTest {
        // GIVEN
        val mockPosts = listOf(mockk<Posts>())
        every { observeFiltered(any(), any()) } returns flowOf(Resource.Success(mockPosts))

        // Activamos el Flow
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.postsState.collect {} }

        // WHEN
        viewModel.onSearchQueryChanged("A")
        viewModel.onSearchQueryChanged("AB")
        viewModel.onSearchQueryChanged("ABC")

        // Inmediatamente después de los cambios, el debounce NO debe haber expirado
        verify(exactly = 0) { observeFiltered(any(), any()) }

        // Avanzamos el tiempo
        advanceTimeBy(305)

        // THEN
        verify(exactly = 1) { observeFiltered(id = null, title = "ABC") }

        collectJob.cancel()
    }
}
