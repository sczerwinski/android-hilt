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

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import it.czerwinski.android.hilt.annotations.Bound
import it.czerwinski.android.hilt.examples.generated.model.Post
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Named

@Bound
@Named(value = "remote")
class RemotePostsRepository @Inject constructor(
    private val client: HttpClient,
) : PostsRepository {

    override fun findAll(): LiveData<List<Post>> = liveData(Dispatchers.IO) {
        val posts: List<Post> = client.get(POSTS_URL)
        emit(posts)
    }

    companion object {
        private const val BASE_URL = "https://czerwinski.it/api"

        private const val POSTS_PATH = "/posts.json"

        private const val POSTS_URL = BASE_URL + POSTS_PATH
    }
}
