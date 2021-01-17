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
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import it.czerwinski.android.hilt.annotations.Bound
import it.czerwinski.android.hilt.examples.generated.db.PostsDao
import it.czerwinski.android.hilt.examples.generated.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

@Bound
class DispatchingPostsRepository @Inject constructor(
    @Named(value = "remote") private val remoteRepository: PostsRepository,
    @Named(value = "local") private val localRepository: PostsRepository,
    private val dao: PostsDao
) : PostsRepository {

    override fun findAll(): LiveData<List<Post>> = liveData {
        emitSource(localRepository.findAll())
        val remoteLiveData = remoteRepository.findAll()
        val remoteObserver = RemoteLiveDataObserver(remoteLiveData)
        remoteLiveData.observeForever(remoteObserver)
    }

    private inner class RemoteLiveDataObserver(
        private val remoteLiveData: LiveData<List<Post>>
    ): Observer<List<Post>> {

        override fun onChanged(posts: List<Post>) {
            runBlocking(Dispatchers.IO) {
                dao.replace(posts)
            }
            remoteLiveData.removeObserver(this)
        }
    }
}
