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
 * ObserveFilteredPostsUseCaseTest - Valida el motor de búsqueda y filtrado dinámico.
 */
class ObserveFilteredPostsUseCaseTest {

    private lateinit var repository: PostRepository
    private lateinit var useCase: ObserveFilteredPostsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ObserveFilteredPostsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke debe pasar parametros de filtrado al repositorio y retornar el flujo`() = runTest {
        // GIVEN
        val id = 123
        val title = "Kotlin"
        val mockPosts = listOf(mockk<Posts>())
        val expectedFlow = flowOf(Resource.Success(mockPosts))

        every { repository.observeFilteredPosts(id, title) } returns expectedFlow

        // WHEN
        val results = useCase(id, title).toList()

        // THEN
        assertTrue(results[0] is Resource.Success)
        assertEquals(mockPosts, (results[0] as Resource.Success).data)

        // Verificación de parámetros exactos
        verify(exactly = 1) { repository.observeFilteredPosts(id, title) }
    }

    @Test
    fun `invoke debe funcionar correctamente con parametros nulos`() = runTest {
        // GIVEN
        val expectedFlow = flowOf(Resource.Loading)

        every { repository.observeFilteredPosts(null, null) } returns expectedFlow

        // WHEN
        val results = useCase().toList() // Usando valores por defecto (null)

        // THEN
        assertTrue(results[0] is Resource.Loading)
        verify(exactly = 1) { repository.observeFilteredPosts(null, null) }
    }

    @Test
    fun `invoke debe propagar errores de busqueda`() = runTest {
        // GIVEN
        val errorMsg = "Búsqueda fallida"
        val errorFlow = flowOf(Resource.Error(errorMsg))

        every { repository.observeFilteredPosts(any(), any()) } returns errorFlow

        // WHEN
        val results = useCase(id = 1).toList()

        // THEN
        assertTrue(results[0] is Resource.Error)
        assertEquals(errorMsg, (results[0] as Resource.Error).message)
    }
}
