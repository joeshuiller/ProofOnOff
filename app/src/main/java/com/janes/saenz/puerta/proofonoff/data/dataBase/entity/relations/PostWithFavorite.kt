package com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity

data class PostWithFavorite(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val favorite: FavoriteEntity?
)