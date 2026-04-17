package com.janes.saenz.puerta.proofonoff.ui.viewModels

import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    // Dispatcher que nos permite controlar el avance manual del tiempo
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `debe establecer startDestination despues de 2 segundos`() = runTest {
        // GIVEN: Al instanciar, el init se ejecuta en el testDispatcher
        val viewModel = SplashViewModel()

        // THEN: Inmediatamente después de la creación, debe ser null
        assertNull("Al inicio el destino debe ser null", viewModel.startDestination)

        // WHEN: Avanzamos el tiempo 1.5 segundos (aún no debería cambiar)
        advanceTimeBy(1500)
        runCurrent() // Procesa las tareas programadas hasta este punto

        // THEN: Sigue siendo null
        assertNull("A los 1.5s el destino aún debe ser null", viewModel.startDestination)

        // WHEN: Avanzamos el tiempo para completar los 2 segundos
        advanceTimeBy(501)
        runCurrent()

        // THEN: El destino debe haberse actualizado
        assertEquals(
            "Después de 2s el destino debe ser main_tabs",
            "main_tabs",
            viewModel.startDestination
        )
    }
}