package com.example.footystats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footystats.data.model.Competition
import com.example.footystats.data.model.CompetitionDetails
import com.example.footystats.data.model.Match
import com.example.footystats.data.model.StandingData
import com.example.footystats.data.model.StandingsResponse
import com.example.footystats.data.model.TableItem
import com.example.footystats.data.repository.FootballRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FootballViewModel: ViewModel() {
    private var footballRepo = FootballRepo()

    //Home Screen
    private val _competitions = MutableStateFlow<List<Competition>>(emptyList())
    val competitions: StateFlow<List<Competition>> = _competitions

    //Competition Details Screen
    private val _competitionDetails = MutableStateFlow<CompetitionDetails?>(null)
    val competitionDetails: StateFlow<CompetitionDetails?> = _competitionDetails

    //Standings
    private val _standings = MutableStateFlow<List<StandingData>>(emptyList())
    val standings: StateFlow<List<StandingData>> = _standings

    //Matches
    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getCompetition()
    }
    private fun getCompetition(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = footballRepo.getCompetitions()
                _competitions.value = response.competitions
            }catch (e: Exception){
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun getCompetitionDetails(id : Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = footballRepo.getCompetitionDetails(id)
                _competitionDetails.value = response
            }
            catch(e: Exception){
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun getStandings(id: Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = footballRepo.getStandings(id)
                _standings.value = response.standings
            }
            catch(e: Exception){
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun getMatches(id: Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _matches.value = footballRepo.getMatches(id).matches
            }
            catch(e: Exception){
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}