package com.example.footystats.data.model

data class CompetitionDetails(
    val area: Area,
    val id: Int,
    val name: String,
    val code: String,
    val type: String,
    val emblem: String?,
    val seasons: List<Season>
)
