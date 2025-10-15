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

import android.net.Uri
import androidx.core.net.toUri
import com.google.jetstream.data.models.MoviesResponseItem

data class Movie(
    val id: String,
    val sources: Map<StereoscopicVisionType, Uri>,
    val subtitleUri: String?,
    val posterUri: String,
    val name: String,
    val description: String
) {
    fun videoUriFor(
        stereoscopicVisionType: StereoscopicVisionType = StereoscopicVisionType.Mono
    ): Uri? {
        return sources[stereoscopicVisionType]
    }

    fun supportingStereoModes(): List<StereoscopicVisionType> {
        return sources.keys.toList() - StereoscopicVisionType.Mono
    }

    val videoUri: Uri
        get() {
            return videoUriFor() ?: sources.values.first()
        }

    companion object {
        fun from(movieDetails: MovieDetails): Movie {
            return Movie(
                id = movieDetails.id,
                sources = movieDetails.sources,
                subtitleUri = movieDetails.subtitleUri,
                posterUri = movieDetails.posterUri,
                name = movieDetails.name,
                description = movieDetails.description
            )
        }
    }
}

fun MoviesResponseItem.toMovie(thumbnailType: ThumbnailType = ThumbnailType.Standard): Movie {
    val thumbnail = when (thumbnailType) {
        ThumbnailType.Standard -> image_2_3
        ThumbnailType.Long -> image_16_9
    }

    val sourceMap = sources.associate {
        StereoscopicVisionType.from(it.stereoMode) to it.uri.toUri()
    }

    return Movie(
        id = id,
        sources = sourceMap,
        subtitleUri,
        thumbnail,
        title,
        fullTitle
    )
}

enum class ThumbnailType {
    Standard,
    Long
}
