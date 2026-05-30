package com.example.footystats

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.footystats.data.remote.RetrofitInstance
import com.example.footystats.ui.theme.FootyStatsTheme
import com.example.footystats.viewmodel.FootballViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.footystats.appui.components.CompetitionCard
import com.example.footystats.appui.screens.CompetitionDetailsScreen
import com.example.footystats.appui.screens.HomeScreen
import com.example.footystats.data.model.Competition
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FootyStatsTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ){
                    composable("home") {
                        HomeScreen(onCompetitionClick = { id ->
                            navController.navigate("competitionDetails/$id")
                        })
                    }
                    composable("competitionDetails/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()

                        if(id != null){
                            CompetitionDetailsScreen(competitionId = id)
                        }
                    }
                }

            }
        }
    }
}

