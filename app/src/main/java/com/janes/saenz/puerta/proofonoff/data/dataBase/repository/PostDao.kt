package com.janes.saenz.puerta.proofonoff.data.dataBase.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * PostDao - Interfaz de acceso a datos para la tabla 'posts_table'.
 *
 * Contiene la lógica de persistencia para las publicaciones, permitiendo
 * operaciones reactivas y sincronización transaccional.
 */
@Dao
interface PostDao {

    /**
     * Recupera una publicación específica por su identificador.
     * @return [Flow] que emite la entidad cada vez que se actualiza en DB.
     */
    @Query("SELECT * FROM posts_table WHERE id = :id")
    fun getPostById(id: Int): Flow<PostEntity>

    /**
     * Observa todas las publicaciones ordenadas de forma ascendente.
     * @return [Flow] con la lista completa de publicaciones.
     */
    @Query("SELECT * FROM posts_table ORDER BY id ASC")
    fun observeAllPosts(): Flow<List<PostEntity?>>

    /**
     * Inserta o actualiza una lista de publicaciones.
     * Usa [OnConflictStrategy.REPLACE] para sobrescribir datos antiguos con los nuevos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    /**
     * Realiza una búsqueda filtrada reactiva.
     * * Soporta filtrado por ID exacto y/o coincidencia parcial en el título.
     * * Si los parámetros son nulos, se ignoran en la cláusula WHERE.
     */
    @Query("""
        SELECT * FROM posts_table 
        WHERE (:id IS NULL OR id = :id) 
        AND (:title IS NULL OR title LIKE '%' || :title || '%')
        ORDER BY id ASC
    """)
    fun observeFilteredPosts(id: Int?, title: String?): Flow<List<PostEntity?>>

    /**
     * Elimina registros que ya no son válidos (no presentes en la última sincronización).
     * @param remainingIds Lista de IDs que deben permanecer en la base de datos.
     */
    @Query("DELETE FROM posts_table WHERE id NOT IN (:remainingIds)")
    suspend fun deleteOrphans(remainingIds: List<Int>)

    /**
     * Ejecuta una sincronización diferencial atómica.
     * 1. Actualiza/Inserta los datos recibidos.
     * 2. Elimina los datos locales que ya no existen en la fuente remota.
     * * [Transaction] asegura que toda la operación sea 'Todo o Nada'.
     */
    @Transaction
    suspend fun clearAndInsertPosts(posts: List<PostEntity>) {
        insertPosts(posts)
        val ids = posts.map { it.id }
        deleteOrphans(ids)
    }
}