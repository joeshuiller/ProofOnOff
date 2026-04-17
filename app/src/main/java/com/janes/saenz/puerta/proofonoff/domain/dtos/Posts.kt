package com.janes.saenz.puerta.proofonoff.domain.dtos


data class Posts(
    val userId: Int,
    val id: Int,
    val title: String?,
    val body: String?,
    val createdAt: Long,
    val updatedAt: Long
)
