package com.example.footystats.data.remote

import com.example.footystats.data.model.CompetitionDetails
import com.example.footystats.data.model.CompetitionResponse
import com.example.footystats.data.model.MatchesResponse
import com.example.footystats.data.model.StandingsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FootballApi {
    @GET("competitions")
    suspend fun getCompetitions(): CompetitionResponse

    @GET("competitions/{id}")
    suspend fun getCompetitionDetails(
        @Path("id") id: Int
    ): CompetitionDetails

    @GET("competitions/{id}/standings")
    suspend fun getStandings(
        @Path("id") id: Int
    ): StandingsResponse

    @GET("competitions/{id}/matches")
    suspend fun getMatches(
        @Path("id") id: Int
    ): MatchesResponse
}