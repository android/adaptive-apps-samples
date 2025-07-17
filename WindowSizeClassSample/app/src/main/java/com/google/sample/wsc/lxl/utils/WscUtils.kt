@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.google.sample.wsc.lxl.utils

import android.app.Activity
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.BREAKPOINTS_V2
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import androidx.window.layout.WindowMetricsCalculator
import androidx.window.layout.adapter.computeWindowSizeClass

enum class Width(val label: String) {
    COMPACT("Compact"),
    MEDIUM("Medium"),
    EXPANDED("Expanded"),
    LARGE("L"),
    EXTRALARGE("XL")
}

enum class Height(val label: String) {
    COMPACT("Compact"),
    MEDIUM("Medium"),
    EXPANDED("Expanded")
}

typealias WindowSize = Pair<Width, Height>

fun Activity.windowManager(): WindowSize {

    val wmc = WindowMetricsCalculator.getOrCreate()
    val metrics = wmc.computeCurrentWindowMetrics(this)
    val wsc = BREAKPOINTS_V2.computeWindowSizeClass(metrics)

    return wsc.width() to wsc.height()
}

private fun WindowSizeClass.width(): Width =
    when {
        isWidthAtLeastBreakpoint(WIDTH_DP_EXTRA_LARGE_LOWER_BOUND) -> Width.EXTRALARGE
        isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND) -> Width.LARGE
        isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> Width.EXPANDED
        isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> Width.MEDIUM
        else -> Width.COMPACT
    }

private fun WindowSizeClass.height(): Height =
    when {
        isHeightAtLeastBreakpoint(HEIGHT_DP_EXPANDED_LOWER_BOUND) -> Height.EXPANDED
        isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND) -> Height.MEDIUM
        else -> Height.COMPACT
    }

@Composable
fun material(): WindowSize {
    val wsc = currentWindowAdaptiveInfo().windowSizeClass
    return wsc.width() to wsc.height()
}

fun calculateColumns(width: Width): Int =
    when (width) {
        Width.EXTRALARGE, Width.LARGE -> 4
        Width.EXPANDED, Width.MEDIUM -> 2
        Width.COMPACT -> 1
    }

fun calculateColumnsForTitles(width: Width): Int = when (width) {
    Width.COMPACT -> 1
    else -> 2
}
