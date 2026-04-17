package com.janes.saenz.puerta.proofonoff.data.dataBase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE // Si el post se borra, el favorito también
        )
    ],
    indices = [Index(value = ["postId"], unique = true)] // Un post solo puede ser favorito una vez
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var favoriteId: Int = 0,
    val postId: Int, // Relación con PostEntity
    @ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis() // Metadata útil
)
