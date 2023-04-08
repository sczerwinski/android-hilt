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

package it.czerwinski.android.hilt.fragment.testing.tests.test

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.czerwinski.android.hilt.fragment.testing.launchFragment
import it.czerwinski.android.hilt.fragment.testing.launchFragmentInContainer
import it.czerwinski.android.hilt.fragment.testing.tests.TestFragment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HiltFragmentScenarioExtensionsTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun launchFragment() {
        launchFragment<TestFragment>().use { scenario ->
            scenario.onFragment { fragment ->
                assertNotNull(fragment.viewModel)
                assertEquals("Text from repository", fragment.viewModel.text.value)
            }
            onView(withText("Text from repository")).check(doesNotExist())
        }
    }

    @Test
    fun launchFragmentWithInstantiateMethod() {
        launchFragment { TestFragment() }.use { scenario ->
            scenario.onFragment { fragment ->
                assertNotNull(fragment.viewModel)
                assertEquals("Text from repository", fragment.viewModel.text.value)
            }
            onView(withText("Text from repository")).check(doesNotExist())
        }
    }

    @Test
    fun launchFragmentInContainer() {
        launchFragmentInContainer<TestFragment>().use { scenario ->
            scenario.onFragment { fragment ->
                assertNotNull(fragment.viewModel)
                assertEquals("Text from repository", fragment.viewModel.text.value)
            }
            onView(withText("Text from repository")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun launchFragmentInContainerWithInstantiateMethod() {
        launchFragmentInContainer { TestFragment() }.use { scenario ->
            scenario.onFragment { fragment ->
                assertNotNull(fragment.viewModel)
                assertEquals("Text from repository", fragment.viewModel.text.value)
            }
            onView(withText("Text from repository")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun launchFragmentInContainerWithArgs() {
        val args = Bundle()
        args.putString(TestFragment.ARG_TEXT, "Test text")
        launchFragmentInContainer<TestFragment>(fragmentArgs = args).use { scenario ->
            scenario.onFragment { fragment ->
                assertNotNull(fragment.viewModel)
            }
            onView(withText("Test text")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun launchFragmentInContainerWithTheme() {
        launchFragmentInContainer<TestFragment>(
            themeResId = it.czerwinski.android.hilt.fragment.testing.tests.R.style.TestFragmentTheme
        ).use { scenario ->
            scenario.onFragment { fragment ->
                assertNotNull(fragment.viewModel)
                assertEquals("Text from repository", fragment.viewModel.text.value)
            }
            onView(withText("Text from repository")).check(matches(isDisplayed()))
        }
    }
}
