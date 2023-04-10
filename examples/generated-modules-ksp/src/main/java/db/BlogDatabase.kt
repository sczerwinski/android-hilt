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

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import it.czerwinski.android.hilt.annotations.FactoryMethod
import it.czerwinski.android.hilt.examples.generated.db.converters.LocalDateConverter
import it.czerwinski.android.hilt.examples.generated.db.converters.StringListConverter
import it.czerwinski.android.hilt.examples.generated.model.Post
import it.czerwinski.android.room.database.roomDatabaseBuilder
import javax.inject.Singleton

@Database(
    entities = [
        Post::class
    ],
    version = 1
)
@TypeConverters(
    StringListConverter::class,
    LocalDateConverter::class
)
abstract class BlogDatabase : RoomDatabase() {

    @FactoryMethod
    @Singleton
    abstract fun postsDao(): PostsDao

    companion object {

        @FactoryMethod
        @Singleton
        fun createDatabase(@ApplicationContext context: Context): BlogDatabase =
            context.roomDatabaseBuilder<BlogDatabase>()
                .fallbackToDestructiveMigration()
                .build()
    }
}
