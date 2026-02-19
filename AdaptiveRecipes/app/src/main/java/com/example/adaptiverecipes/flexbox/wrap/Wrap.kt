@file:OptIn(ExperimentalFlexBoxApi::class)

package com.example.adaptiverecipes.flexbox.wrap

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.PinkRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

/**
 * A FlexBox that wraps with variable width items that grow equally to fill the available space
 */
@Preview(widthDp = 1200)
@Preview(widthDp = 840)
@Preview(widthDp = 600)
@Composable
fun WrapWithGrow() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
        }
    ) {
        RedRoundedBox(modifier = Modifier.width(150.dp).flex { grow = 1.0f })
        BlueRoundedBox(modifier = Modifier.width(250.dp).flex { grow = 1.0f })
        GreenRoundedBox(modifier = Modifier.width(350.dp).flex { grow = 1.0f })
        OrangeRoundedBox(modifier = Modifier.width(200.dp).flex { grow = 1.0f })
        PinkRoundedBox(modifier = Modifier.width(200.dp).flex { grow = 1.0f })
    }
}
