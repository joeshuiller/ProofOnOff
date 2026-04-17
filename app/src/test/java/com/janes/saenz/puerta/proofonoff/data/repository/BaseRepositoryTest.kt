package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/**
 * Clase concreta para testear la lógica de BaseRepository.
 */
class TestRepository : BaseRepository()

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryTest {

    private lateinit var repository: TestRepository

    @Before
    fun setUp() {
        repository = TestRepository()
    }

    // --- Tests de safeApiCall (One-Shot) ---

    @Test
    fun `safeApiCall returns Success when response is successful`() = runTest {
        val mockData = "TestData"
        val response = Response.success(mockData)

        val result = repository.safeApiCall { response }

        assertTrue(result is Resource.Success)
        assertEquals(mockData, (result as Resource.Success).data)
    }

    @Test
    fun `safeApiCall returns Error when response is 404`() = runTest {
        val response = Response.error<String>(404, mockk(relaxed = true))

        val result = repository.safeApiCall { response }

        assertTrue(result is Resource.Error)
        assertEquals("Recurso no encontrado", (result as Resource.Error).message)
    }

    @Test
    fun `safeApiCall returns Error when IOException occurs`() = runTest {
        val result = repository.safeApiCall<String> { throw IOException() }

        assertTrue(result is Resource.Error)
        assertEquals("No hay conexión a internet.", (result as Resource.Error).message)
    }

    @Test
    fun `safeApiCall returns Error when unknown Exception occurs`() = runTest {
        val result = repository.safeApiCall<String> { throw Exception("No se pudo conectar al api") }

        assertTrue(result is Resource.Error)
        assertEquals("No se pudo conectar al api", (result as Resource.Error).message)
    }

    // --- Tests de asResource (Flow de DB) ---

    @Test
    fun `asResource emits Loading then Success`() = runTest {
        val flow = flowOf("DBData")
        val results = repository.run { flow.asResource().toList() }

        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertEquals("DBData", (results[1] as Resource.Success).data)
    }

    // --- Tests de safeApiCallFlow (Flow de Red) ---

    @Test
    fun `safeApiCallFlow emits Loading then Success`() = runTest {
        val mockData = "NetworkData"
        val response = Response.success(mockData)

        val results = repository.safeApiCallFlow { response }.toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertEquals(mockData, (results[1] as Resource.Success).data)
    }

    @Test
    fun `safeApiCallFlow emits Loading then Error on 500`() = runTest {
        val response = Response.error<String>(500, mockk(relaxed = true))

        val results = repository.safeApiCallFlow { response }.toList()

        assertTrue(results[1] is Resource.Error)
        assertEquals("Error en el servidor", (results[1] as Resource.Error).message)
    }

    // --- Test de parseError (Ramas del When) ---

    @Test
    fun `parseError handles all status codes`() = runTest {
        // Usamos safeApiCall para disparar indirectamente parseError
        val codes = mapOf(
            401 to "Sesión expirada",
            500 to "Error en el servidor",
            999 to "Error inesperado (Código: 999)"
        )

        codes.forEach { (code, expectedMsg) ->
            val response = Response.error<String>(code, mockk(relaxed = true))
            val result = repository.safeApiCall { response }
            assertEquals(expectedMsg, (result as Resource.Error).message)
        }
    }
}