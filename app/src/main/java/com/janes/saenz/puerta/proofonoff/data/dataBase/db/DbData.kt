package com.janes.saenz.puerta.proofonoff.data.dataBase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostFavoriteDao
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostCommentsDao
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostDao
/**
 * DbData - Definición central y configurador del motor de persistencia Room.
 * * Esta clase orquesta el esquema de la base de datos local y provee los DAOs
 * necesarios para la manipulación de datos.
 *
 * @property entities Lista de clases marcadas con @Entity que definen las tablas de la DB.
 * @property version Versión actual del esquema (incremental para migraciones).
 * @property exportSchema Determina si se genera un archivo JSON con el esquema (útil para auditoría).
 */
@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DbData : RoomDatabase() {

    /** * Acceso a operaciones CRUD y observación de publicaciones.
     * @return [PostDao]
     */
    abstract fun postDao(): PostDao

    /** * Acceso a la gestión de comentarios e hilos de conversación.
     * @return [PostCommentsDao]
     */
    abstract fun postCommentsDao(): PostCommentsDao

    /** * Acceso a la persistencia de favoritos y estados de interés del usuario.
     * @return [PostFavoriteDao]
     */
    abstract fun favoriteDao(): PostFavoriteDao
}