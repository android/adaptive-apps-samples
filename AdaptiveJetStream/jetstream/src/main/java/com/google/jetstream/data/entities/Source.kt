package com.google.jetstream.data.entities

import com.google.jetstream.data.convert.From
import com.google.jetstream.data.models.SourceDescription

data class Source(
    val url: String,
    val stereoMode: StereoMode,
) {

    companion object : From<SourceDescription, Source> {
        override fun from(value: SourceDescription): Source {
            return Source(
                url = value.uri,
                stereoMode = StereoMode.from(value.stereoMode)
            )
        }
    }
}

sealed interface StereoMode {
    data object Mono : StereoMode
    data object MultiViewLeftPrimary : StereoMode
    data object MultiViewRightPrimary : StereoMode
    data object SideBySide : StereoMode
    data object TopBottom : StereoMode

    companion object : From<String, StereoMode> {
        override fun from(value: String): StereoMode {
            return when (value) {
                "multiview_left_primary" -> MultiViewLeftPrimary
                "multiview_right_primary" -> MultiViewRightPrimary
                "side_by_side" -> SideBySide
                "top_bottom" -> TopBottom
                else -> Mono
            }
        }

    }
}