package com.example.codesherlockai.ui.agent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codesherlockai.ui.components.AgentStepItem
import com.example.codesherlockai.ui.components.GlassCard
import com.example.codesherlockai.ui.components.PrimaryButton
import com.example.codesherlockai.ui.components.SectionHeader
import com.example.codesherlockai.ui.theme.*
import com.example.codesherlockai.viewmodel.AgentExecutionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentExecutionScreen(
    issueNumber: String,
    onNavigateBack: () -> Unit,
    onViewResult: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgentExecutionViewModel = viewModel()
) {
    val steps by viewModel.steps.collectAsState()
    val activityLogs by viewModel.activityLogs.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()

    LaunchedEffect(issueNumber) {
        viewModel.startExecution(issueNumber)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("CodeSherlock Agent", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            AgentBottomBar(
                isFinished = isFinished,
                onViewResult = { onViewResult(issueNumber) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle Header
            SectionHeader(
                title = "Autonomous Execution",
                subtitle = "Investigating Issue $issueNumber"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Live Activity Panel
            LiveActivityPanel(
                logs = activityLogs,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Steps Timeline Section Title
            Text(
                text = "Agent Workflow Timeline",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Vertical Timeline LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(steps) { index, step ->
                    AgentStepItem(
                        step = step,
                        isLastStep = index == steps.size - 1
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveActivityPanel(
    logs: List<String>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        borderColor = PrimaryCyan.copy(alpha = 0.3f),
        backgroundColor = DarkSurfaceCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(PrimaryCyan, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = PrimaryCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Agent activity",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                reverseLayout = false
            ) {
                itemsIndexed(logs) { _, log ->
                    Text(
                        text = "› $log",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AgentBottomBar(
    isFinished: Boolean,
    onViewResult: () -> Unit
) {
    Surface(
        color = DarkSurface,
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isFinished) {
                PrimaryButton(
                    text = "View Investigation Result",
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = onViewResult
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = PrimaryCyan,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Agent is reasoning over repository evidence...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgentExecutionScreenPreview() {
    CodeSherlockAITheme {
        AgentExecutionScreen(
            issueNumber = "#421",
            onNavigateBack = {},
            onViewResult = {}
        )
    }
}
