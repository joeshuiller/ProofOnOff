package com.janes.saenz.puerta.proofonoff.domain.repository

import com.janes.saenz.puerta.proofonoff.domain.models.NetworkStatus
import kotlinx.coroutines.flow.Flow

/**
 * NetworkRepository - Contrato para el monitoreo de conectividad.
 * * Centraliza la lógica de red para permitir que la aplicación responda
 * dinámicamente a cambios en el entorno de comunicaciones.
 */
interface NetworkRepository {

    /**
     * Flujo reactivo que emite cambios en el estado de red.
     * * [NetworkStatus] suele ser una Sealed Class con estados como
     * Available, Lost, o Unavailable. Ideal para componentes de UI globales.
     */
    val networkStatus: Flow<NetworkStatus>

    /**
     * Verifica de forma instantánea la disponibilidad de internet.
     * * @return [Boolean] 'true' si el dispositivo tiene una ruta activa a internet,
     * 'false' en caso contrario.
     */
    fun hasInternetConnection(): Boolean
}
