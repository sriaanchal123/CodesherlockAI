package com.example.codesherlockai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.codesherlockai.ui.theme.CodeSherlockAITheme
import com.example.codesherlockai.ui.theme.DarkSurfaceCard
import com.example.codesherlockai.ui.theme.GlassBorder
import com.example.codesherlockai.ui.theme.GlassFill
import com.example.codesherlockai.ui.theme.TextPrimary

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    backgroundColor: Color = GlassFill,
    borderColor: Color = GlassBorder,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        shape = shape,
        color = backgroundColor,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkSurfaceCard.copy(alpha = 0.6f),
                            GlassFill
                        )
                    )
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun GlassCardPreview() {
    CodeSherlockAITheme {
        GlassCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Glass Card Content",
                color = TextPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
