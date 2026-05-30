package com.example.footystats.data.model

data class TableItem(
    val position: Int,
    val team: Winner,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int
)
