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

import it.czerwinski.android.hilt.annotations.Bound
import it.czerwinski.android.hilt.annotations.BoundTo
import it.czerwinski.android.hilt.annotations.FactoryMethod
import it.czerwinski.android.hilt.annotations.TestBound
import it.czerwinski.android.hilt.annotations.TestBoundTo
import it.czerwinski.android.hilt.annotations.TestFactoryMethod
import it.czerwinski.android.hilt.processor.model.Binding
import it.czerwinski.android.hilt.processor.model.BindingBuilder
import it.czerwinski.android.hilt.processor.model.FactoryMethodModel
import it.czerwinski.android.hilt.processor.model.FactoryMethodModelBuilder
import it.czerwinski.android.hilt.processor.poet.BindingsModulePoet
import it.czerwinski.android.hilt.processor.poet.FactoryMethodsModulePoet
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class HiltModulesGenerator : AbstractProcessor() {

    private val filer get() = processingEnv.filer

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        (boundAnnotationTypes + factoryMethodAnnotationTypes)
            .map { it.canonicalName }
            .toMutableSet()

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        boundAnnotationTypes
            .flatMap { annotationType -> createBindings(roundEnvironment, annotationType) }
            .groupBy { binding -> binding.groupingKey }
            .forEach { (groupingKey, bindings) ->
                BindingsModulePoet.generateModule(groupingKey, bindings, filer)
            }
        factoryMethodAnnotationTypes
            .flatMap { annotationType -> createFactoryMethods(roundEnvironment, annotationType) }
            .groupBy { factoryMethod -> factoryMethod.groupingKey }
            .forEach { (groupingKey, factoryMethods) ->
                FactoryMethodsModulePoet.generateModule(groupingKey, factoryMethods, filer)
            }
        return true
    }

    private fun createBindings(
        roundEnvironment: RoundEnvironment,
        annotationType: Class<out Annotation>
    ): List<Binding> =
        roundEnvironment.getElementsAnnotatedWith(annotationType)
            .map { element -> createBinding(element, annotationType) }

    private fun createBinding(element: Element, annotationType: Class<out Annotation>): Binding =
        BindingBuilder(processingEnv)
            .forElement(element)
            .annotatedWith(annotationType)
            .build()

    private fun createFactoryMethods(
        roundEnvironment: RoundEnvironment,
        annotationType: Class<out Annotation>
    ): List<FactoryMethodModel> =
        roundEnvironment.getElementsAnnotatedWith(annotationType)
            .map { element -> createFactoryMethod(element, annotationType) }

    private fun createFactoryMethod(element: Element, annotationType: Class<out Annotation>): FactoryMethodModel =
        FactoryMethodModelBuilder()
            .forElement(element)
            .annotatedWith(annotationType)
            .build()

    companion object {
        private val boundAnnotationTypes = listOf(
            Bound::class.java, BoundTo::class.java, TestBound::class.java, TestBoundTo::class.java
        )
        private val factoryMethodAnnotationTypes = listOf(
            FactoryMethod::class.java, TestFactoryMethod::class.java
        )
    }
}
