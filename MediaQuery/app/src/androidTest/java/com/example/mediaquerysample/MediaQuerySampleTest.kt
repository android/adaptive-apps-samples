package com.example.mediaquerysample

import androidx.activity.ComponentActivity
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeUiApi::class)
class MediaQuerySampleTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    ComposeUiFlags.isMediaQueryIntegrationEnabled = true
    composeTestRule.setContent { MediaQuerySample() }
  }

  @Test
  fun mediaQuerySample_rendersSuccessfully() {
    composeTestRule.onNodeWithText("MediaQuery sample").assertExists()
    composeTestRule.onNodeWithText("Window info and orientation").assertExists()
    composeTestRule.onNodeWithText("Keyboard kind").assertExists()
    composeTestRule.onNodeWithText("Touch target sizing").assertExists()
    composeTestRule.onNodeWithText("Hardware sensors").assertExists()
    composeTestRule.onNodeWithText("Viewing distance").assertExists()
  }
}
