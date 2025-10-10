/*
 * Copyright 2023 Google LLC
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

package com.google.jetstream.data.entities

import com.google.jetstream.data.convert.From
import com.google.jetstream.data.convert.Into
import androidx.xr.compose.subspace.StereoMode

sealed interface StereoscopicVisionType: Into<StereoMode> {
    data object Mono: StereoscopicVisionType {
        override fun into(): StereoMode {
            return StereoMode.Mono
        }
    }
    data object MultiViewLeftPrimary: StereoscopicVisionType {
        override fun into(): StereoMode {
            return StereoMode.MultiviewLeftPrimary
        }

    }
    data object MultiViewRightPrimary: StereoscopicVisionType {
        override fun into(): StereoMode {
            return StereoMode.MultiviewRightPrimary
        }
    }
    data object SideBySide: StereoscopicVisionType {
        override fun into(): StereoMode {
            return StereoMode.SideBySide
        }
    }
    data object TopBottom: StereoscopicVisionType {
        override fun into(): StereoMode {
            return StereoMode.TopBottom
        }
    }

    companion object: From<String, StereoscopicVisionType> {
        override fun from(value: String): StereoscopicVisionType {
            return when(value) {
                "multiview_left_primary" -> MultiViewLeftPrimary
                "multiview_right_primary" -> MultiViewRightPrimary
                "side_by_side" -> SideBySide
                "top_bottom" -> TopBottom
                else -> Mono
            }
        }

    }
}