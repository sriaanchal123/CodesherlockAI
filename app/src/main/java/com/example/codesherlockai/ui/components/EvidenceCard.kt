package com.example.codesherlockai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesherlockai.ui.theme.CodeSherlockAITheme
import com.example.codesherlockai.ui.theme.PrimaryCyan
import com.example.codesherlockai.ui.theme.TextMuted
import com.example.codesherlockai.ui.theme.TextPrimary
import com.example.codesherlockai.ui.theme.TextSecondary

@Composable
fun EvidenceCard(
    categoryTitle: String,
    contentTitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = PrimaryCyan,
    contentSubtitle: String? = null
) {
    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(iconTint.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = categoryTitle,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryTitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = contentTitle,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                if (contentSubtitle != null) {
                    Text(
                        text = contentSubtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EvidenceCardPreview() {
    CodeSherlockAITheme {
        EvidenceCard(
            categoryTitle = "GitHub Issue",
            contentTitle = "#421 — Payment crash",
            icon = Icons.Default.Tag
        )
    }
}
