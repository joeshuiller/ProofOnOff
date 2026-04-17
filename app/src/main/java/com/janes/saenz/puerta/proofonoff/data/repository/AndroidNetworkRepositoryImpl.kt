package com.janes.saenz.puerta.proofonoff.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.janes.saenz.puerta.proofonoff.domain.models.NetworkStatus
import com.janes.saenz.puerta.proofonoff.domain.repository.NetworkRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AndroidNetworkRepositoryImpl - Implementación nativa del monitoreo de conectividad.
 *
 * Provee una interfaz reactiva y síncrona para conocer la disponibilidad de internet
 * mediante las APIs modernas de [ConnectivityManager].
 *
 * @property context Contexto de la aplicación necesario para acceder a los servicios del sistema.
 */
@Singleton
class AndroidNetworkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Flujo reactivo de [NetworkStatus].
     * * Utiliza [callbackFlow] para adaptar la API de listeners de Android a Coroutines.
     * * Registra un callback que filtra redes con capacidad de internet real.
     */
    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Sincronización inmediata: Emitir el estado actual al conectar
        trySend(if (isCurrentlyConnected()) NetworkStatus.Available else NetworkStatus.Unavailable)

        // Limpieza: Se ejecuta cuando el suscriptor (ViewModel) cancela el Scope
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged().flowOn(Dispatchers.IO)

    /**
     * Chequeo puntual de conexión.
     * * Útil para validaciones antes de disparar una petición API única.
     */
    override fun hasInternetConnection(): Boolean = isCurrentlyConnected()

    /**
     * Lógica interna para validar si la red activa tiene salida real a internet.
     */
    private fun isCurrentlyConnected(): Boolean =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
