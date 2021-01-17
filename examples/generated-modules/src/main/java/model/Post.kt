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

package it.czerwinski.android.hilt.examples.generated.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import it.czerwinski.android.hilt.examples.generated.remote.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joda.time.LocalDate

@Entity(tableName = "posts")
@Serializable
data class Post(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "brief")
    @SerialName(value = "abstract")
    val brief: String,

    @ColumnInfo(name = "image_url")
    val image: String? = null,

    @ColumnInfo(name = "tags")
    val tags: List<String>,

    @ColumnInfo(name = "post_url")
    val url: String,

    @ColumnInfo(name = "published_date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
)
