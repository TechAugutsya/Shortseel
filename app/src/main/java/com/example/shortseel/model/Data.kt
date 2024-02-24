package com.example.shortseel.model

data class Data(
    val offset: Int,
    val page: Int,
    val posts: List<Post>
)