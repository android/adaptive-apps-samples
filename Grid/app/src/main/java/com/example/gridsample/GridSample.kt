package com.example.gridsample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gridsample.ui.theme.MyApplicationTheme

@OptIn(ExperimentalGridApi::class)
@Composable
fun GridSample() {
    val colors = listOf(
        Color(0xFF4285F4), // Google Blue
        Color(0xFFEA4335), // Google Red
        Color(0xFFFBBC05), // Google Yellow
        Color(0xFF34A853), // Google Green
        Color(0xFF000000)  // Black
    )

    Column(modifier = Modifier.safeDrawingPadding().padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
        Text(
            text = "Grid sample",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Display Grid layout variations on this device.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Grid(
            config = {
                repeat(3) { column(0.333f) }
                row(112.dp)
                row(88.dp)
                row(100.dp)
                gap(8.dp)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .gridItem(rowSpan = 2)
                    .background(colors[0], shape = RoundedCornerShape(16.dp))
                    .fillMaxSize()
            ) {
                Text(
                    text = "1",
                    color = if (colors[0].luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 40.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(colors[1], shape = RoundedCornerShape(16.dp))
                    .fillMaxSize()
            ) {
                Text(
                    text = "2",
                    color = if (colors[1].luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 40.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(colors[2], shape = RoundedCornerShape(16.dp))
                    .fillMaxSize()
            ) {
                Text(
                    text = "3",
                    color = if (colors[2].luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 40.sp
                )
            }

            Box(
                modifier = Modifier
                    .gridItem(columnSpan = 2)
                    .background(colors[3], shape = RoundedCornerShape(16.dp))
                    .fillMaxSize()
            ) {
                Text(
                    text = "4",
                    color = if (colors[3].luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 40.sp
                )
            }

            Box(
                modifier = Modifier
                    .gridItem(columnSpan = 3)
                    .background(colors[4], shape = RoundedCornerShape(16.dp))
                    .fillMaxSize()
            ) {
                Text(
                    text = "5",
                    color = if (colors[4].luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 40.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridSamplePreview() {
    MyApplicationTheme {
        GridSample()
    }
}
