package com.janes.saenz.puerta.proofonoff

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/**
 * Clase base de la aplicación que actúa como el contenedor principal de dependencias.
 *
 * Al estar anotada con [@HiltAndroidApp], esta clase gatilla la generación de código de Hilt,
 * estableciendo el componente base del grafo de dependencias ([SingletonComponent]).
 * @author Janes Saenz Puerta
 * @see HiltAndroidApp
 */
@HiltAndroidApp
class App : Application()
