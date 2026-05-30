package com.example.footystats.data.model

data class Match(
    val id: Int,
    val utcDate: String?,
    val status: String?,
    val matchday: Int?,
    val stage: String?,
    val homeTeam: Winner?,
    val awayTeam: Winner?,
    val score: Score?
)
