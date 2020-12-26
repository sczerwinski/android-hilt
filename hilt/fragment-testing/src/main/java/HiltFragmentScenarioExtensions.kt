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

package it.czerwinski.android.hilt.fragment.testing

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory

/**
 * Launches a Fragment with given arguments hosted by an empty [FragmentActivity] using
 * given [FragmentFactory] and waits for it to reach a resumed state.
 *
 * This method cannot be called from the main thread.
 *
 * @param fragmentArgs a bundle to passed into fragment
 * @param themeResId a style resource id to be set to the host activity's theme
 * @param factory a fragment factory to use or null to use default factory
 */
inline fun <reified F : Fragment> launchFragment(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = HiltFragmentScenario.defaultTheme,
    factory: FragmentFactory? = null
) = HiltFragmentScenario.launch(F::class.java, fragmentArgs, themeResId, factory)

/**
 * Launches a Fragment with given arguments hosted by an empty [FragmentActivity] using
 * [instantiate] to create the Fragment and waits for it to reach a resumed state.
 *
 * This method cannot be called from the main thread.
 *
 * @param fragmentArgs a bundle to passed into fragment
 * @param themeResId a style resource id to be set to the host activity's theme
 * @param instantiate method which will be used to instantiate the Fragment.
 */
inline fun <reified F : Fragment> launchFragment(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = HiltFragmentScenario.defaultTheme,
    crossinline instantiate: () -> F
) = HiltFragmentScenario.launch(F::class.java, fragmentArgs, themeResId, object : FragmentFactory() {
    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ) = when (className) {
        F::class.java.name -> instantiate()
        else -> super.instantiate(classLoader, className)
    }
})

/**
 * Launches a Fragment in the Activity's root view container `android.R.id.content`, with
 * given arguments hosted by an empty [FragmentActivity] and waits for it to reach a
 * resumed state.
 *
 * This method cannot be called from the main thread.
 *
 * @param fragmentArgs a bundle to passed into fragment
 * @param themeResId a style resource id to be set to the host activity's theme
 * @param factory a fragment factory to use or null to use default factory
 */
inline fun <reified F : Fragment> launchFragmentInContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = HiltFragmentScenario.defaultTheme,
    factory: FragmentFactory? = null
) = HiltFragmentScenario.launchInContainer(F::class.java, fragmentArgs, themeResId, factory)

/**
 * Launches a Fragment in the Activity's root view container `android.R.id.content`, with
 * given arguments hosted by an empty [FragmentActivity] using
 * [instantiate] to create the Fragment and waits for it to reach a
 * resumed state.
 *
 * This method cannot be called from the main thread.
 *
 * @param fragmentArgs a bundle to passed into fragment
 * @param themeResId a style resource id to be set to the host activity's theme
 * @param instantiate method which will be used to instantiate the Fragment. This is a
 * simplification of the [FragmentFactory] interface for cases where only a single class
 * needs a custom constructor called.
 */
inline fun <reified F : Fragment> launchFragmentInContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = HiltFragmentScenario.defaultTheme,
    crossinline instantiate: () -> F
) = HiltFragmentScenario.launchInContainer(F::class.java, fragmentArgs, themeResId, object : FragmentFactory() {
    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ) = when (className) {
        F::class.java.name -> instantiate()
        else -> super.instantiate(classLoader, className)
    }
})
