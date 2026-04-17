package com.janes.saenz.puerta.proofonoff.data.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * ResourceTest - Validación de los estados de transporte de datos.
 * Verifica que los contenedores Success, Error y Loading preserven
 * la integridad de la información.
 */
class ResourceTest {

    @Test
    fun `Success should contain correct data and be of type Resource`() {
        // GIVEN
        val data = "Test Data"

        // WHEN
        val resource = Resource.Success(data)

        // THEN
        assertEquals(data, resource.data)
    }

    @Test
    fun `Error should contain message and optional code`() {
        // GIVEN
        val errorMessage = "Unauthorized"
        val errorCode = 401

        // WHEN
        val resource = Resource.Error(errorMessage, errorCode)
        val resourceNoCode = Resource.Error(errorMessage)

        // THEN
        assertEquals(errorMessage, resource.message)
        assertEquals(errorCode, resource.code)
        assertNull(resourceNoCode.code)
    }

    @Test
    fun `Loading should be a singleton object`() {
        // WHEN
        val resource1 = Resource.Loading
        val resource2 = Resource.Loading

        // THEN
        assertEquals(resource1, resource2) // Verificación de Singleton
    }

    @Test
    fun `Resource subtypes should work with when expression`() {
        // GIVEN
        val resource: Resource<Int> = Resource.Success(100)

        // WHEN
        val result = when (resource) {
            is Resource.Success -> "Success"
            is Resource.Error -> "Error"
            Resource.Loading -> "Loading"
        }

        // THEN
        assertEquals("Success", result)
    }
}
