package com.google.sample.wsc.lxl.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.sample.wsc.lxl.utils.Height
import com.google.sample.wsc.lxl.utils.Width
import com.google.sample.wsc.lxl.utils.calculateColumns
import com.google.sample.wsc.lxl.utils.calculateColumnsForTitles
import com.google.sample.wsc.lxl.utils.material
import com.google.sample.wsc.lxl.utils.windowManager

@PreviewScreenSizes
@Composable
fun Content(modifier: Modifier = Modifier) {

    val wm = LocalActivity.current?.windowManager() ?: return
    val material = material()
    val numberOfColumns = calculateColumns(wm.first)
    val numberOfColumnsForTitles = calculateColumnsForTitles(wm.first)

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfColumns), modifier = modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(numberOfColumnsForTitles) }) {
            Title()
        }
        item(span = { GridItemSpan(numberOfColumnsForTitles) }) {
            Subtitle("WindowManager")
        }
        item {
            WidthComposable(width = wm.first)
        }
        item {
            HeightComposable(height = wm.second)
        }

        if (numberOfColumns < 3) {
            item(span = { GridItemSpan(numberOfColumnsForTitles) }) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.onPrimaryFixedVariant
                )
            }
        }

        item(span = { GridItemSpan(numberOfColumnsForTitles) }) {
            Subtitle("Material")
        }
        item {
            WidthComposable(material.first)
        }
        item {
            HeightComposable(material.second)
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "WindowSizeClass Sample",
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(vertical = 16.dp),
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Subtitle(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(vertical = 16.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WidthComposable(width: Width) {
    Column {
        DimensionTitle(title = "Width")
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (w in Width.entries) {
                Indicator(label = w.label, isSelected = w == width)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun DimensionTitle(title: String) {
    Text(
        text = title, modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        style = MaterialTheme.typography.bodyMediumEmphasized,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun RowScope.Indicator(label: String, isSelected: Boolean) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    val borderWidth = if (isSelected) {
        2.dp
    } else {
        0.dp
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .weight(1f)
            .height(200.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.medium)
            .border(
                width = borderWidth,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = label,
            color = textColor,
            modifier = Modifier.padding(all = 4.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun HeightComposable(height: Height) {
    Column {
        DimensionTitle(title = "Height")
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (h in Height.entries) {
                Indicator(label = h.label, isSelected = h == height)
            }
        }
    }
}
