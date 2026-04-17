package com.janes.saenz.puerta.proofonoff.ui.utlis

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * ScreenTest - Valida la integridad del sistema de rutas de navegación.
 */
class ScreenTest {

    @Test
    fun `las rutas estaticas deben devolver el string correcto`() {
        // THEN
        assertEquals("splash", Screen.Splash.route)
        assertEquals("auth", Screen.Auth.route)
        assertEquals("main_tabs", Screen.MainTabs.route)
        assertEquals("tables", Screen.Tables.route)
    }

    @Test
    fun `Details route debe tener el formato de argumento de Navigation Component`() {
        // THEN
        assertEquals("details/{postId}", Screen.Details.route)
    }

    @Test
    fun `createRoute debe generar la ruta dinamica con el ID proporcionado`() {
        // GIVEN
        val postId = 101

        // WHEN
        val result = Screen.Details.createRoute(postId)

        // THEN
        assertEquals("details/101", result)
    }

    @Test
    fun `todas las instancias de Screen deben ser consistentes`() {
        // Verificamos que al usar la clase base se mantenga la ruta
        val screen: Screen = Screen.Splash
        assertEquals("splash", screen.route)
    }
}