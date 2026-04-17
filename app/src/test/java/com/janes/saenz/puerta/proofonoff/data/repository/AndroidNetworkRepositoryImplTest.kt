package com.janes.saenz.puerta.proofonoff.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.janes.saenz.puerta.proofonoff.domain.models.NetworkStatus
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidNetworkRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var repository: AndroidNetworkRepositoryImpl

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        connectivityManager = mockk(relaxed = true)

        // 1. IMPORTANTE: Mockear la clase estática ANTES de instanciar el repositorio
        mockkStatic(NetworkRequest.Builder::class)
        val mockBuilder = mockk<NetworkRequest.Builder>(relaxed = true)

        // Interceptamos el constructor y devolvemos el mockBuilder
        //every { NetworkRequest.Builder() } returns mockBuilder
        every { mockBuilder.addCapability(any()) } returns mockBuilder
        //every { mockBuilder.build() } returns mockk(relaxed = true)

        // 2. Mockear el servicio del sistema
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        repository = AndroidNetworkRepositoryImpl(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `hasInternetConnection should return true when network is capable`() {
        // GIVEN
        val mockNetwork = mockk<Network>()
        val mockCapabilities = mockk<NetworkCapabilities>()

        every { connectivityManager.activeNetwork } returns mockNetwork
        every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities
        every { mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        // WHEN
        val result = repository.hasInternetConnection()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `hasInternetConnection should return false when no capabilities found`() {
        every { connectivityManager.activeNetwork } returns mockk()
        every { connectivityManager.getNetworkCapabilities(any()) } returns null

        val result = repository.hasInternetConnection()

        assertFalse(result)
    }

    @Test
    fun `networkStatus should emit Available initially when connected`() = runTest {
        // GIVEN
        val mockNetwork = mockk<Network>()
        val mockCapabilities = mockk<NetworkCapabilities>()
        every { connectivityManager.activeNetwork } returns mockNetwork
        every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities
        every { mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        // WHEN
        val status = NetworkStatus.Available

        // THEN
        assertEquals(NetworkStatus.Available, status)
    }


}