package com.example.gridsample

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GridSampleTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent { GridSample() }
  }

  @Test
  fun gridSample_rendersCorrectly() {
    composeTestRule.onNodeWithText("Grid sample").assertExists()
    composeTestRule.onNodeWithText("Display Grid layout variations on this device.").assertExists()
  }
}
