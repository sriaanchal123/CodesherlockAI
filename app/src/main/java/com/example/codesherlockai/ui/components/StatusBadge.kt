package com.example.codesherlockai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesherlockai.domain.model.InvestigationStatus
import com.example.codesherlockai.ui.theme.CodeSherlockAITheme
import com.example.codesherlockai.ui.theme.PrimaryCyan
import com.example.codesherlockai.ui.theme.StatusError
import com.example.codesherlockai.ui.theme.StatusInfo
import com.example.codesherlockai.ui.theme.StatusSuccess
import com.example.codesherlockai.ui.theme.StatusWarning

@Composable
fun StatusBadge(
    status: InvestigationStatus,
    modifier: Modifier = Modifier
) {
    val (badgeColor, textColor) = when (status) {
        InvestigationStatus.ROOT_CAUSE_FOUND -> StatusSuccess to StatusSuccess
        InvestigationStatus.JIRA_CREATED -> PrimaryCyan to PrimaryCyan
        InvestigationStatus.INVESTIGATED -> StatusInfo to StatusInfo
        InvestigationStatus.IN_PROGRESS -> StatusWarning to StatusWarning
        InvestigationStatus.FAILED -> StatusError to StatusError
    }

    Box(
        modifier = modifier
            .background(badgeColor.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp))
            .border(1.dp, badgeColor.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(textColor, shape = CircleShape)
            )
            Text(
                text = status.label,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

@Preview
@Composable
fun StatusBadgePreview() {
    CodeSherlockAITheme {
        StatusBadge(status = InvestigationStatus.ROOT_CAUSE_FOUND)
    }
}
