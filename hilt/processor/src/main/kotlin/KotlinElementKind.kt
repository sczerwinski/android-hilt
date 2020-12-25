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

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier

enum class KotlinElementKind {
    CLASS, OBJECT, FILE;

    companion object {

        fun forElement(element: Element): KotlinElementKind = when {
            element.getAnnotation(Metadata::class.java).kind == 2 -> FILE
            element.enclosedElements.any(::isObjectSingletonInstance) -> OBJECT
            else -> CLASS
        }

        private fun isObjectSingletonInstance(element: Element) =
            element.kind == ElementKind.FIELD &&
                Modifier.STATIC in element.modifiers &&
                element.simpleName.contentEquals("INSTANCE")
    }
}
