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
 * ObserveAllPostsUseCaseTest - Valida la observación reactiva del feed completo.
 */
class ObserveAllPostsUseCaseTest {

    private lateinit var repository: PostRepository
    private lateinit var useCase: ObserveAllPostsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ObserveAllPostsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke debe retornar el flujo del repositorio y emitir estados de Resource correctamente`() = runTest {
        // GIVEN
        val mockPosts = listOf(mockk<Posts>(), mockk<Posts>())

        // Simulamos la secuencia reactiva: Carga -> Éxito con datos
        val expectedFlow = flowOf(
            Resource.Loading,
            Resource.Success(mockPosts)
        )

        every { repository.observeAllPosts() } returns expectedFlow

        // WHEN: Recolectamos el flujo en una lista para validar la secuencia
        val results = useCase().toList()

        // THEN
        assertEquals(2, results.size)

        // Verificamos estado inicial
        assertTrue(results[0] is Resource.Loading)

        // Verificamos estado final y datos
        assertTrue(results[1] is Resource.Success)
        val successData = (results[1] as Resource.Success).data
        assertEquals(mockPosts, successData)
        assertEquals(2, successData.size)

        // Verificamos interacción con el contrato del repositorio
        verify(exactly = 1) { repository.observeAllPosts() }
    }

    @Test
    fun `invoke debe propagar errores del repositorio sin modificaciones`() = runTest {
        // GIVEN
        val errorMessage = "Database Connection Failed"
        val errorFlow = flowOf(Resource.Error(errorMessage, 500))

        every { repository.observeAllPosts() } returns errorFlow

        // WHEN
        val results = useCase().toList()

        // THEN
        assertTrue(results[0] is Resource.Error)
        val errorResult = results[0] as Resource.Error
        assertEquals(errorMessage, errorResult.message)
        assertEquals(500, errorResult.code)
    }
}
