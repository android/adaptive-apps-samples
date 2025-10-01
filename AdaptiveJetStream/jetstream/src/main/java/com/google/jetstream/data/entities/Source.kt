package com.google.jetstream.data.entities

import com.google.jetstream.data.convert.From
import com.google.jetstream.data.models.SourceDescription

data class Source(
    val url: String,
    val stereoscopicVisionType: StereoscopicVisionType,
) {

    companion object : From<SourceDescription, Source> {
        override fun from(value: SourceDescription): Source {
            return Source(
                url = value.uri,
                stereoscopicVisionType = StereoscopicVisionType.from(value.stereoMode)
            )
        }
    }
}