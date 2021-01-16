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

package it.czerwinski.android.hilt.processor.model

import androidx.annotation.NonNull
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.processor.className
import it.czerwinski.android.hilt.processor.findAnnotationOfType
import it.czerwinski.android.hilt.processor.scopesAndQualifiers
import it.czerwinski.android.hilt.processor.simpleName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

class FactoryMethodModelBuilder {

    private var element: Element? = null
    private var annotationType: Class<*>? = null

    fun forElement(element: Element): FactoryMethodModelBuilder {
        this.element = element
        return this
    }

    fun annotatedWith(annotationType: Class<*>): FactoryMethodModelBuilder {
        this.annotationType = annotationType
        return this
    }

    fun build(): FactoryMethodModel {
        val element = requireNotNull(element) { "No element was set" }
        require(value = element.kind == ElementKind.METHOD) { "Factory method is not a method" }
        val annotationType = requireNotNull(annotationType) { "No annotation was set" }

        val annotatedMethodElement = element as ExecutableElement
        val enclosingElement = element.enclosingElement

        val visitor = FactoryMethodComponentVisitor()

        element.findAnnotationOfType(annotationType)?.elementValues?.forEach { (element, value) ->
            value.accept(visitor, element.simpleName.toString())
        }

        return FactoryMethodModel(
            methodName = element.simpleName.toString(),
            isStatic = Modifier.STATIC in element.modifiers,
            parameters = annotatedMethodElement.parameters.map(::createParameterSpec),
            returnTypeName = TypeName.get(annotatedMethodElement.returnType),
            enclosingClassName = enclosingElement.className(),
            enclosingElementKind = KotlinElementKind.forElement(enclosingElement),
            componentClassName = visitor.componentClassName as? ClassName ?: defaultComponentClassName,
            annotations = element.scopesAndQualifiers().map(AnnotationSpec::get)
        )
    }

    private fun createParameterSpec(parameter: VariableElement) =
        ParameterSpec.builder(TypeName.get(parameter.asType()), parameter.simpleName())
            .addAnnotations(parameter.scopesAndQualifiers().map(AnnotationSpec::get))
            .apply {
                if (!parameter.asType().kind.isPrimitive) {
                    addAnnotation(NonNull::class.java)
                }
            }
            .build()

    companion object {
        private val defaultComponentClassName = ClassName.get(SingletonComponent::class.java)
    }
}
