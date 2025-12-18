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

package com.google.jetstream.data.repositories

import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.util.FakeAssetReader
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TvDataSourceTest {

    private val fakeAssetReader = FakeAssetReader()
    private val tvDataSource = TvDataSource(fakeAssetReader)

    @Test
    fun getTvShowList_returnsItems() = runTest {
        val item = """
            {
              "id": "1",
              "sources": [],
              "subtitleUri": "",
              "rank": 1,
              "rankUpDown": "",
              "title": "Show 1",
              "fullTitle": "Show 1",
              "year": 2021,
              "releaseDate": "",
              "image_16_9": "",
              "image_2_3": "",
              "runtimeMins": 120,
              "runtimeStr": "",
              "plot": "",
              "contentRating": "",
              "rating": 8.0,
              "ratingCount": 1000,
              "metaCriticRating": 80,
              "genres": "",
              "directors": "",
              "stars": ""
            }
        """.trimIndent()
        // We need at least 5 items for subList(0, 5) to work
        val json = "[$item, $item, $item, $item, $item]"
        fakeAssetReader.setResponse(StringConstants.Assets.MostPopularTVShows, json)

        val shows = tvDataSource.getTvShowList()
        assertEquals(5, shows.size)
        assertEquals("Show 1", shows[0].name)
    }
}
