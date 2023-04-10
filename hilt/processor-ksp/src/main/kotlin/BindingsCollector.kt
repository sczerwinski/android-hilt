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

package it.czerwinski.android.hilt.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.ClassName
import it.czerwinski.android.hilt.processor.model.BindingModel
import it.czerwinski.android.hilt.processor.visitors.BoundImplementationVisitor

class BindingsCollector {

    fun collect(resolver: Resolver, logger: KSPLogger): Map<ClassName, List<BindingModel>> {
        val visitor = BoundImplementationVisitor()
        return BoundImplementationVisitor.boundAnnotations.asSequence()
            .mapNotNull { annotationType -> annotationType.qualifiedName }
            .flatMap { annotation -> resolver.getSymbolsWithAnnotation(annotation) }
            .distinct()
            .flatMap { symbol -> symbol.accept(visitor, logger) }
            .groupBy { binding -> binding.moduleClassName }
    }
}
