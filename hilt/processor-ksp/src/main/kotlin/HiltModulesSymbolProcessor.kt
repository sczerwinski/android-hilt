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

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import it.czerwinski.android.hilt.annotations.Bound
import it.czerwinski.android.hilt.annotations.BoundTo
import it.czerwinski.android.hilt.annotations.FactoryMethod
import it.czerwinski.android.hilt.annotations.TestBound
import it.czerwinski.android.hilt.annotations.TestBoundTo
import it.czerwinski.android.hilt.annotations.TestFactoryMethod
import it.czerwinski.android.hilt.processor.kotlinpoet.BindingModuleGenerator
import it.czerwinski.android.hilt.processor.kotlinpoet.FactoryMethodModuleGenerator

/**
 * Generates Hilt modules from annotations:
 * * [Bound]
 * * [BoundTo]
 * * [FactoryMethod]
 * * [TestBound]
 * * [TestBoundTo]
 * * [TestFactoryMethod]
 */
class HiltModulesSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        processBindings(resolver)
        processFactoryMethods(resolver)
        return emptyList()
    }

    private fun processBindings(resolver: Resolver) {
        val groupedBindings = BindingsCollector().collect(resolver, logger)
        val generator = BindingModuleGenerator()
        for (bindings in groupedBindings.values) {
            generator.generateModule(codeGenerator, bindings)
        }
    }

    private fun processFactoryMethods(resolver: Resolver) {
        val groupedFactoryMethods = FactoryMethodsCollector().collect(resolver, logger)
        val generator = FactoryMethodModuleGenerator()
        for (factoryMethods in groupedFactoryMethods.values) {
            generator.generateModule(codeGenerator, factoryMethods)
        }
    }
}
