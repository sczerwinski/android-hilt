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

package it.czerwinski.android.hilt.examples.generated

import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.czerwinski.android.xpresso.launchTestActivity
import it.czerwinski.android.xpresso.recyclerview.onRecyclerView
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PostsActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    @Ignore("Doesn't work after dependencies update")
    fun testPostsList() {
        launchTestActivity<PostsActivity>()

        onRecyclerView(withId(R.id.posts_list))
            .onItem<PostsAdapter.ViewHolder>(position = 0) {
                on<TextView>(withId(R.id.title))
                    .check(withText("Test"))
                on<TextView>(withId(R.id.brief))
                    .check(withText("Test post"))
            }
    }
}
