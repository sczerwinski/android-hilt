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

package it.czerwinski.android.hilt.processor

import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.annotations.FactoryMethod
import javax.inject.Named
import javax.inject.Singleton

data class Result(
    val number: Int,
    val text: String
)

@FactoryMethod(component = SingletonComponent::class)
@Singleton
fun createResult(): Result = Result(number = 1, text = "In file")

object FactoryObject {

    @FactoryMethod(component = SingletonComponent::class)
    @Named(value = "object")
    fun createResult(): Result = Result(number = 2, text = "In object")

    @FactoryMethod(component = SingletonComponent::class)
    @Named(value = "static")
    @JvmStatic
    fun createResultStatic(): Result = Result(number = 3, text = "In object (static)")
}

class Factory {

    @FactoryMethod(component = SingletonComponent::class)
    @Named(value = "class")
    fun createResult(): Result = Result(number = 2, text = "In class")

    @FactoryMethod(component = SingletonComponent::class)
    @Named(value = "params")
    fun createResult(
        @Named(value = "number") number: Int,
        @Named(value = "text") text: String
    ): Result = Result(number, text)
}
