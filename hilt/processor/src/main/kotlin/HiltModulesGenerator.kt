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

import com.squareup.javapoet.ClassName
import it.czerwinski.android.hilt.annotations.Primary
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class HiltModulesGenerator : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(primaryAnnotationClass.canonicalName)

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        roundEnvironment.getElementsAnnotatedWith(primaryAnnotationClass)
            .map { element -> createPrimaryBindings(element) }
            .groupBy { primaryBinding -> primaryBinding.packageName to primaryBinding.componentClassName }
            .forEach { (groupingKey, bindings) ->
                val (packageName, componentClassName) = groupingKey
                PrimaryModulePoet.generateModule(packageName, componentClassName, bindings, filer)
            }
        return true
    }

    private fun createPrimaryBindings(element: Element): PrimaryBinding {
        val elementClassName = ClassName.get(element.packageName(), element.simpleName())
        val primaryAnnotationMirror = element.annotationMirrors.find { annotationMirror ->
            annotationMirror.annotationType.asElement().isOfType(primaryAnnotationClass)
        }
        val annotationsToCopy = element.annotationMirrors.filter { annotationMirror ->
            annotationMirror.annotationType.asElement().annotationMirrors.any {
                val annotationElement = it.annotationType.asElement()
                annotationElement.isOfType(scopeAnnotationClass) || annotationElement.isOfType(qualifierAnnotationClass)
            }
        }
        val builder = PrimaryBindingBuilder(elementClassName)
        primaryAnnotationMirror?.elementValues?.forEach { (element, value) ->
            value.accept(builder, element.simpleName.toString())
        }
        return builder.build(annotations = annotationsToCopy)
    }

    private fun Element.isOfType(type: Class<*>): Boolean =
        packageName() == type.`package`.name && simpleName() == type.simpleName

    private fun Element.packageName(): String = processingEnv.elementUtils.getPackageOf(this).toString()

    private fun Element.simpleName(): String = simpleName.toString()

    companion object {
        private val primaryAnnotationClass = Primary::class.java
        private val scopeAnnotationClass = Scope::class.java
        private val qualifierAnnotationClass = Qualifier::class.java
    }
}
