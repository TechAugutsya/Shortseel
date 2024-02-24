package com.example.shortseel.model

data class Post(
    val comment: Comment,
    val creator: Creator,
    val postId: String,
    val reaction: Reaction,
    val submission: Submission
)