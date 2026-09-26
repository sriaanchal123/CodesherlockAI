package com.example.codesherlockai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesherlockai.domain.model.Investigation
import com.example.codesherlockai.domain.model.InvestigationStatus
import com.example.codesherlockai.ui.theme.CodeSherlockAITheme
import com.example.codesherlockai.ui.theme.PrimaryCyan
import com.example.codesherlockai.ui.theme.TextMuted
import com.example.codesherlockai.ui.theme.TextPrimary
import com.example.codesherlockai.ui.theme.TextSecondary

@Composable
fun InvestigationCard(
    investigation: Investigation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = investigation.issueNumber,
                        color = PrimaryCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = investigation.title,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = investigation.timeAgo,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = investigation.description,
                color = TextSecondary,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = investigation.status)

                Text(
                    text = investigation.repository,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun InvestigationCardPreview() {
    CodeSherlockAITheme {
        InvestigationCard(
            investigation = Investigation(
                id = "421",
                issueNumber = "#421",
                title = "Payment Crash",
                description = "Probable payment response regression",
                status = InvestigationStatus.ROOT_CAUSE_FOUND,
                timeAgo = "2 min ago",
                repository = "company/android-app",
                instruction = "Find the root cause"
            ),
            onClick = {}
        )
    }
}
