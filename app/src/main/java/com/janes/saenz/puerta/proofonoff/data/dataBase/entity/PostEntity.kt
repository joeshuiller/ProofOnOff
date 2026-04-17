package com.janes.saenz.puerta.proofonoff.data.dataBase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_table")
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
