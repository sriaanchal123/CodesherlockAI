package com.example.codesherlockai.ui.evidence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MergeType
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Tag
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
import com.example.codesherlockai.domain.model.TimelineStep
import com.example.codesherlockai.ui.components.EvidenceCard
import com.example.codesherlockai.ui.components.GlassCard
import com.example.codesherlockai.ui.components.PrimaryButton
import com.example.codesherlockai.ui.components.SectionHeader
import com.example.codesherlockai.ui.theme.*
import com.example.codesherlockai.viewmodel.EvidenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceDetailsScreen(
    issueNumber: String,
    onNavigateBack: () -> Unit,
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
                title = { Text("Evidence Breakdown", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
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
        }
    ) { innerPadding ->
        result?.evidenceDetails?.let { evidence ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                SectionHeader(
                    title = "Evidence",
                    subtitle = "Why did CodeSherlock reach this conclusion?"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Evidence Artifact Cards
                Text(
                    text = "Retrieved Artifacts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                EvidenceCard(
                    categoryTitle = "GitHub Issue",
                    contentTitle = evidence.githubIssue,
                    icon = Icons.Default.Tag,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                EvidenceCard(
                    categoryTitle = "Related Pull Request",
                    contentTitle = evidence.relatedPr,
                    icon = Icons.AutoMirrored.Filled.MergeType,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                EvidenceCard(
                    categoryTitle = "Relevant Commit",
                    contentTitle = evidence.relevantCommit,
                    icon = Icons.Default.Commit,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Retrieved Files Card
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FolderZip, contentDescription = null, tint = PrimaryCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retrieved Files", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        evidence.retrievedFiles.forEach { filename ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 3.dp)
                            ) {
                                Icon(Icons.Default.Code, contentDescription = null, tint = SecondaryBlue, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = filename,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // AI Reasoning Card
                Text(
                    text = "AI Reasoning",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = AccentViolet.copy(alpha = 0.4f),
                    backgroundColor = DarkSurfaceCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AccentViolet,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sherlock RAG Synthesis",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentViolet
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = evidence.aiReasoning,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Evidence Timeline Flow
                Text(
                    text = "Evidence Timeline Flow",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        evidence.timeline.forEachIndexed { index, step ->
                            TimelineStepItem(
                                step = step,
                                isLast = index == evidence.timeline.size - 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Back Button
                PrimaryButton(
                    text = "Back to Investigation",
                    icon = Icons.Default.Description,
                    onClick = onNavigateBack
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TimelineStepItem(
    step: TimelineStep,
    isLast: Boolean
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(PrimaryCyan, shape = CircleShape)
                    .border(2.dp, DarkBackground, shape = CircleShape)
            )

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(GlassBorder)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isLast) 0.dp else 12.dp)
        ) {
            Text(
                text = step.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryCyan
            )
            Text(
                text = step.description,
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EvidenceDetailsScreenPreview() {
    CodeSherlockAITheme {
        EvidenceDetailsScreen(
            issueNumber = "#421",
            onNavigateBack = {}
        )
    }
}
