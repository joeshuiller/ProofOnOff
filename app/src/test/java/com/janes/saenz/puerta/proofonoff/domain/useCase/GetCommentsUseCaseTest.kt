package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * GetCommentsUseCaseTest - Valida la comunicación reactiva entre el dominio y el repositorio.
 */
class GetCommentsUseCaseTest {

    private lateinit var repository: PostCommentRepository
    private lateinit var useCase: GetCommentsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCommentsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should return flow from repository and emit correct resources`() = runTest {
        // GIVEN
        val postId = 10
        val mockComments = listOf(mockk<Comments>(), mockk<Comments>())

        // Simulamos un flujo que emite Loading y luego Success
        val expectedFlow = flowOf(
            Resource.Loading,
            Resource.Success(mockComments)
        )

        every { repository.getPostWithComments(postId) } returns expectedFlow

        // WHEN: Recolectamos el flujo en una lista
        val results = useCase(postId).toList()

        // THEN
        // Verificamos el tamaño de las emisiones
        assertEquals(2, results.size)

        // Verificamos el primer estado (Loading)
        assertTrue(results[0] is Resource.Loading)

        // Verificamos el segundo estado (Success) y sus datos
        assertTrue(results[1] is Resource.Success)
        assertEquals(mockComments, (results[1] as Resource.Success).data)

        // Verificamos la interacción con el repositorio
        verify(exactly = 1) { repository.getPostWithComments(postId) }
    }

    @Test
    fun `invoke should propagate error state from repository`() = runTest {
        // GIVEN
        val postId = 1
        val errorMessage = "Error de conexión"
        val errorFlow = flowOf(Resource.Error(errorMessage))

        every { repository.getPostWithComments(postId) } returns errorFlow

        // WHEN
        val results = useCase(postId).toList()

        // THEN
        assertTrue(results[0] is Resource.Error)
        assertEquals(errorMessage, (results[0] as Resource.Error).message)
    }
}