package com.example.sevenminutesworkout.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history-table")
data class History(
    @PrimaryKey
    var date: String
)
