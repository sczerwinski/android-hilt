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

package it.czerwinski.android.hilt.examples.generated.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import it.czerwinski.android.hilt.examples.generated.model.Post

@Dao
abstract class PostsDao {

    @Query(value = "SELECT * FROM posts")
    abstract suspend fun findAll(): List<Post>

    @Transaction
    open suspend fun replace(posts: List<Post>) {
        deleteAll()
        @Suppress("SpreadOperator")
        insert(*posts.toTypedArray())
    }

    @Query(value = "DELETE FROM posts")
    abstract suspend fun deleteAll()

    @Insert
    abstract suspend fun insert(vararg posts: Post)
}
