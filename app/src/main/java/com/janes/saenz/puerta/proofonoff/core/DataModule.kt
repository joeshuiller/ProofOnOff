package com.janes.saenz.puerta.proofonoff.core

import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostCommentsDataSource
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostCommentsDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostDataSource
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostFavoriteDataSource
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostFavoriteDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.network.source.RemoteDataSource
import com.janes.saenz.puerta.proofonoff.data.network.source.RemoteDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.repository.AndroidNetworkRepositoryImpl
import com.janes.saenz.puerta.proofonoff.data.repository.PostCommentRepositoryImpl
import com.janes.saenz.puerta.proofonoff.data.repository.PostRepositoryImpl
import com.janes.saenz.puerta.proofonoff.data.repository.RemoteRepositoryImpl
import com.janes.saenz.puerta.proofonoff.domain.repository.NetworkRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * DataModule - Módulo de resolución de interfaces para la capa de datos.
 *
 * Este módulo vincula las interfaces de repositorio y fuentes de datos con sus
 * implementaciones reales, permitiendo el uso de Inyección de Dependencias
 * bajo los principios SOLID.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    // --- REPOSITORIOS (Domain Interfaces) ---

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        impl: AndroidNetworkRepositoryImpl
    ): NetworkRepository

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        impl: RemoteRepositoryImpl
    ): RemoteRepository

    @Binds
    @Singleton
    abstract fun bindPostCommentRepository(
        impl: PostCommentRepositoryImpl
    ): PostCommentRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository

    // --- DATA SOURCES (Infrastructure) ---

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        impl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPostDataSource(
        impl: PostDataSourceImpl
    ): PostDataSource

    @Binds
    @Singleton
    abstract fun bindPostCommentsDataSource(
        impl: PostCommentsDataSourceImpl
    ): PostCommentsDataSource

    @Binds
    @Singleton
    abstract fun bindPostFavoriteDataSource(
        impl: PostFavoriteDataSourceImpl
    ): PostFavoriteDataSource
}
