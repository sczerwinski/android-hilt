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

package it.czerwinski.android.hilt.processor.visitors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import it.czerwinski.android.hilt.processor.model.TypesCollection
import javax.inject.Qualifier
import javax.inject.Scope

abstract class BaseVisitor<T> : KSDefaultVisitor<KSPLogger, Sequence<T>>() {

    override fun defaultHandler(node: KSNode, data: KSPLogger): Sequence<T> = emptySequence()

    protected fun KSAnnotation.isCopiedAnnotation(): Boolean =
        annotationType.resolve().declaration.annotations.any { it in copiedAnnotations }

    companion object {

        internal const val TEST_ANNOTATION_PREFIX = "Test"

        private val copiedAnnotations = TypesCollection.of(Scope::class, Qualifier::class)
    }
}
