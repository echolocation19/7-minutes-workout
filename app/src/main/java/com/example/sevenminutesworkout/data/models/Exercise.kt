package com.example.sevenminutesworkout.data.models

data class Exercise(
    val id: Int,
    val name: String,
    val img: Int,
    var isCompleted: Boolean = false,
    var isSelected: Boolean = false
)