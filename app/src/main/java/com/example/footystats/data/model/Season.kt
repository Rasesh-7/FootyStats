package com.example.footystats.data.model

data class Season(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val currentMatchday: Int?,
    val winner: Winner?
)
