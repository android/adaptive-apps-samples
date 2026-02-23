package com.example.adaptiverecipes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.example.adaptiverecipes.flexbox.containerconfig.AlignContentCenter
import com.example.adaptiverecipes.flexbox.containerconfig.AlignContentEnd
import com.example.adaptiverecipes.flexbox.containerconfig.AlignContentSpaceAround
import com.example.adaptiverecipes.flexbox.containerconfig.AlignContentSpaceBetween
import com.example.adaptiverecipes.flexbox.containerconfig.AlignContentStart
import com.example.adaptiverecipes.flexbox.containerconfig.AlignContentStretch
import com.example.adaptiverecipes.flexbox.containerconfig.AlignItemsBaseline
import com.example.adaptiverecipes.flexbox.containerconfig.AlignItemsCenter
import com.example.adaptiverecipes.flexbox.containerconfig.AlignItemsEnd
import com.example.adaptiverecipes.flexbox.containerconfig.AlignItemsStart
import com.example.adaptiverecipes.flexbox.containerconfig.AlignItemsStretch
import com.example.adaptiverecipes.flexbox.containerconfig.ColumnGap
import com.example.adaptiverecipes.flexbox.containerconfig.CombinedGap
import com.example.adaptiverecipes.flexbox.containerconfig.DirectionColumn
import com.example.adaptiverecipes.flexbox.containerconfig.DirectionColumnReverse
import com.example.adaptiverecipes.flexbox.containerconfig.DirectionRow
import com.example.adaptiverecipes.flexbox.containerconfig.DirectionRowReverse
import com.example.adaptiverecipes.flexbox.containerconfig.JustifyContentCenter
import com.example.adaptiverecipes.flexbox.containerconfig.JustifyContentEnd
import com.example.adaptiverecipes.flexbox.containerconfig.JustifyContentSpaceAround
import com.example.adaptiverecipes.flexbox.containerconfig.JustifyContentSpaceBetween
import com.example.adaptiverecipes.flexbox.containerconfig.JustifyContentSpaceEvenly
import com.example.adaptiverecipes.flexbox.containerconfig.JustifyContentStart
import com.example.adaptiverecipes.flexbox.containerconfig.NoWrap
import com.example.adaptiverecipes.flexbox.containerconfig.RowGap
import com.example.adaptiverecipes.flexbox.containerconfig.Wrap
import com.example.adaptiverecipes.flexbox.containerconfig.WrapReverse
import com.example.adaptiverecipes.flexbox.itemconfig.AlignSelfOverride
import com.example.adaptiverecipes.flexbox.itemconfig.BasisAuto
import com.example.adaptiverecipes.flexbox.itemconfig.BasisDp
import com.example.adaptiverecipes.flexbox.itemconfig.BasisPercent
import com.example.adaptiverecipes.flexbox.itemconfig.FlexGrowMultiple
import com.example.adaptiverecipes.flexbox.itemconfig.FlexGrowSingle
import com.example.adaptiverecipes.flexbox.itemconfig.FlexShrink
import com.example.adaptiverecipes.flexbox.basic.WrapWithGrow


@Preview(showBackground = true, backgroundColor = 0xFF777777)
annotation class PreviewWithBackground

class FlexBoxScreenshotTest {

    @PreviewTest
    @Preview(widthDp = 1200, showBackground = true, backgroundColor = 0xFF777777)
    @Preview(widthDp = 840, showBackground = true, backgroundColor = 0xFF777777)
    @Preview(widthDp = 600, showBackground = true, backgroundColor = 0xFF777777)
    @Composable
    fun WrapWithGrowScreenshot() = WrapWithGrow()

    @PreviewTest
    @PreviewWithBackground
    @Composable
    fun DirectionRowScreenshot() = DirectionRow()

    @PreviewTest
    @PreviewWithBackground
    @Composable
    fun DirectionRowReverseScreenshot() = DirectionRowReverse()

    @PreviewTest
    @PreviewWithBackground
    @Composable
    fun DirectionColumnScreenshot() = DirectionColumn()

    @PreviewTest
    @PreviewWithBackground
    @Composable
    fun DirectionColumnReverseScreenshot() = DirectionColumnReverse()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 350, backgroundColor = 0xFF777777)
    @Composable
    fun NoWrapScreenshot() = NoWrap()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 350, backgroundColor = 0xFF777777)
    @Composable
    fun WrapScreenshot() = Wrap()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 350, backgroundColor = 0xFF777777)
    @Composable
    fun WrapReverseScreenshot() = WrapReverse()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun JustifyContentStartScreenshot() = JustifyContentStart()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun JustifyContentEndScreenshot() = JustifyContentEnd()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun JustifyContentCenterScreenshot() = JustifyContentCenter()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun JustifyContentSpaceBetweenScreenshot() = JustifyContentSpaceBetween()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun JustifyContentSpaceAroundScreenshot() = JustifyContentSpaceAround()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun JustifyContentSpaceEvenlyScreenshot() = JustifyContentSpaceEvenly()

    @PreviewTest
    @Preview(showBackground = true, heightDp = 200, backgroundColor = 0xFF777777)
    @Composable
    fun AlignItemsStartScreenshot() = AlignItemsStart()

    @PreviewTest
    @Preview(showBackground = true, heightDp = 200, backgroundColor = 0xFF777777)
    @Composable
    fun AlignItemsEndScreenshot() = AlignItemsEnd()

    @PreviewTest
    @Preview(showBackground = true, heightDp = 200, backgroundColor = 0xFF777777)
    @Composable
    fun AlignItemsCenterScreenshot() = AlignItemsCenter()

    @PreviewTest
    @Preview(showBackground = true, heightDp = 200, backgroundColor = 0xFF777777)
    @Composable
    fun AlignItemsStretchScreenshot() = AlignItemsStretch()

    @PreviewTest
    @Preview(showBackground = true, heightDp = 200, backgroundColor = 0xFF777777)
    @Composable
    fun AlignItemsBaselineScreenshot() = AlignItemsBaseline()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
    @Composable
    fun AlignContentStartScreenshot() = AlignContentStart()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
    @Composable
    fun AlignContentEndScreenshot() = AlignContentEnd()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
    @Composable
    fun AlignContentCenterScreenshot() = AlignContentCenter()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
    @Composable
    fun AlignContentStretchScreenshot() = AlignContentStretch()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
    @Composable
    fun AlignContentSpaceBetweenScreenshot() = AlignContentSpaceBetween()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
    @Composable
    fun AlignContentSpaceAroundScreenshot() = AlignContentSpaceAround()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, backgroundColor = 0xFF777777)
    @Composable
    fun ColumnGapScreenshot() = ColumnGap()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, backgroundColor = 0xFF777777)
    @Composable
    fun RowGapScreenshot() = RowGap()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 300, backgroundColor = 0xFF777777)
    @Composable
    fun CombinedGapScreenshot() = CombinedGap()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun FlexGrowSingleScreenshot() = FlexGrowSingle()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun FlexGrowMultipleScreenshot() = FlexGrowMultiple()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 500, backgroundColor = 0xFF777777)
    @Preview(showBackground = true, widthDp = 400, backgroundColor = 0xFF777777)
    @Preview(showBackground = true, widthDp = 300, backgroundColor = 0xFF777777)
    @Composable
    fun FlexShrinkScreenshot() = FlexShrink()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun BasisAutoScreenshot() = BasisAuto()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun BasisDpScreenshot() = BasisDp()

    @PreviewTest
    @Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
    @Composable
    fun BasisPercentScreenshot() = BasisPercent()

    @PreviewTest
    @Preview(showBackground = true, heightDp = 300, backgroundColor = 0xFF777777)
    @Composable
    fun AlignSelfOverrideScreenshot() = AlignSelfOverride()
}
