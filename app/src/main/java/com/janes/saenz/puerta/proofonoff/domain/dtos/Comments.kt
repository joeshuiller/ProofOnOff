package com.janes.saenz.puerta.proofonoff.domain.dtos

data class Comments(
    val id: Int = 0,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis()
)
