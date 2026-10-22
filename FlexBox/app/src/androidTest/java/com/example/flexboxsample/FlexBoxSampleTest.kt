package com.example.flexboxsample

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** UI tests for [FlexBoxSample]. */
class FlexBoxSampleTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent { FlexBoxSample() }
  }

  @Test
  fun flexboxExamples_exist() {
    composeTestRule.onNodeWithText("FlexBox sample").assertExists()
    composeTestRule.onNodeWithText("Example 0: Column direction (center)").assertExists()
  }
}
