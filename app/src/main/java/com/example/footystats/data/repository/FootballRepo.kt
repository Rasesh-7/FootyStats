package com.example.footystats.data.repository

import com.example.footystats.data.remote.RetrofitInstance

class FootballRepo {
    suspend fun getCompetitions() = RetrofitInstance.api.getCompetitions()
    suspend fun getCompetitionDetails(id: Int) = RetrofitInstance.api.getCompetitionDetails(id)
    suspend fun getStandings(id: Int) = RetrofitInstance.api.getStandings(id)
    suspend fun getMatches(id: Int) = RetrofitInstance.api.getMatches(id)
}