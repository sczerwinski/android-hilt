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
 * Marks primary implementation of the given [superclass].
 *
 * The implementation will be bound to the given superclass in the given [component]. Optionally, it will be annotated
 * with the given [scope] and/or [qualifier].
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Primary(

    /**
     * Superclass for which the primary implementation should be bound.
     *
     * Must be assignable from the class annotated with [Primary].
     */
    val superclass: KClass<*>,

    /**
     * Hilt component in which the binding should be installed.
     *
     * Must be a type annotated with `@DefineComponent`.
     */
    val component: KClass<*>,

    /**
     * Scope for which the primary implementation is provided.
     *
     * Must be an annotation class annotated with `@Scope`.
     */
    val scope: KClass<*> = Unscoped::class,

    /**
     * Scope for which the primary implementation is provided.
     *
     * Must be an annotation class annotated with `@Qualifier`.
     */
    val qualifier: KClass<*> = Unqualified::class
)
