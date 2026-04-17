package com.janes.saenz.puerta.proofonoff.core

import android.content.Context
import androidx.room.Room
import com.janes.saenz.puerta.proofonoff.data.dataBase.db.DbData
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostCommentsDao
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostDao
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostFavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

/**
 * DatabaseModule - Proveedor de dependencias para la persistencia local.
 *
 * Este módulo configura el stack de Room, definiendo el nombre físico de la base de datos
 * y distribuyendo los DAOs a través del grafo de dependencias de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Construye y provee la instancia única de la base de datos [DbData].
     * * Utiliza el patrón Builder de Room para inicializar el esquema definido.
     * * @param context Contexto global de la aplicación.
     * @return Instancia única de la base de datos física.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DbData {
        return Room.databaseBuilder(
            context,
            DbData::class.java,
            "proof_on_off_db" // Nombre del archivo SQLite en el almacenamiento interno
        ).build()
    }

    /**
     * Provee el DAO especializado en la tabla de publicaciones.
     * @param db Instancia de la base de datos inyectada automáticamente.
     */
    @Provides
    fun providePostDao(db: DbData): PostDao = db.postDao()

    /**
     * Provee el DAO especializado en hilos de comentarios.
     * @param db Instancia de la base de datos inyectada automáticamente.
     */
    @Provides
    fun providePostCommentsDao(db: DbData): PostCommentsDao = db.postCommentsDao()

    /**
     * Provee el DAO especializado en la gestión de favoritos.
     * @param db Instancia de la base de datos inyectada automáticamente.
     */
    @Provides
    fun provideFavoriteDao(db: DbData): PostFavoriteDao = db.favoriteDao()
}
