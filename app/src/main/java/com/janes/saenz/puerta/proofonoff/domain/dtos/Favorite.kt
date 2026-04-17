package com.janes.saenz.puerta.proofonoff.domain.dtos


data class Favorite(
    val favoriteId: Int,
    val postId: Int,
    val createdAt: Long
)