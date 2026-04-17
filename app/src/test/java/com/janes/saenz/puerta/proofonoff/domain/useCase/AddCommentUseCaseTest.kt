package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * AddCommentUseCaseTest - Verifica que la capa de dominio coordine
 * correctamente la inserción de comentarios.
 */
class AddCommentUseCaseTest {

    // Dependencia mockeada
    private lateinit var repository: PostCommentRepository

    // Clase bajo prueba
    private lateinit var addCommentUseCase: AddCommentUseCase

    @Before
    fun setUp() {
        repository = mockk()
        addCommentUseCase = AddCommentUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should call insertComment in repository with correct parameters`() = runTest {
        // GIVEN
        val mockComment = mockk<Comments>()

        // Configuramos el mock para una función suspendida (coEvery)
        coEvery { repository.insertComment(any()) } returns Unit

        // WHEN
        addCommentUseCase(mockComment)

        // THEN
        // Verificamos que se llamó al repositorio exactamente una vez
        coVerify(exactly = 1) { repository.insertComment(mockComment) }
    }

    @Test(expected = Exception::class)
    fun `invoke should propagate exception if repository fails`() = runTest {
        // GIVEN
        val mockComment = mockk<Comments>()
        coEvery { repository.insertComment(any()) } throws Exception("Database error")

        // WHEN
        addCommentUseCase(mockComment)

        // THEN - Se espera la excepción definida en el atributo 'expected' de la anotación @Test
    }
}