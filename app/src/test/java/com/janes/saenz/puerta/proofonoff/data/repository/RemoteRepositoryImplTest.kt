package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import com.janes.saenz.puerta.proofonoff.data.network.source.RemoteDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.PostResponseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteRepositoryImplTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var mapper: PostResponseMapper
    private lateinit var repository: RemoteRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        mapper = mockk()
        repository = RemoteRepositoryImpl(remoteDataSource, mapper)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getPosts should emit Loading and then Success with mapped data`() = runTest {
        // GIVEN
        val mockResponseList = listOf(mockk<PostsResponse>())
        val mockDomainList = listOf(mockk<Posts>())
        val apiResponse = Response.success(mockResponseList)

        coEvery { remoteDataSource.getPosts() } returns apiResponse
        every { mapper.fromResponseListToDomain(mockResponseList) } returns mockDomainList

        // WHEN
        val results = repository.getPosts().toList()

        // THEN
        // results[0] -> Loading (emitido por safeApiCallFlow)
        // results[1] -> Success (tras el mapeo)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val successResult = results[1] as Resource.Success
        assertEquals(mockDomainList, successResult.data)

        coVerify(exactly = 1) { remoteDataSource.getPosts() }
        verify(exactly = 1) { mapper.fromResponseListToDomain(mockResponseList) }
    }

    @Test
    fun `getPosts should propagate Error state from safeApiCallFlow`() = runTest {
        // GIVEN
        val errorCode = 404
        val apiResponse = Response.error<List<PostsResponse>>(errorCode, mockk(relaxed = true))

        coEvery { remoteDataSource.getPosts() } returns apiResponse

        // WHEN
        val results = repository.getPosts().toList()

        // THEN
        assertTrue(results[1] is Resource.Error)
        val errorResult = results[1] as Resource.Error
        assertEquals(errorCode, errorResult.code)
        // El mensaje viene de parseError en BaseRepository para el código 404
        assertEquals("Recurso no encontrado", errorResult.message)
    }

    @Test
    fun `getPosts should handle empty body response as Error`() = runTest {
        // GIVEN
        val apiResponse = Response.success<List<PostsResponse>>(null)

        coEvery { remoteDataSource.getPosts() } returns apiResponse

        // WHEN
        val results = repository.getPosts().toList()

        // THEN
        assertTrue(results[1] is Resource.Error)
    }
}