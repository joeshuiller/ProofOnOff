package com.janes.saenz.puerta.proofonoff.core

import com.janes.saenz.puerta.proofonoff.data.network.api.ApiUrl
import com.janes.saenz.puerta.proofonoff.ui.utlis.Constants
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class NetworkModuleTest {

    @Before
    fun setUp() {
        // Mocking estático para Retrofit si fuera necesario,
        // pero aquí probaremos la lógica de provisión directa.
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `provideOkHttpClient should return client with 30s timeouts`() {
        val client = NetworkModule.provideOkHttpClient()

        assertNotNull(client)
        assertEquals(30000, client.connectTimeoutMillis)
        assertEquals(30000, client.readTimeoutMillis)
    }

    @Test
    fun `provideVersionRetrofit should return Retrofit instance with correct baseUrl`() {
        val mockOkHttpClient = mockk<OkHttpClient>()

        val retrofit = NetworkModule.provideVersionRetrofit(mockOkHttpClient)

        assertNotNull(retrofit)
        assertEquals(Constants.BASE_URL, retrofit.baseUrl().toString())
        assertEquals(mockOkHttpClient, retrofit.callFactory())
    }

    @Test
    fun `provideApiAuth should return ApiUrl implementation`() {
        // GIVEN
        val mockRetrofit = mockk<Retrofit>()
        val mockApiUrl = mockk<ApiUrl>()

        every { mockRetrofit.create(ApiUrl::class.java) } returns mockApiUrl

        // WHEN
        val result = NetworkModule.provideApiAuth(mockRetrofit)

        // THEN
        assertNotNull(result)
        assertEquals(mockApiUrl, result)
        verify(exactly = 1) { mockRetrofit.create(ApiUrl::class.java) }
    }

    @Test
    fun `baseUrl constant should match BuildConfig URL`() {
        // Este test asegura que no se haya hardcodeado accidentalmente un string
        assertEquals(com.janes.saenz.puerta.proofonoff.BuildConfig.URL_API, Constants.BASE_URL)
    }
}
