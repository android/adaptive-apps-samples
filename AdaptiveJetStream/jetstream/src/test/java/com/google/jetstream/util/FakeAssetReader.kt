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

package com.google.jetstream.util

import com.google.jetstream.data.util.AssetReader

class FakeAssetReader : AssetReader {
    private val responses = mutableMapOf<String, String>()

    fun setResponse(fileName: String, content: String) {
        responses[fileName] = content
    }

    override fun getJsonDataFromAsset(fileName: String): Result<String> {
        val content = responses[fileName]
        return if (content != null) {
            Result.success(content)
        } else {
            Result.failure(Exception("File not found: $fileName"))
        }
    }
}
