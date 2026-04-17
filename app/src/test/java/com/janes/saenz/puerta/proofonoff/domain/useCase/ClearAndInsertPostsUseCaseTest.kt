package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * ClearAndInsertPostsUseCaseTest - Valida la lógica de sincronización masiva.
 * Se enfoca en la protección contra inserciones vacías y la delegación al repositorio.
 */
class ClearAndInsertPostsUseCaseTest {

    private lateinit var repository: PostRepository
    private lateinit var useCase: ClearAndInsertPostsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ClearAndInsertPostsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should call repository when list is NOT empty`() = runTest {
        // GIVEN
        val mockPosts = listOf(mockk<Posts>(), mockk<Posts>())
        coEvery { repository.clearAndInsertPosts(any()) } returns Unit

        // WHEN
        useCase(mockPosts)

        // THEN
        coVerify(exactly = 1) { repository.clearAndInsertPosts(mockPosts) }
    }

    @Test
    fun `invoke should NOT call repository when list IS empty`() = runTest {
        // GIVEN
        val emptyPosts = emptyList<Posts>()

        // WHEN
        useCase(emptyPosts)

        // THEN
        // Verificamos que el repositorio nunca fue tocado
        coVerify(exactly = 0) { repository.clearAndInsertPosts(any()) }
    }

    @Test(expected = Exception::class)
    fun `invoke should propagate error when repository fails`() = runTest {
        // GIVEN
        val mockPosts = listOf(mockk<Posts>())
        coEvery { repository.clearAndInsertPosts(any()) } throws Exception("Database failure")

        // WHEN
        useCase(mockPosts)

        // THEN: Expects Exception
    }
}
