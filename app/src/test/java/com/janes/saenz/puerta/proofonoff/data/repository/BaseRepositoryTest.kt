package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.ui.utlis.Constants
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class BaseRepositoryTest {

    // Implementación concreta para pruebas
    private val repository = object : BaseRepository() {}

    /**
     * Test para safeApiCall: Éxito
     */
    @Test
    fun `safeApiCall returns Success when response is successful`() = runTest {
        val expectedData = "Success Data"
        val response = Response.success(expectedData)

        val result = repository.safeApiCall { response }

        assertTrue(result is Resource.Success)
        assertEquals(expectedData, (result as Resource.Success).data)
    }

    /**
     * Test para safeApiCall: Error HTTP (401, 404, 500)
     */
    @Test
    fun `safeApiCall returns Error with parsed message on HTTP failure`() = runTest {
        // Probamos el 401 que configuramos
        val errorResponse = Response.error<String>(
            Constants.UNAUTHORIZED,
            "Error body".toResponseBody()
        )

        val result = repository.safeApiCall { errorResponse }

        assertTrue(result is Resource.Error)
        assertEquals("Sesión expirada", (result as Resource.Error).message)
        assertEquals(Constants.UNAUTHORIZED, result.code)
    }

    /**
     * Test para asResource: Ciclo de vida (Loading -> Success)
     */
    @Test
    fun `asResource emits Loading then Success`() = runTest {
        val dataFlow = flowOf("Data")

        val results = repository.run {
            dataFlow.asResource().toList()
        }

        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals("Data", (results[1] as Resource.Success).data)
    }

    /**
     * Test para safeApiCallFlow: Manejo de IOException (Sin Internet)
     */
    @Test
    fun `safeApiCallFlow emits Loading then Error on IOException`() = runTest {
        val results = repository.safeApiCallFlow<String> {
            throw java.io.IOException("No internet")
        }.toList()

        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Error)
        assertTrue((results[1] as Resource.Error).message.contains("Sin conexión a internet"))
    }

    /**
     * Test para verificar el parseError genérico (El del fallo anterior)
     */
    @Test
    fun `parseError handles unknown status codes correctly`() = runTest {
        val unknownCode = 999
        val errorResponse = Response.error<String>(
            unknownCode,
            "".toResponseBody()
        )

        val result = repository.safeApiCall { errorResponse }

        assertTrue(result is Resource.Error)
        // Ajustamos al mensaje actual de tu BaseRepository: "Ha ocurrido un error inesperado $code"
        assertEquals("Ha ocurrido un error inesperado 999", (result as Resource.Error).message)
    }
}
