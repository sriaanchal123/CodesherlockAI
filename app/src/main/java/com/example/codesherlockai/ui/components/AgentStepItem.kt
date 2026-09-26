package com.example.codesherlockai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesherlockai.domain.model.AgentStep
import com.example.codesherlockai.domain.model.AgentStepStatus
import com.example.codesherlockai.ui.theme.*

@Composable
fun AgentStepItem(
    step: AgentStep,
    isLastStep: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Vertical Timeline Column (Icon + Line)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            // Status Icon Container
            val (bgColor, borderColor, iconTint) = when (step.status) {
                AgentStepStatus.COMPLETED -> Triple(StatusSuccess.copy(alpha = 0.2f), StatusSuccess, StatusSuccess)
                AgentStepStatus.RUNNING -> Triple(PrimaryCyan.copy(alpha = 0.2f), PrimaryCyan, PrimaryCyan)
                AgentStepStatus.FAILED -> Triple(StatusError.copy(alpha = 0.2f), StatusError, StatusError)
                AgentStepStatus.PENDING -> Triple(DarkSurfaceVariant, GlassBorder, TextMuted)
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(bgColor, shape = CircleShape)
                    .border(1.dp, borderColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when (step.status) {
                    AgentStepStatus.COMPLETED -> Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                    AgentStepStatus.RUNNING -> CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = PrimaryCyan,
                        strokeWidth = 2.dp
                    )
                    AgentStepStatus.FAILED -> Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Failed",
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                    AgentStepStatus.PENDING -> Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = "Pending",
                        tint = TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            if (!isLastStep) {
                val lineGradient = when (step.status) {
                    AgentStepStatus.COMPLETED -> StatusSuccess
                    AgentStepStatus.RUNNING -> PrimaryCyan
                    else -> GlassBorder
                }
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(38.dp)
                        .background(lineGradient)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Step Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = step.title,
                fontSize = 15.sp,
                fontWeight = if (step.status == AgentStepStatus.RUNNING) FontWeight.Bold else FontWeight.SemiBold,
                color = when (step.status) {
                    AgentStepStatus.COMPLETED -> TextPrimary
                    AgentStepStatus.RUNNING -> PrimaryCyan
                    AgentStepStatus.FAILED -> StatusError
                    AgentStepStatus.PENDING -> TextMuted
                }
            )

            Text(
                text = step.description,
                fontSize = 12.sp,
                color = if (step.status == AgentStepStatus.PENDING) TextMuted else TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Step Log Pills
            AnimatedVisibility(
                visible = step.status == AgentStepStatus.RUNNING || step.status == AgentStepStatus.COMPLETED,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 6.dp)) {
                    step.logs.forEach { log ->
                        Box(
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .background(DarkSurfaceVariant, shape = RoundedCornerShape(6.dp))
                                .border(0.5.dp, GlassBorder, shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "› $log",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = PrimaryCyanVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AgentStepItemPreview() {
    CodeSherlockAITheme {
        AgentStepItem(
            step = AgentStep(
                id = 1,
                title = "Retrieving RAG Evidence",
                description = "Querying vector database for code context",
                status = AgentStepStatus.RUNNING,
                logs = listOf("Retrieved PaymentRepository.kt", "Retrieved PaymentResponse.kt")
            ),
            isLastStep = false
        )
    }
}
