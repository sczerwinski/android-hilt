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

package it.czerwinski.android.hilt.annotations

import kotlin.reflect.KClass

/**
 * Marks primary implementation of the given [supertype].
 *
 * The implementation will be bound to the given supertype in the given [component].
 *
 * Any annotations annotated with `@Scope` or `@Qualifier` will also annotate the resulting `@Binds` method.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Primary(

    /**
     * Supertype for which the primary implementation should be bound.
     *
     * Must be assignable from the class annotated with [Primary].
     */
    val supertype: KClass<*>,

    /**
     * Hilt component in which the binding should be installed.
     *
     * Must be a type annotated with `@DefineComponent`.
     */
    val component: KClass<*>
)
