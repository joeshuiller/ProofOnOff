package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
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
 * GetPostByIdUseCaseTest - Verifica la recuperación reactiva de un post individual.
 */
class GetPostByIdUseCaseTest {

    private lateinit var repository: PostRepository
    private lateinit var useCase: GetPostByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPostByIdUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should return flow from repository and emit Success with correct post`() = runTest {
        // GIVEN
        val postId = 101
        val mockPost = mockk<Posts>()

        // Simulamos la secuencia típica: Loading -> Success
        val expectedFlow = flowOf(
            Resource.Loading,
            Resource.Success(mockPost)
        )

        every { repository.getPostById(postId) } returns expectedFlow

        // WHEN: Convertimos el flujo en una lista para validar cada emisión
        val results = useCase(postId).toList()

        // THEN
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val successResult = results[1] as Resource.Success
        assertEquals(mockPost, successResult.data)

        // Verificamos que se llamó al repositorio con el ID exacto
        verify(exactly = 1) { repository.getPostById(postId) }
    }

    @Test
    fun `invoke should propagate Error state when repository fails`() = runTest {
        // GIVEN
        val postId = 50
        val errorMsg = "Post no encontrado"
        val errorFlow = flowOf(Resource.Error(errorMsg, 404))

        every { repository.getPostById(postId) } returns errorFlow

        // WHEN
        val results = useCase(postId).toList()

        // THEN
        assertTrue(results[0] is Resource.Error)
        val errorResult = results[0] as Resource.Error
        assertEquals(errorMsg, errorResult.message)
        assertEquals(404, errorResult.code)
    }
}