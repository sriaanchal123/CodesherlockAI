package com.example.codesherlockai.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MergeType
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codesherlockai.domain.model.EvidenceSummaryItem
import com.example.codesherlockai.ui.components.GlassCard
import com.example.codesherlockai.ui.components.PrimaryButton
import com.example.codesherlockai.ui.components.SectionHeader
import com.example.codesherlockai.ui.theme.*
import com.example.codesherlockai.viewmodel.EvidenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestigationResultScreen(
    issueNumber: String,
    onShowEvidence: (String) -> Unit,
    onBackToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EvidenceViewModel = viewModel()
) {
    val result by viewModel.result.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(issueNumber) {
        viewModel.loadResult(issueNumber)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Investigation Report", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackToDashboard) {
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
        }
    ) { innerPadding ->
        result?.let { report ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                SectionHeader(
                    title = "Investigation Complete",
                    subtitle = "Report generated for Issue ${report.issueNumber}"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Prominent Result Card: Probable Root Cause & Confidence
                ProbableRootCauseCard(
                    rootCause = report.probableRootCause,
                    confidenceScore = report.confidenceScore,
                    confidenceNote = report.confidenceNote
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Evidence Summary Section
                Text(
                    text = "Evidence Summary",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        report.evidenceSummary.forEach { item ->
                            EvidenceSummaryRow(item = item)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Jira & Slack Actions Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Jira Card
                    GlassCard(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Task, contentDescription = null, tint = SecondaryBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Jira Action", fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("${report.jiraResult.ticketId} created", fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Slack Card
                    GlassCard(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = AccentViolet, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Team Notification", fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Team notified", fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // CTA Buttons
                PrimaryButton(
                    text = "Show Evidence",
                    icon = Icons.Default.Description,
                    onClick = { onShowEvidence(report.issueNumber) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBackToDashboard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                ) {
                    Text("View Investigation Dashboard", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProbableRootCauseCard(
    rootCause: String,
    confidenceScore: Int,
    confidenceNote: String
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        borderColor = PrimaryCyan.copy(alpha = 0.5f),
        backgroundColor = DarkSurfaceCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = PrimaryCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Probable Root Cause",
                        fontSize = 12.sp,
                        color = PrimaryCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Confidence Badge
                Box(
                    modifier = Modifier
                        .background(StatusSuccess.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp))
                        .border(1.dp, StatusSuccess.copy(alpha = 0.4f), shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$confidenceScore% Confidence",
                        fontSize = 12.sp,
                        color = StatusSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = rootCause,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = confidenceNote,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun EvidenceSummaryRow(item: EvidenceSummaryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(StatusSuccess.copy(alpha = 0.15f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = StatusSuccess,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        val itemIcon = when (item.iconType) {
            "github" -> Icons.Default.Tag
            "pr" -> Icons.AutoMirrored.Filled.MergeType
            "commit" -> Icons.Default.Commit
            else -> Icons.Default.Code
        }

        Icon(
            imageVector = itemIcon,
            contentDescription = null,
            tint = PrimaryCyan,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = item.detail,
            fontSize = 13.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InvestigationResultScreenPreview() {
    CodeSherlockAITheme {
        InvestigationResultScreen(
            issueNumber = "#421",
            onShowEvidence = {},
            onBackToDashboard = {}
        )
    }
}
