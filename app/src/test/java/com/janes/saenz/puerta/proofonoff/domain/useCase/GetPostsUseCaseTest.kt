package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.NetworkRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.RemoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPostsUseCaseTest {

    private lateinit var repository: PostRepository
    private lateinit var remoteRepository: RemoteRepository
    private lateinit var networkRepository: NetworkRepository
    private lateinit var useCase: GetPostsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        remoteRepository = mockk()
        networkRepository = mockk()
        useCase = GetPostsUseCase(repository, remoteRepository, networkRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `cuando hay internet y cache vacia, debe descargar, guardar y emitir desde DB`() = runTest {
        // GIVEN
        val mockPosts = listOf(mockk<Posts>())

        // 1. Mock de red disponible
        every { networkRepository.hasInternetConnection() } returns true

        // 2. Mock de caché vacía al inicio
        every { repository.observeAllPosts() } returnsMany listOf(
            flowOf(Resource.Success(emptyList<Posts>())), // Primera llamada (check isCacheEmpty)
            flowOf(Resource.Success(mockPosts)) // Segunda llamada (emitAll)
        )

        // 3. Mock de descarga exitosa
        coEvery { remoteRepository.getPosts() } returns flowOf(Resource.Success(mockPosts))

        // 4. Mock de guardado en DB
        coEvery { repository.clearAndInsertPosts(any()) } returns Unit

        // WHEN
        val results = useCase().toList()

        // THEN
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals(mockPosts, (results[1] as Resource.Success).data)

        coVerify(exactly = 1) { remoteRepository.getPosts() }
        coVerify(exactly = 1) { repository.clearAndInsertPosts(mockPosts) }
    }

    @Test
    fun `cuando no hay internet y cache vacia, debe emitir error`() = runTest {
        // GIVEN
        every { networkRepository.hasInternetConnection() } returns false
        every { repository.observeAllPosts() } returns flowOf(Resource.Success(emptyList()))

        // WHEN
        val results = useCase().toList()

        // THEN
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Error)
        assertEquals("No hay internet y la base de datos local está vacía", (results[1] as Resource.Error).message)
    }

    @Test
    fun `cuando hay datos en cache, no debe llamar a red incluso si hay internet`() = runTest {
        // GIVEN
        val mockPosts = listOf(mockk<Posts>())
        every { networkRepository.hasInternetConnection() } returns true
        every { repository.observeAllPosts() } returns flowOf(Resource.Success(mockPosts))

        // WHEN
        val results = useCase().toList()

        // THEN
        assertTrue(results[1] is Resource.Success)
        coVerify(exactly = 0) { remoteRepository.getPosts() } // IMPORTANTE: No toca la red
    }
}
