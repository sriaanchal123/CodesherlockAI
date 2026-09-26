package com.example.codesherlockai.ui.investigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codesherlockai.domain.model.InvestigationDepth
import com.example.codesherlockai.ui.components.GlassCard
import com.example.codesherlockai.ui.components.PrimaryButton
import com.example.codesherlockai.ui.components.SectionHeader
import com.example.codesherlockai.ui.theme.*
import com.example.codesherlockai.viewmodel.InvestigationUiState
import com.example.codesherlockai.viewmodel.InvestigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInvestigationScreen(
    onNavigateBack: () -> Unit,
    onStartInvestigation: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InvestigationViewModel = viewModel()
) {
    val githubIssue by viewModel.githubIssue.collectAsState()
    val repositoryName by viewModel.repositoryName.collectAsState()
    val instruction by viewModel.instruction.collectAsState()
    val selectedDepth by viewModel.selectedDepth.collectAsState()
    val issueError by viewModel.issueError.collectAsState()
    val repoError by viewModel.repoError.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val isLoading = uiState is InvestigationUiState.Loading

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("CodeSherlock AI", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            SectionHeader(
                title = "New Investigation",
                subtitle = "Tell CodeSherlock what you want to investigate."
            )

            Spacer(modifier = Modifier.height(24.dp))

            // GitHub Issue Field
            Text(
                text = "GitHub Issue Number",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = githubIssue,
                onValueChange = { viewModel.onIssueChanged(it) },
                placeholder = { Text("#421", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = PrimaryCyan) },
                isError = issueError != null,
                supportingText = issueError?.let { { Text(it, color = StatusError) } },
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Repository Field
            Text(
                text = "Target Repository",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = repositoryName,
                onValueChange = { viewModel.onRepoChanged(it) },
                placeholder = { Text("company/android-app", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = PrimaryCyan) },
                isError = repoError != null,
                supportingText = repoError?.let { { Text(it, color = StatusError) } },
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Investigation Instruction Field
            Text(
                text = "Investigation Instruction",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = instruction,
                onValueChange = { viewModel.onInstructionChanged(it) },
                placeholder = { Text("Find the probable root cause of this issue.", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = PrimaryCyan) },
                minLines = 3,
                maxLines = 5,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Investigation Depth Selection
            Text(
                text = "Investigation Depth",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InvestigationDepth.entries.forEach { depth ->
                    DepthChip(
                        depth = depth,
                        isSelected = depth == selectedDepth,
                        onSelect = { if (!isLoading) viewModel.onDepthSelected(depth) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Description of selected depth
            Text(
                text = selectedDepth.description,
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 8.dp, start = 2.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Network Error Feedback Banner
            AnimatedVisibility(
                visible = uiState is InvestigationUiState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (uiState is InvestigationUiState.Error) {
                    val errorMessage = (uiState as InvestigationUiState.Error).message
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        borderColor = StatusError,
                        backgroundColor = DarkSurfaceCard
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Error",
                                tint = StatusError,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = errorMessage,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.clearError() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Primary Button
            PrimaryButton(
                text = if (isLoading) "Connecting to FastAPI..." else "Investigate Bug",
                icon = Icons.Default.Search,
                isLoading = isLoading,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.submitInvestigation(onStartInvestigation)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DepthChip(
    depth: InvestigationDepth,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) PrimaryCyan else GlassBorder
    val bgColor = if (isSelected) PrimaryCyan.copy(alpha = 0.15f) else DarkSurfaceVariant

    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = bgColor,
        borderColor = borderColor,
        onClick = onSelect
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = depth.displayName,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PrimaryCyan else TextSecondary
            )
        }
    }
}

@Composable
private fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = DarkSurfaceVariant,
    unfocusedContainerColor = DarkSurface,
    errorContainerColor = DarkSurface,
    focusedBorderColor = PrimaryCyan,
    unfocusedBorderColor = GlassBorder,
    errorBorderColor = StatusError,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = PrimaryCyan
)

@Preview(showBackground = true)
@Composable
fun NewInvestigationScreenPreview() {
    CodeSherlockAITheme {
        NewInvestigationScreen(
            onNavigateBack = {},
            onStartInvestigation = {}
        )
    }
}
