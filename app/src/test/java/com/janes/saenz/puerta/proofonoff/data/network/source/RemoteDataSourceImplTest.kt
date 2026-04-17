package com.janes.saenz.puerta.proofonoff.data.network.source

import com.janes.saenz.puerta.proofonoff.data.network.api.ApiUrl
import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteDataSourceImplTest {

    // Mock de la interfaz de Retrofit
    private lateinit var apiService: ApiUrl

    // Clase bajo prueba
    private lateinit var remoteDataSource: RemoteDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk()
        remoteDataSource = RemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getPosts should call apiService getPosts and return response`() = runTest {
        // GIVEN
        val mockPosts = listOf(
            PostsResponse(userId = 1, id = 1, title = "Title 1", body = "Body 1"),
            PostsResponse(userId = 1, id = 2, title = "Title 2", body = "Body 2")
        )
        val expectedResponse = Response.success(mockPosts)

        // Configuramos el mock para una función suspendida
        coEvery { apiService.getPosts() } returns expectedResponse

        // WHEN
        val result = remoteDataSource.getPosts()

        // THEN
        assert(result.isSuccessful)
        assertEquals(expectedResponse, result)
        assertEquals(2, result.body()?.size)

        // Verificamos que se llamó exactamente una vez
        coVerify(exactly = 1) { apiService.getPosts() }
    }

    @Test
    fun `getPosts should return error response when apiService fails`() = runTest {
        // GIVEN
        val expectedResponse = Response.error<List<PostsResponse>>(
            404,
            mockk(relaxed = true)
        )
        coEvery { apiService.getPosts() } returns expectedResponse

        // WHEN
        val result = remoteDataSource.getPosts()

        // THEN
        assert(!result.isSuccessful)
        assertEquals(404, result.code())
        coVerify(exactly = 1) { apiService.getPosts() }
    }
}
