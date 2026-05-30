package com.example.footystats.appui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.footystats.R
import com.example.footystats.viewmodel.FootballViewModel
import kotlinx.coroutines.launch

@Composable
fun CompetitionDetailsScreen(competitionId: Int) {

    val viewModel: FootballViewModel = viewModel()

    val competitionDetails = viewModel.competitionDetails.collectAsState()
    val standings = viewModel.standings.collectAsState()
    val matches = viewModel.matches.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    LaunchedEffect(competitionId) {
        viewModel.getCompetitionDetails(competitionId)
        viewModel.getStandings(competitionId)
        viewModel.getMatches(competitionId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = isLoading.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                }
            }

            AnimatedVisibility(
                visible = !isLoading.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                competitionDetails.value?.let { details ->

                    val tabs = listOf("Standings", "Matches")

                    val pagerState = rememberPagerState(
                        pageCount = { tabs.size }
                    )

                    val scope = rememberCoroutineScope()

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        // ================= HEADER =================

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = details.name,
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = details.type.uppercase(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    // Flag/Logo
                                    Surface(
                                        modifier = Modifier.size(56.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            if (details.area.name == "World") {
                                                Image(
                                                    painter = painterResource(R.drawable.fifa_cover),
                                                    contentDescription = "FIFA",
                                                    modifier = Modifier.size(40.dp)
                                                )
                                            } else {
                                                details.area.flag?.let { flagUrl ->
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(flagUrl)
                                                            .decoderFactory(SvgDecoder.Factory())
                                                            .crossfade(true)
                                                            .build(),
                                                        contentDescription = details.area.name,
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .clip(RoundedCornerShape(8.dp)),
                                                        error = painterResource(R.drawable.placeholder)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Region",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = details.area.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        // ================= TABS =================

                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp
                        ) {
                            TabRow(
                                selectedTabIndex = pagerState.currentPage,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                indicator = { tabPositions ->
                                    TabRowDefaults.PrimaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(
                                            tabPositions[pagerState.currentPage]
                                        ),
                                        width = 48.dp,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                                }
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(
                                        selected = pagerState.currentPage == index,
                                        onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        },
                                        text = {
                                            Text(
                                                text = title,
                                                fontWeight = if (pagerState.currentPage == index)
                                                    FontWeight.Bold else FontWeight.Medium,
                                                color = if (pagerState.currentPage == index)
                                                    MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        // ================= PAGER =================

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->

                            when (page) {

                                // ================= STANDINGS PAGE =================

                                0 -> {

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
                                    ) {

                                        standings.value.forEach { standing ->

                                            standing.group?.let { groupName ->
                                                item {
                                                    Text(
                                                        text = groupName,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        modifier = Modifier.padding(vertical = 4.dp)
                                                    )
                                                }
                                            }

                                            item {
                                                Surface(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(12.dp)
                                                    ) {
                                                        Text(
                                                            text = "#",
                                                            modifier = Modifier.weight(0.5f),
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )

                                                        Text(
                                                            text = "Club",
                                                            modifier = Modifier.weight(2f),
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )

                                                        Text(
                                                            text = "P",
                                                            modifier = Modifier.weight(0.7f),
                                                            fontWeight = FontWeight.Bold,
                                                            textAlign = TextAlign.Center,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )

                                                        Text(
                                                            text = "Pts",
                                                            modifier = Modifier.weight(0.7f),
                                                            fontWeight = FontWeight.Bold,
                                                            textAlign = TextAlign.Center,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                    }
                                                }
                                            }

                                            items(standing.table) { team ->

                                                Card(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(16.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                                                    ),
                                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                                ) {

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(14.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {

                                                        // Position badge
                                                        Surface(
                                                            modifier = Modifier.size(28.dp),
                                                            shape = CircleShape,
                                                            color = when (team.position) {
                                                                in 1..4 -> MaterialTheme.colorScheme.primary
                                                                in 5..6 -> MaterialTheme.colorScheme.tertiary
                                                                else -> MaterialTheme.colorScheme.surfaceContainerHighest
                                                            }
                                                        ) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Text(
                                                                    text = team.position.toString(),
                                                                    style = MaterialTheme.typography.labelMedium,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = when (team.position) {
                                                                        in 1..6 -> MaterialTheme.colorScheme.onPrimary
                                                                        else -> MaterialTheme.colorScheme.onSurface
                                                                    }
                                                                )
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.width(12.dp))

                                                        Row(
                                                            modifier = Modifier.weight(2f),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {

                                                            team.team.crest?.let { crestUrl ->
                                                                AsyncImage(
                                                                    model = ImageRequest.Builder(
                                                                        LocalContext.current
                                                                    )
                                                                        .data(crestUrl)
                                                                        .decoderFactory(SvgDecoder.Factory())
                                                                        .crossfade(true)
                                                                        .build(),
                                                                    contentDescription = team.team.name ?: "Club",
                                                                    modifier = Modifier.size(32.dp),
                                                                    error = painterResource(R.drawable.placeholder)
                                                                )

                                                                Spacer(modifier = Modifier.width(10.dp))
                                                            }

                                                            Text(
                                                                text = team.team.shortName
                                                                    ?: team.team.name
                                                                    ?: "TBD",
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                fontWeight = FontWeight.Medium,
                                                                color = MaterialTheme.colorScheme.onSurface,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }

                                                        Text(
                                                            text = team.playedGames.toString(),
                                                            modifier = Modifier.weight(0.7f),
                                                            textAlign = TextAlign.Center,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )

                                                        Surface(
                                                            shape = RoundedCornerShape(6.dp),
                                                            color = MaterialTheme.colorScheme.secondaryContainer
                                                        ) {
                                                            Text(
                                                                text = team.points.toString(),
                                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                                fontWeight = FontWeight.Bold,
                                                                textAlign = TextAlign.Center,
                                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // ================= MATCHES PAGE =================

                                1 -> {

                                    // Sort matches: for LEAGUE type, show recent matches first (descending by date)
                                    val sortedMatches = if (details.type.uppercase() == "LEAGUE") {
                                        matches.value.sortedByDescending { it.utcDate }
                                    } else {
                                        matches.value
                                    }

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
                                    ) {

                                        items(sortedMatches) { match ->

                                            val homeTeam = match.homeTeam
                                            val awayTeam = match.awayTeam

                                            val homeScore = match.score?.fullTime?.home
                                            val awayScore = match.score?.fullTime?.away

                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(20.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                                                ),
                                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                            ) {

                                                Column(
                                                    modifier = Modifier.padding(16.dp)
                                                ) {

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Surface(
                                                            shape = RoundedCornerShape(8.dp),
                                                            color = MaterialTheme.colorScheme.primaryContainer
                                                        ) {
                                                            Text(
                                                                text = match.stage ?: "Match",
                                                                style = MaterialTheme.typography.labelSmall,
                                                                fontWeight = FontWeight.SemiBold,
                                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                            )
                                                        }

                                                        Text(
                                                            text = match.utcDate?.take(10) ?: "-",
                                                            style = MaterialTheme.typography.labelMedium,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.height(20.dp))

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {

                                                        // HOME
                                                        Column(
                                                            modifier = Modifier.weight(1f),
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Surface(
                                                                modifier = Modifier.size(56.dp),
                                                                shape = CircleShape,
                                                                color = MaterialTheme.colorScheme.surfaceContainerHighest
                                                            ) {
                                                                Box(
                                                                    contentAlignment = Alignment.Center,
                                                                    modifier = Modifier.padding(8.dp)
                                                                ) {
                                                                    homeTeam?.crest?.let { crestUrl ->
                                                                        AsyncImage(
                                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                                .data(crestUrl)
                                                                                .decoderFactory(SvgDecoder.Factory())
                                                                                .crossfade(true)
                                                                                .build(),
                                                                            contentDescription = homeTeam.name ?: "Home Team",
                                                                            modifier = Modifier.size(40.dp),
                                                                            error = painterResource(R.drawable.placeholder)
                                                                        )
                                                                    }
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            Text(
                                                                text = homeTeam?.shortName
                                                                    ?: homeTeam?.name
                                                                    ?: "TBD",
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                fontWeight = FontWeight.Medium,
                                                                textAlign = TextAlign.Center,
                                                                maxLines = 2,
                                                                overflow = TextOverflow.Ellipsis,
                                                                color = MaterialTheme.colorScheme.onSurface
                                                            )
                                                        }

                                                        // SCORE
                                                        Column(
                                                            modifier = Modifier.weight(0.8f),
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Surface(
                                                                shape = RoundedCornerShape(12.dp),
                                                                color = if (homeScore != null && awayScore != null)
                                                                    MaterialTheme.colorScheme.secondaryContainer
                                                                else MaterialTheme.colorScheme.surfaceContainerHighest
                                                            ) {
                                                                Text(
                                                                    text = if (homeScore != null && awayScore != null) {
                                                                        "$homeScore - $awayScore"
                                                                    } else {
                                                                        "VS"
                                                                    },
                                                                    style = MaterialTheme.typography.titleLarge,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = if (homeScore != null && awayScore != null)
                                                                        MaterialTheme.colorScheme.onSecondaryContainer
                                                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                                                )
                                                            }

                                                            Spacer(modifier = Modifier.height(6.dp))

                                                            Surface(
                                                                shape = RoundedCornerShape(6.dp),
                                                                color = when (match.status) {
                                                                    "FINISHED" -> MaterialTheme.colorScheme.tertiaryContainer
                                                                    "IN_PLAY", "LIVE" -> MaterialTheme.colorScheme.errorContainer
                                                                    else -> MaterialTheme.colorScheme.surfaceContainerHigh
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = match.status ?: "TIMED",
                                                                    style = MaterialTheme.typography.labelSmall,
                                                                    fontWeight = FontWeight.Medium,
                                                                    color = when (match.status) {
                                                                        "FINISHED" -> MaterialTheme.colorScheme.onTertiaryContainer
                                                                        "IN_PLAY", "LIVE" -> MaterialTheme.colorScheme.onErrorContainer
                                                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                                    },
                                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                                )
                                                            }
                                                        }

                                                        // AWAY
                                                        Column(
                                                            modifier = Modifier.weight(1f),
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Surface(
                                                                modifier = Modifier.size(56.dp),
                                                                shape = CircleShape,
                                                                color = MaterialTheme.colorScheme.surfaceContainerHighest
                                                            ) {
                                                                Box(
                                                                    contentAlignment = Alignment.Center,
                                                                    modifier = Modifier.padding(8.dp)
                                                                ) {
                                                                    awayTeam?.crest?.let { crestUrl ->
                                                                        AsyncImage(
                                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                                .data(crestUrl)
                                                                                .decoderFactory(SvgDecoder.Factory())
                                                                                .crossfade(true)
                                                                                .build(),
                                                                            contentDescription = awayTeam.name ?: "Away Team",
                                                                            modifier = Modifier.size(40.dp),
                                                                            error = painterResource(R.drawable.placeholder)
                                                                        )
                                                                    }
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            Text(
                                                                text = awayTeam?.shortName
                                                                    ?: awayTeam?.name
                                                                    ?: "TBD",
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                fontWeight = FontWeight.Medium,
                                                                textAlign = TextAlign.Center,
                                                                maxLines = 2,
                                                                overflow = TextOverflow.Ellipsis,
                                                                color = MaterialTheme.colorScheme.onSurface
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}