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

import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

/**
 * Marks test factory method for the class returned by the annotated function.
 *
 * The implementation will be provided in the given [component].
 *
 * Any annotations annotated with `@Scope` or `@Qualifier` will also annotate the resulting `@Provides` method.
 *
 * If the annotated function is neither static nor a member of an object, the enclosing class will become
 * the first parameter of the generated provide function.
 *
 * @since 1.1.0
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class TestFactoryMethod(

    /**
     * Hilt component in which the provider should be installed.
     *
     * Must be a type annotated with `@DefineComponent`.
     *
     * If not defined, the provider will be installed in [SingletonComponent].
     */
    val component: KClass<*> = SingletonComponent::class
)
