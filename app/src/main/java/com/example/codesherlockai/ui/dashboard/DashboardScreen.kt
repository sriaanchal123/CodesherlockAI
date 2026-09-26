package com.example.codesherlockai.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codesherlockai.domain.model.DashboardMetrics
import com.example.codesherlockai.ui.components.*
import com.example.codesherlockai.ui.theme.*
import com.example.codesherlockai.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onNewInvestigationClick: () -> Unit,
    onInvestigationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {
    val investigations by viewModel.investigations.collectAsState()
    val metrics by viewModel.metrics.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            DashboardBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            // Top Header Section
            item {
                HeaderSection()
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Primary CTA: Large Card
            item {
                NewInvestigationCTA(onClick = onNewInvestigationClick)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Statistics Grid Section
            item {
                SectionHeader(
                    title = "System Metrics",
                    subtitle = "Real-time AI diagnostic overview"
                )
                Spacer(modifier = Modifier.height(12.dp))
                StatisticsGrid(metrics = metrics)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recent Investigations Section
            item {
                SectionHeader(
                    title = "Recent Investigations",
                    subtitle = "Latest bug root-cause analyses"
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(investigations) { investigation ->
                InvestigationCard(
                    investigation = investigation,
                    onClick = { onInvestigationClick(investigation.issueNumber) },
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good morning 👋",
                fontSize = 13.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "CodeSherlock AI",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Autonomous AI Software Engineer",
                fontSize = 12.sp,
                color = PrimaryCyan,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Status Avatar / Glow Badge
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    Brush.linearGradient(colors = listOf(PrimaryCyan, AccentViolet)),
                    shape = CircleShape
                )
                .padding(2.dp)
                .background(DarkSurface, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "AI Agent Active",
                tint = PrimaryCyan,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun NewInvestigationCTA(onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        borderColor = PrimaryCyan.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(listOf(PrimaryCyan, SecondaryBlue)),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = DarkBackground,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "New Investigation",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Investigate a GitHub issue and find its probable root cause.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = PrimaryCyan,
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun StatisticsGrid(metrics: DashboardMetrics) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Investigations",
                value = metrics.totalInvestigations.toString(),
                icon = Icons.Default.BugReport,
                accentColor = PrimaryCyan,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Root Causes Found",
                value = metrics.rootCausesFound.toString(),
                icon = Icons.Default.Psychology,
                accentColor = StatusSuccess,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Jira Tasks",
                value = metrics.jiraTasksCreated.toString(),
                icon = Icons.Default.Task,
                accentColor = SecondaryBlue,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Team Alerts",
                value = metrics.teamAlertsSent.toString(),
                icon = Icons.Default.Notifications,
                accentColor = AccentViolet,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DashboardBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        color = DarkSurface,
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
    ) {
        NavigationBar(
            containerColor = DarkSurface,
            contentColor = TextPrimary
        ) {
            val navItems = listOf(
                "Home" to Icons.Default.Home,
                "Investigations" to Icons.Default.Search,
                "Activity" to Icons.Default.Assessment,
                "Settings" to Icons.Default.Settings
            )

            navItems.forEachIndexed { index, pair ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    icon = {
                        Icon(
                            imageVector = pair.second,
                            contentDescription = pair.first
                        )
                    },
                    label = {
                        Text(
                            text = pair.first,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkBackground,
                        selectedTextColor = PrimaryCyan,
                        indicatorColor = PrimaryCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    CodeSherlockAITheme {
        DashboardScreen(
            onNewInvestigationClick = {},
            onInvestigationClick = {}
        )
    }
}
