package com.janes.saenz.puerta.proofonoff.ui.viewModels

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.useCase.AddCommentUseCase
import com.janes.saenz.puerta.proofonoff.domain.useCase.GetCommentsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentsViewModelTest {

    private lateinit var getCommentsUseCase: GetCommentsUseCase
    private lateinit var addCommentUseCase: AddCommentUseCase
    private lateinit var viewModel: CommentsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCommentsUseCase = mockk()
        addCommentUseCase = mockk()
        viewModel = CommentsViewModel(getCommentsUseCase, addCommentUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getComments debe actualizar uiStateComments con exito`() = runTest {
        // GIVEN
        val postId = 1
        val mockComments = listOf(mockk<Comments>())
        every { getCommentsUseCase(postId) } returns flowOf(Resource.Success(mockComments))

        // WHEN
        viewModel.getComments(postId)
        advanceUntilIdle() // Ejecuta las corrutinas pendientes

        // THEN
        val state = viewModel.uiStateComments.value
        assertTrue(state is Resource.Success)
        assertEquals(mockComments, (state as Resource.Success).data)
        verify { getCommentsUseCase(postId) }
    }

    @Test
    fun `getComments debe manejar estados de error`() = runTest {
        // GIVEN
        val postId = 1
        val errorMsg = "Error al cargar"
        every { getCommentsUseCase(postId) } returns flowOf(Resource.Error(errorMsg))

        // WHEN
        viewModel.getComments(postId)
        advanceUntilIdle()

        // THEN
        val state = viewModel.uiStateComments.value
        assertTrue(state is Resource.Error)
        assertEquals(errorMsg, (state as Resource.Error).message)
    }

    @Test
    fun `sendComment debe gestionar el ciclo de vida de isSuccess y refrescar comentarios`() = runTest {
        // GIVEN
        val mockComment = mockk<Comments>(relaxed = true)
        val postId = 123
        every { mockComment.postId } returns postId

        // Importante: coEvery para la función suspend
        coEvery { addCommentUseCase(mockComment) } returns Unit
        every { getCommentsUseCase(postId) } returns flowOf(Resource.Loading)

        // WHEN
        viewModel.sendComment(mockComment)

        // --- SOLUCIÓN AL ASSERTION ERROR ---
        // Forzamos la ejecución del inicio de la corrutina (antes del delay)
        runCurrent()

        // THEN
        // Ahora sí, el estado debe ser true
        assertTrue("isSuccess debería ser true al iniciar el envío", viewModel.isSuccess)

        // Avanzamos el tiempo para saltar el delay(2000L)
        advanceTimeBy(2000L)
        runCurrent() // Ejecuta lo que sigue después del delay

        // THEN FINAL
        assertFalse("isSuccess debería ser false después del delay y ejecución", viewModel.isSuccess)
        coVerify(exactly = 1) { addCommentUseCase(mockComment) }
        verify { getCommentsUseCase(postId) }
    }
}
