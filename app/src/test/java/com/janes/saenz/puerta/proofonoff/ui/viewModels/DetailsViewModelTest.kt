package com.janes.saenz.puerta.proofonoff.ui.viewModels

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.useCase.GetPostByIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private lateinit var getPostByIdUseCase: GetPostByIdUseCase
    private lateinit var viewModel: DetailsViewModel

    // Dispatcher para controlar el tiempo de ejecución de las corrutinas
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Redirigimos el Dispatcher Main al de pruebas
        Dispatchers.setMain(testDispatcher)
        getPostByIdUseCase = mockk()
        viewModel = DetailsViewModel(getPostByIdUseCase)
    }

    @After
    fun tearDown() {
        // Limpiamos el entorno de pruebas
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getPostDetail debe actualizar uiState con Success cuando el UseCase responde correctamente`() = runTest {
        // GIVEN
        val postId = 1
        val mockPost = mockk<Posts>()
        val successResource = Resource.Success(mockPost)

        // Simulamos que el UseCase devuelve un flujo con el post
        every { getPostByIdUseCase(postId) } returns flowOf(successResource)

        // WHEN
        viewModel.getPostDetail(postId)

        // Forzamos la ejecución de la corrutina en el viewModelScope
        advanceUntilIdle()

        // THEN
        val currentState = viewModel.uiState.value
        assertTrue("El estado debería ser Success", currentState is Resource.Success)
        assertEquals(mockPost, (currentState as Resource.Success).data)

        verify(exactly = 1) { getPostByIdUseCase(postId) }
    }

    @Test
    fun `getPostDetail debe actualizar uiState con Error cuando el UseCase falla`() = runTest {
        // GIVEN
        val postId = 1
        val errorMessage = "No se encontró el post"
        val errorResource = Resource.Error(errorMessage)

        every { getPostByIdUseCase(postId) } returns flowOf(errorResource)

        // WHEN
        viewModel.getPostDetail(postId)
        advanceUntilIdle()

        // THEN
        val currentState = viewModel.uiState.value
        assertTrue("El estado debería ser Error", currentState is Resource.Error)
        assertEquals(errorMessage, (currentState as Resource.Error).message)
    }

    @Test
    fun `getPostDetail debe mostrar Loading al iniciar la peticion`() = runTest {
        // GIVEN
        val postId = 1
        // Simulamos un flujo que no emite nada inmediatamente para capturar el onStart
        every { getPostByIdUseCase(postId) } returns flowOf()

        // WHEN
        viewModel.getPostDetail(postId)
        // No usamos advanceUntilIdle() para ver el estado inicial del flujo iniciado

        // THEN
        val currentState = viewModel.uiState.value
        assertTrue("El estado inicial debería ser Loading debido al onStart", currentState is Resource.Loading)
    }
}