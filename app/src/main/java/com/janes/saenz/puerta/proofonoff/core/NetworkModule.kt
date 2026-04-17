package com.janes.saenz.puerta.proofonoff.core

import com.janes.saenz.puerta.proofonoff.BuildConfig
import com.janes.saenz.puerta.proofonoff.data.network.api.ApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
/**
 * NetworkModule - Módulo de provisión de dependencias de red.
 *
 * Configura el stack tecnológico para el consumo de servicios REST, garantizando
 * la correcta serialización de datos y la gestión de tiempos de respuesta.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * URL base del servidor, definida en las variables de entorno de [BuildConfig].
     */
    const val baseUrl = BuildConfig.URL_API

    /**
     * Configura y provee el motor de transporte [OkHttpClient].
     * * Define políticas globales de espera para mitigar problemas de latencia.
     * * @return Instancia única de cliente HTTP con 30s de timeout.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Configura y provee el motor de red [Retrofit].
     * * Integra el cliente [OkHttpClient] con la factoría de conversión [GsonConverterFactory].
     * * @param okHttpClient Cliente HTTP inyectado automáticamente por Hilt.
     * @return Cliente Retrofit configurado para parsear JSON.
     */
    @Provides
    @Singleton
    fun provideVersionRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * Genera la implementación dinámica de la interfaz [ApiUrl].
     * * Este servicio es el que consumen los DataSources de la capa de datos.
     * * @param retrofit Instancia de Retrofit pre-configurada.
     * @return Implementación de los endpoints definidos en el contrato de API.
     */
    @Provides
    @Singleton
    fun provideApiAuth(retrofit: Retrofit): ApiUrl {
        return retrofit.create(ApiUrl::class.java)
    }
}