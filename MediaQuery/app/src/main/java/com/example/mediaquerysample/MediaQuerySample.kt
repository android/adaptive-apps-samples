@file:OptIn(
    androidx.compose.ui.ExperimentalMediaQueryApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
package com.example.mediaquerysample

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiMediaScope
import androidx.compose.ui.UiMediaScope.KeyboardKind
import androidx.compose.ui.UiMediaScope.PointerPrecision
import androidx.compose.ui.UiMediaScope.Posture
import androidx.compose.ui.UiMediaScope.ViewingDistance
import androidx.compose.ui.derivedMediaQuery
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.mediaQuery
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Standard nominal breakpoints for Window Width Size Classes
private val BreakpointMediumWidth = 600.dp
private val BreakpointExpandedWidth = 840.dp

@Composable
fun MediaQuerySample() {
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "MediaQuery sample",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Display MediaQuery values of this device.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        WindowInfoCard()

        Spacer(modifier = Modifier.height(16.dp))

        // --- Main Layout Adaptation ---
        val isAtLeastMediumWindowWidth by derivedMediaQuery { windowWidth >= BreakpointMediumWidth }

        val leftColumnContent = @Composable {
            PostureCard()
            Spacer(modifier = Modifier.height(16.dp))
            KeyboardKindCard()
            Spacer(modifier = Modifier.height(16.dp))
            TouchTargetSizeCard()
        }

        val rightColumnContent = @Composable {
            SensorsCard()
            Spacer(modifier = Modifier.height(16.dp))
            ViewingDistanceCard()
        }

        if (isAtLeastMediumWindowWidth) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    leftColumnContent()
                }
                Column(modifier = Modifier.weight(1f)) {
                    rightColumnContent()
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                leftColumnContent()
                Spacer(modifier = Modifier.height(16.dp))
                rightColumnContent()
            }
        }
    }
}



@Composable
fun PostureCard() {
    val isTabletop = mediaQuery { windowPosture == Posture.Tabletop }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isTabletop) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Device state/posture",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isTabletop) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = if (isTabletop) Color(0xFFFFD54F) else Color(0xFF90CAF9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = if (isTabletop) Color(0xFFFFB300) else Color(0xFF42A5F5),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isTabletop) "Tabletop\n(Folded 90°)" else "Flat / Full",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isTabletop) "Layout adapted for hands-free tabletop view." else "Standard full view layout.",
                fontSize = 11.sp,
                color = if (isTabletop) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun KeyboardKindCard() {
    val keyboardKind = mediaQuery { keyboardKind }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Keyboard kind",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Keyboard alert
            if (keyboardKind == KeyboardKind.None) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "No Keyboard detected. Connect physical key device for full accessibility.",
                        fontSize = 11.sp,
                        color = Color.Red
                    )
                }
            } else {
                Column {
                    Text(
                        text = "Keyboard detected: ${keyboardKind.toString()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Note: Emulators automatically detect your host PC's keyboard.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun TouchTargetSizeCard() {
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val (targetHeight, targetColor) = mediaQuery {
        val isLowPrecision = pointerPrecision == PointerPrecision.Blunt || pointerPrecision == PointerPrecision.None
        if (isLowPrecision) {
            64.dp to Color(0xFFFFAB91)
        } else {
            44.dp to primaryContainerColor
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Touch target sizing",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(targetHeight)
                    .background(targetColor, RoundedCornerShape(12.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val textLuminance = targetColor.luminance()
                val textColor = if (textLuminance > 0.5f) Color.Black else Color.White
                val isLowPrecision = targetHeight == 64.dp
                Text(
                    text = if (isLowPrecision) "Enlarged Touch Target (Low Precision)" else "Standard Clickable Target",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Target size scales based on cursor/pointer device precision.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SensorsCard() {
    val hasCamera = mediaQuery { hasCamera }
    val hasMicrophone = mediaQuery { hasMicrophone }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hardware sensors",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Hardware Sensors
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SensorIndicator(label = "Camera", isSupported = hasCamera)
                SensorIndicator(label = "Microphone", isSupported = hasMicrophone)
            }
        }
    }
}

@Composable
fun ViewingDistanceCard() {
    val viewingDistance = mediaQuery { viewingDistance }
    val isFarDistance = viewingDistance == ViewingDistance.Far || viewingDistance == ViewingDistance.Medium

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Viewing distance",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Viewing Distance text size adaptation
            val fontSize = if (isFarDistance) 22.sp else 14.sp
            val fontColor = if (isFarDistance) Color.Red else MaterialTheme.colorScheme.onSurface

            Text(
                text = "Adaptive viewing scale",
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = fontColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Estimated range: ${viewingDistance.toString()}. Font scaling matches optimal readability parameters.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun SensorIndicator(label: String, isSupported: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (isSupported) Color(0xFFE8F5E9) else Color(0xFFECEFF1),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(if (isSupported) Color.Green else Color.Gray, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSupported) Color(0xFF2E7D32) else Color.DarkGray
        )
    }
}

@Composable
fun WindowInfoCard() {
    val windowWidth by derivedMediaQuery { windowWidth }
    val windowHeight by derivedMediaQuery { windowHeight }
    val isLandscape = windowWidth > windowHeight

    val widthSizeClass = when {
        windowWidth < BreakpointMediumWidth -> "Compact"
        windowWidth < BreakpointExpandedWidth -> "Medium"
        else -> "Expanded"
    }
    val heightSizeClass = when {
        windowHeight < 480.dp -> "Compact"
        windowHeight < 900.dp -> "Medium"
        else -> "Expanded"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Window info and orientation",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Width Class: $widthSizeClass", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(text = "Height Class: $heightSizeClass", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Text(
                        text = "Viewport: $windowWidth x $windowHeight",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                // Orientation Visual
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(width = if (isLandscape) 50.dp else 30.dp, height = if (isLandscape) 30.dp else 50.dp)
                            .background(Color(0xFFECEFF1), RoundedCornerShape(4.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = if (isLandscape) 0.dp else 4.dp, end = if (isLandscape) 4.dp else 0.dp)
                                .size(4.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .align(if (isLandscape) Alignment.CenterEnd else Alignment.BottomCenter)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isLandscape) "Landscape" else "Portrait",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
