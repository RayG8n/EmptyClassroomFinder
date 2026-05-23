package com.example.emptyclassroomfinder.data

data class Group(
    val name: String,
    val owner: String,
    val members: List<String> = emptyList()
)
