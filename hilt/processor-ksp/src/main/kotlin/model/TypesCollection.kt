/*
 * Copyright 2020-2023 Slawomir Czerwinski
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
 */

package it.czerwinski.android.hilt.processor.model

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

class TypesCollection(private val types: List<KClass<*>>) : List<KClass<*>> by types {

    private val classNames = types.map { it.asClassName() }

    private val simpleNames = types.map { it.simpleName }

    operator fun contains(annotation: KSAnnotation): Boolean =
        annotation.shortName.asString() in simpleNames &&
            annotation.annotationType.resolve().toClassName() in classNames

    companion object {

        fun of(vararg types: KClass<*>): TypesCollection = TypesCollection(types.toList())
    }
}
