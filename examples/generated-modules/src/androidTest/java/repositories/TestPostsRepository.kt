/*
 * Copyright 2020 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.czerwinski.android.hilt.examples.generated.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import it.czerwinski.android.hilt.annotations.TestBound
import it.czerwinski.android.hilt.examples.generated.model.Post
import it.czerwinski.android.hilt.examples.generated.remote.resources.Posts
import javax.inject.Inject

@TestBound
class TestPostsRepository @Inject constructor(
    private val client: HttpClient,
) : PostsRepository {

    override suspend fun findAll(): List<Post> = client.get(Posts()).body()
}
