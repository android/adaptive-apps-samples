/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.profile

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.google.jetstream.presentation.components.AdaptivePreview
import com.google.jetstream.presentation.components.JetStreamPreview
import com.google.jetstream.presentation.screens.profile.compoents.AboutSection
import com.google.jetstream.presentation.screens.profile.compoents.AccountsSection
import com.google.jetstream.presentation.screens.profile.compoents.HelpAndSupportSection
import com.google.jetstream.presentation.screens.profile.compoents.LanguageSection
import com.google.jetstream.presentation.screens.profile.compoents.SearchHistorySection
import com.google.jetstream.presentation.screens.profile.compoents.SubtitlesSection

@PreviewTest
@AdaptivePreview
@Composable
fun AboutSectionScreenshot() {
    JetStreamPreview {
        Surface {
            AboutSection()
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun AccountsSectionScreenshot() {
    JetStreamPreview {
        Surface {
            AccountsSection()
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun SubtitlesSectionScreenshot() {
    JetStreamPreview {
        Surface {
            SubtitlesSection(isSubtitlesChecked = true, onSubtitleCheckChange = {})
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun LanguageSectionScreenshot() {
    JetStreamPreview {
        Surface {
            LanguageSection(selectedIndex = 0, onSelectedIndexChange = {})
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun SearchHistorySectionScreenshot() {
    JetStreamPreview {
        Surface {
            SearchHistorySection()
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun HelpAndSupportSectionScreenshot() {
    JetStreamPreview {
        Surface {
            HelpAndSupportSection()
        }
    }
}
