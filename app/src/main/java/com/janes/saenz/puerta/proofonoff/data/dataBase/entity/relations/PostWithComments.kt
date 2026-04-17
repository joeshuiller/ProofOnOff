package com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity

data class PostWithComments(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "id", // ID del PostEntity
        entityColumn = "postId" // postId del CommentEntity
    )
    val comments: List<CommentEntity>
)
