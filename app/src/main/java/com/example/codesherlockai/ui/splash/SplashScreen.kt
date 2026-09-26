package com.example.codesherlockai.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesherlockai.ui.theme.CodeSherlockAITheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

private data class SplashParticle(
    val baseAngleDeg: Float,
    val orbitRadiusDp: Float,
    val speedMultiplier: Float,
    val sizeDp: Float,
    val alpha: Float,
    val color: Color
)

@Composable
fun CodeSherlockSplash(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animatable sequence state values
    val particlesAlpha = remember { Animatable(0f) }
    val coreAlpha = remember { Animatable(0f) }
    val coreScale = remember { Animatable(0.85f) }
    val circuitAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val loadingAlpha = remember { Animatable(0f) }
    val progressAnim = remember { Animatable(0f) }
    val screenAlpha = remember { Animatable(1f) }

    // Infinite transitions for ambient animations
    val infiniteTransition = rememberInfiniteTransition(label = "ambient")

    val ringRotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1"
    )

    val ringRotation2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring2"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val particleOrbitProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )

    // Sequence Controller Timeline:
    // 0 - 400ms: Dark bg, particles appear
    // 400 - 1000ms: Core fades in & scales 0.85x -> 1x
    // 1000 - 1600ms: Scanning rings rotate, circuit lines illuminate
    // 1600 - 2200ms: "CodeSherlock AI" fades in
    // 2200 - 2600ms: "Autonomous AI Software Engineer" fades in
    // 2600 - 3500ms: "ANALYZING YOUR CODE..." & progress bar 0% -> 100%
    // 3500ms+: Fade transition out & invoke onFinished
    LaunchedEffect(Unit) {
        // Phase 1: 0 - 400 ms
        particlesAlpha.animateTo(1f, animationSpec = tween(400))

        // Phase 2: 400 - 1000 ms
        coreAlpha.animateTo(1f, animationSpec = tween(600))
        coreScale.animateTo(1.0f, animationSpec = tween(600, easing = FastOutSlowInEasing))

        // Phase 3: 1000 - 1600 ms
        circuitAlpha.animateTo(1f, animationSpec = tween(600))

        // Phase 4: 1600 - 2200 ms
        titleAlpha.animateTo(1f, animationSpec = tween(600))

        // Phase 5: 2200 - 2600 ms
        subtitleAlpha.animateTo(1f, animationSpec = tween(400))

        // Phase 6: 2600 - 3500 ms
        loadingAlpha.animateTo(1f, animationSpec = tween(300))
        progressAnim.animateTo(1f, animationSpec = tween(900, easing = FastOutSlowInEasing))

        // Final Transition: 3500 ms+
        delay(200)
        screenAlpha.animateTo(0f, animationSpec = tween(300))
        onFinished()
    }

    // Color definitions
    val darkNavyBg = Color(0xFF070A10)
    val primaryCyan = Color(0xFF00E5FF)
    val accentViolet = Color(0xFFA855F7)
    val accentBlue = Color(0xFF3B82F6)
    val textPrimary = Color(0xFFF8FAFC)
    val textSecondary = Color(0xFF94A3B8)
    val textMuted = Color(0xFF64748B)

    // Particles configuration (7 particles floating around core)
    val particles = remember {
        listOf(
            SplashParticle(0f, 75f, 1.0f, 2.5f, 0.9f, primaryCyan),
            SplashParticle(55f, 90f, 0.8f, 3.0f, 0.8f, accentViolet),
            SplashParticle(120f, 105f, 1.2f, 2.0f, 0.7f, primaryCyan),
            SplashParticle(175f, 82f, -0.9f, 2.8f, 0.85f, accentBlue),
            SplashParticle(230f, 115f, 0.7f, 3.2f, 0.75f, primaryCyan),
            SplashParticle(290f, 95f, -1.1f, 2.2f, 0.8f, accentViolet),
            SplashParticle(330f, 125f, 0.6f, 2.6f, 0.65f, accentBlue)
        )
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(screenAlpha.value)
            .background(darkNavyBg),
        contentAlignment = Alignment.Center
    ) {
        // Background Ambient Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height / 2f - size.height * 0.08f)
            
            // Ambient Radial Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryCyan.copy(alpha = 0.12f * coreAlpha.value),
                        accentViolet.copy(alpha = 0.08f * coreAlpha.value),
                        Color.Transparent
                    ),
                    center = centerOffset,
                    radius = size.width * 0.65f
                ),
                radius = size.width * 0.65f,
                center = centerOffset
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ==========================================
            // CENTER VISUAL: AI Investigation Core
            // ==========================================
            Box(
                modifier = Modifier
                    .size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val effectiveScale = coreScale.value * pulseScale

                    // 1. Draw Circuit Lines (Extending outward)
                    if (circuitAlpha.value > 0f) {
                        drawCircuitBoardLines(
                            center = center,
                            alpha = circuitAlpha.value,
                            primaryColor = primaryCyan,
                            accentColor = accentViolet,
                            density = density
                        )
                    }

                    // 2. Draw Scanning Rings
                    if (coreAlpha.value > 0f) {
                        // Outer Dash Ring (Rotating Clockwise)
                        rotate(ringRotation1, pivot = center) {
                            drawCircle(
                                color = primaryCyan.copy(alpha = 0.35f * coreAlpha.value),
                                radius = 100.dp.toPx() * effectiveScale,
                                center = center,
                                style = Stroke(
                                    width = 1.5.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f, 40f, 15f), 0f)
                                )
                            )
                        }

                        // Inner Segment Ring (Rotating Counter-Clockwise)
                        rotate(ringRotation2, pivot = center) {
                            drawCircle(
                                color = accentViolet.copy(alpha = 0.4f * coreAlpha.value),
                                radius = 78.dp.toPx() * effectiveScale,
                                center = center,
                                style = Stroke(
                                    width = 1.8.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(50f, 25f, 10f, 25f), 0f)
                                )
                            )
                        }

                        // Static Precision Ring
                        drawCircle(
                            color = accentBlue.copy(alpha = 0.25f * coreAlpha.value),
                            radius = 60.dp.toPx() * effectiveScale,
                            center = center,
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }

                    // 3. Draw Orbiting Particles
                    if (particlesAlpha.value > 0f) {
                        particles.forEach { particle ->
                            val currentAngleRad = Math.toRadians(
                                (particle.baseAngleDeg + particleOrbitProgress * particle.speedMultiplier).toDouble()
                            )
                            val radiusPx = particle.orbitRadiusDp.dp.toPx() * effectiveScale
                            val particleX = center.x + (radiusPx * cos(currentAngleRad)).toFloat()
                            val particleY = center.y + (radiusPx * sin(currentAngleRad)).toFloat()

                            // Particle Glow
                            drawCircle(
                                color = particle.color.copy(alpha = 0.3f * particlesAlpha.value),
                                radius = particle.sizeDp.dp.toPx() * 2.2f,
                                center = Offset(particleX, particleY)
                            )
                            // Particle Core
                            drawCircle(
                                color = particle.color.copy(alpha = particle.alpha * particlesAlpha.value),
                                radius = particle.sizeDp.dp.toPx(),
                                center = Offset(particleX, particleY)
                            )
                        }
                    }

                    // 4. Central AI Core Node
                    if (coreAlpha.value > 0f) {
                        val nodeRadius = 32.dp.toPx() * effectiveScale

                        // Core Radial Glow
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryCyan.copy(alpha = 0.8f * coreAlpha.value),
                                    accentViolet.copy(alpha = 0.4f * coreAlpha.value),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = nodeRadius * 1.8f
                            ),
                            radius = nodeRadius * 1.8f,
                            center = center
                        )

                        // Central Inner Circle Node
                        drawCircle(
                            brush = Brush.linearGradient(
                                colors = listOf(primaryCyan, accentViolet),
                                start = Offset(center.x - nodeRadius, center.y - nodeRadius),
                                end = Offset(center.x + nodeRadius, center.y + nodeRadius)
                            ),
                            radius = nodeRadius,
                            center = center,
                            alpha = coreAlpha.value
                        )

                        // Center Diamond/Search Accent inside Core
                        val innerDiamondSize = 10.dp.toPx() * effectiveScale
                        val path = Path().apply {
                            moveTo(center.x, center.y - innerDiamondSize)
                            lineTo(center.x + innerDiamondSize, center.y)
                            lineTo(center.x, center.y + innerDiamondSize)
                            lineTo(center.x - innerDiamondSize, center.y)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = darkNavyBg,
                            alpha = coreAlpha.value
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // TEXT: Title & Subtitle
            // ==========================================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Name Title
                Text(
                    text = "CodeSherlock AI",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.alpha(titleAlpha.value)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Tagline Subtitle
                Text(
                    text = "Autonomous AI Software Engineer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textSecondary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.alpha(subtitleAlpha.value)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ==========================================
            // LOADING AREA: Status Text & Progress Bar
            // ==========================================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
                    .alpha(loadingAlpha.value)
            ) {
                Text(
                    text = "ANALYZING YOUR CODE...",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryCyan,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Thin Animated Progress Bar
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(textMuted.copy(alpha = 0.2f))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val progressWidth = size.width * progressAnim.value
                        if (progressWidth > 0f) {
                            drawRoundRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(primaryCyan, accentViolet)
                                ),
                                size = Size(progressWidth, size.height),
                                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                            )
                        }
                    }
                }
            }
        }
    }
}

// Helper to draw subtle circuit board lines extending from center
private fun DrawScope.drawCircuitBoardLines(
    center: Offset,
    alpha: Float,
    primaryColor: Color,
    accentColor: Color,
    density: androidx.compose.ui.unit.Density
) {
    val angles = floatArrayOf(25f, 65f, 115f, 155f, 205f, 245f, 295f, 335f)
    val innerRadius = with(density) { 62.dp.toPx() }
    val outerRadius = with(density) { 118.dp.toPx() }

    angles.forEachIndexed { index, angleDeg ->
        val rad = Math.toRadians(angleDeg.toDouble())
        val startX = center.x + (innerRadius * cos(rad)).toFloat()
        val startY = center.y + (innerRadius * sin(rad)).toFloat()

        val midX = center.x + ((innerRadius + 22.dp.toPx()) * cos(rad)).toFloat()
        val midY = center.y + ((innerRadius + 22.dp.toPx()) * sin(rad)).toFloat()

        val endX = center.x + (outerRadius * cos(rad)).toFloat()
        val endY = midY // 90-degree orthogonal bend

        val strokeColor = if (index % 2 == 0) primaryColor else accentColor

        val path = Path().apply {
            moveTo(startX, startY)
            lineTo(midX, midY)
            lineTo(endX, endY)
        }

        drawPath(
            path = path,
            color = strokeColor.copy(alpha = 0.25f * alpha),
            style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Terminal Dot Node
        drawCircle(
            color = strokeColor.copy(alpha = 0.5f * alpha),
            radius = 2.dp.toPx(),
            center = Offset(endX, endY)
        )
    }
}

@Composable
fun SplashScreen(
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    CodeSherlockSplash(
        onFinished = onNavigateToDashboard,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CodeSherlockSplashPreview() {
    CodeSherlockAITheme {
        CodeSherlockSplash(onFinished = {})
    }
}
