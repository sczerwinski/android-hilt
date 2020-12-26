/*
 * Copyright 2020 Slawomir Czerwinski
 * Copyright (C) 2018 The Android Open Source Project
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
 * Based on androidx.fragment.app.testing.FragmentScenario.
 * Modified to add support for Hilt dependency injection.
 *
 */

package it.czerwinski.android.hilt.fragment.testing

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.AndroidEntryPoint

/**
 * `HiltFragmentScenario` provides API to start and drive a Fragment's lifecycle state for testing.
 *
 * Unlike the `FragmentScenario` from AndroidX Fragment Testing library, `HiltFragmentScenario` supports fragments
 * annotated with [dagger.hilt.android.AndroidEntryPoint].
 *
 * It also supports testing fragment in a custom [FragmentActivity].
 */
class HiltFragmentScenario<F : Fragment, A : FragmentActivity> private constructor(
    private val fragmentClass: Class<F>,
    private val activityScenario: ActivityScenario<A>
) {

    /**
     * Moves Fragment state to a new state.
     *
     * If a new state and current state are the same, this method does nothing.
     *
     * It accepts [CREATED][Lifecycle.State.CREATED], [STARTED][Lifecycle.State.STARTED],
     * [RESUMED][Lifecycle.State.RESUMED], and [DESTROYED][Lifecycle.State.DESTROYED].
     *
     * [DESTROYED][Lifecycle.State.DESTROYED] is a terminal state.
     * You cannot move to any other state after the Fragment reaches that state.
     *
     * This method cannot be called from the main thread.
     *
     * _Note: Moving state to [STARTED][Lifecycle.State.STARTED] is not supported on Android API level 23 and lower.
     * [UnsupportedOperationException] will be thrown._
     */
    fun moveToState(newState: Lifecycle.State): HiltFragmentScenario<F, A> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && newState == Lifecycle.State.STARTED) {
            throw UnsupportedOperationException(
                "Moving state to STARTED is not supported on Android API level 23 and lower."
                    + " This restriction comes from the combination of the Android framework bug"
                    + " around the timing of onSaveInstanceState invocation and its workaround code"
                    + " in FragmentActivity. See http://issuetracker.google.com/65665621#comment3"
                    + " for more information."
            )
        }
        if (newState == Lifecycle.State.DESTROYED) {
            activityScenario.onActivity { activity ->
                val fragment = activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
                if (fragment != null) {
                    activity
                        .supportFragmentManager
                        .beginTransaction()
                        .remove(fragment)
                        .commitNowAllowingStateLoss()
                }
            }
        } else {
            activityScenario.onActivity { activity ->
                val fragment = activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
                checkNotNull(fragment) { "The fragment has been removed from FragmentManager already." }
            }
            activityScenario.moveToState(newState)
        }
        return this
    }

    /**
     * Recreates the host Activity.
     *
     * After this method call, it is ensured that the Fragment state goes back to the same state
     * as its previous state.
     *
     * This method cannot be called from the main thread.
     */
    fun recreate(): HiltFragmentScenario<F, A> {
        activityScenario.recreate()
        return this
    }

    /**
     * Runs a given [action] on the current Activity's main thread.
     *
     * Note that you should never keep Fragment reference passed into your [action] because it can be recreated
     * at anytime during state transitions.
     *
     * Throwing an exception from [action] makes the host Activity crash. You can inspect the exception in logcat
     * outputs.
     *
     * This method cannot be called from the main thread.
     */
    fun onFragment(action: FragmentAction<F>): HiltFragmentScenario<F, A> {
        activityScenario.onActivity { activity ->
            val fragment = activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
            checkNotNull(fragment) { "The fragment has been removed from FragmentManager already." }
            check(fragmentClass.isInstance(fragment)) { "The fragment is not an instance of ${fragmentClass.name}" }
            action.perform(checkNotNull(fragmentClass.cast(fragment)))
        }
        return this
    }

    /**
     * Runs a given [action] on the current Activity's main thread.
     *
     * Note that you should never keep Activity reference passed into your [action] because it can be recreated
     * at anytime during state transitions.
     */
    fun onActivity(action: ActivityAction<A>): HiltFragmentScenario<F, A> {
        activityScenario.onActivity(action)
        return this
    }

    /**
     * FragmentAction interface should be implemented by any class whose instances are intended to be executed
     * by the main thread. A Fragment that is instrumented by the HiltFragmentScenario is passed
     * to [FragmentAction.perform] method.
     *
     * You should never keep the Fragment reference as it will lead to unpredictable behaviour.
     * It should only be accessed in [FragmentAction.perform] scope.
     */
    fun interface FragmentAction<F : Fragment> {

        /**
         * This method is invoked on the main thread with the reference to the [fragment].
         */
        @MainThread
        fun perform(fragment: F)
    }

    /**
     * A ViewModel to hold a fragment factory.
     */
    class FragmentFactoryViewModel @ViewModelInject constructor() : ViewModel() {

        var fragmentFactory: FragmentFactory? = null

        override fun onCleared() {
            super.onCleared()
            fragmentFactory = null
        }

        private object Factory : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val viewModel = FragmentFactoryViewModel()
                return checkNotNull(modelClass.cast(viewModel))
            }
        }

        companion object {
            fun getInstance(activity: FragmentActivity): FragmentFactoryViewModel {
                val viewModelProvider = ViewModelProvider(activity, Factory)
                return viewModelProvider[FragmentFactoryViewModel::class.java]
            }
        }
    }

    /**
     * An empty activity inheriting [FragmentActivity], annotated with [AndroidEntryPoint].
     *
     * This Activity is used to host Fragment in HiltFragmentScenario.
     */
    @AndroidEntryPoint
    class EmptyFragmentActivity : FragmentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            val themeId = intent.getIntExtra(EXTRA_THEME, defaultTheme)
            setTheme(themeId)

            FragmentFactoryViewModel.getInstance(this).fragmentFactory?.let { factory ->
                supportFragmentManager.fragmentFactory = factory
            }

            super.onCreate(savedInstanceState)
        }
    }

    companion object {

        const val EXTRA_THEME = "it.czerwinski.android.hilt.fragment.testing.HiltFragmentScenario.INTENT_EXTRA_THEME"
        val defaultTheme = R.style.HiltFragmentScenarioEmptyActivityTheme

        private const val FRAGMENT_TAG = "HiltFragmentScenario_Fragment_Tag"

        /**
         * Launches a Fragment with given arguments hosted by an empty [FragmentActivity] themed by [themeResId],
         * using the given [FragmentFactory] and waits for it to reach the resumed state.
         *
         * This method cannot be called from the main thread.
         */
        fun <F : Fragment> launch(
            fragmentClass: Class<F>,
            fragmentArgs: Bundle? = null,
            @StyleRes themeResId: Int = defaultTheme,
            factory: FragmentFactory? = null
        ): HiltFragmentScenario<F, EmptyFragmentActivity> = launch(
            EmptyFragmentActivity::class.java, fragmentClass, fragmentArgs, themeResId, factory
        )

        /**
         * Launches a Fragment with given arguments hosted by an empty [FragmentActivity] themed by [themeResId],
         * using the given [FragmentFactory] and waits for it to reach the resumed state.
         *
         * This method cannot be called from the main thread.
         */
        fun <F : Fragment, A : FragmentActivity> launch(
            activityClass: Class<A>,
            fragmentClass: Class<F>,
            fragmentArgs: Bundle? = null,
            @StyleRes themeResId: Int = defaultTheme,
            factory: FragmentFactory? = null
        ): HiltFragmentScenario<F, A> = internalLaunch(
            activityClass, fragmentClass, fragmentArgs, themeResId, factory, containerViewId = 0
        )

        /**
         * Launches a Fragment in the Activity's root view container `android.R.id.content`, with given arguments
         * hosted by an empty [FragmentActivity] themed by [themeResId], using the given [FragmentFactory]
         * and waits for it to reach the resumed state.
         *
         * This method cannot be called from the main thread.
         */
        fun <F : Fragment> launchInContainer(
            fragmentClass: Class<F>,
            fragmentArgs: Bundle? = null,
            @StyleRes themeResId: Int = defaultTheme,
            factory: FragmentFactory? = null
        ): HiltFragmentScenario<F, EmptyFragmentActivity> = launchInContainer(
            EmptyFragmentActivity::class.java, fragmentClass, fragmentArgs, themeResId, factory
        )

        /**
         * Launches a Fragment in the Activity's root view container `android.R.id.content`, with given arguments
         * hosted by an empty [FragmentActivity] themed by [themeResId], using the given [FragmentFactory]
         * and waits for it to reach the resumed state.
         *
         * This method cannot be called from the main thread.
         */
        fun <F : Fragment, A : FragmentActivity> launchInContainer(
            activityClass: Class<A>,
            fragmentClass: Class<F>,
            fragmentArgs: Bundle? = null,
            @StyleRes themeResId: Int = defaultTheme,
            factory: FragmentFactory? = null
        ): HiltFragmentScenario<F, A> = internalLaunch(
            activityClass, fragmentClass, fragmentArgs, themeResId, factory, containerViewId = android.R.id.content
        )

        @Suppress("LongParameterList")
        private fun <F : Fragment, A : FragmentActivity> internalLaunch(
            activityClass: Class<A>,
            fragmentClass: Class<F>,
            fragmentArgs: Bundle?,
            @StyleRes themeResId: Int,
            factory: FragmentFactory?,
            @IdRes containerViewId: Int
        ): HiltFragmentScenario<F, A> {
            val startActivityIntent =
                Intent.makeMainActivity(ComponentName(ApplicationProvider.getApplicationContext(), activityClass))
                    .putExtra(EXTRA_THEME, themeResId)
            val activityScenario = ActivityScenario.launch<A>(startActivityIntent)
            return HiltFragmentScenario(fragmentClass, activityScenario)
                .onActivity { activity ->
                    if (factory != null) {
                        FragmentFactoryViewModel.getInstance(activity).fragmentFactory = factory
                        activity.supportFragmentManager.fragmentFactory = factory
                    }
                    val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                        checkNotNull(fragmentClass.classLoader) { "No ClassLoader for class ${fragmentClass.name}" },
                        fragmentClass.name
                    )
                    fragment.arguments = fragmentArgs
                    activity.supportFragmentManager
                        .beginTransaction()
                        .add(containerViewId, fragment, FRAGMENT_TAG)
                        .commitNow()
                }
        }
    }
}
