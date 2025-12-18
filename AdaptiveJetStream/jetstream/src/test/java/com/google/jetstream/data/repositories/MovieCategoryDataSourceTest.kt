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

class MovieCategoryDataSourceTest {

    private val fakeAssetReader = FakeAssetReader()
    private val dataSource = MovieCategoryDataSource(fakeAssetReader)

    @Test
    fun getMovieCategoryList_returnsItems() = runTest {
        val json = """
            [
              {
                "id": "1",
                "name": "Action"
              }
            ]
        """.trimIndent()
        fakeAssetReader.setResponse(StringConstants.Assets.MovieCategories, json)

        val categories = dataSource.getMovieCategoryList()
        assertEquals(1, categories.size)
        assertEquals("Action", categories[0].name)
    }
}
