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

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.processor.className
import it.czerwinski.android.hilt.processor.findAnnotationOfType
import it.czerwinski.android.hilt.processor.scopesAndQualifiers
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class BindingBuilder(private val processingEnv: ProcessingEnvironment) {

    private var element: Element? = null
    private var annotationType: Class<*>? = null

    fun forElement(element: Element): BindingBuilder {
        this.element = element
        return this
    }

    fun annotatedWith(annotationType: Class<*>): BindingBuilder {
        this.annotationType = annotationType
        return this
    }

    fun build(): Binding {
        val element = requireNotNull(element) { "No element was set" }
        val annotationType = requireNotNull(annotationType) { "No annotation was set" }

        val visitor = BoundComponentVisitor()

        element.findAnnotationOfType(annotationType)?.elementValues?.forEach { (element, value) ->
            value.accept(visitor, element.simpleName.toString())
        }

        return Binding(
            annotatedClassName = element.className(),
            supertypeClassName = visitor.supertypeClassName ?: getSupertypeClassName(element),
            componentClassName = visitor.componentClassName as? ClassName ?: defaultComponentClassName,
            annotations = element.scopesAndQualifiers().map(AnnotationSpec::get),
            isTest = annotationType.simpleName.startsWith(TEST_ANNOTATION_PREFIX)
        )
    }

    private fun getSupertypeClassName(element: Element): TypeName =
        TypeName.get(getSupertype(element))

    private fun getSupertype(element: Element): TypeMirror {
        val supertypes = processingEnv.typeUtils.directSupertypes(element.asType())
            .filterNot { TypeName.get(it) == TypeName.get(Object::class.java) }
        if (supertypes.size != 1) {
            val errorMessage =
                "Class $element has ${supertypes.size} direct supertypes: $supertypes, but exactly 1 required"
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, errorMessage)
        }
        return supertypes.first()
    }

    companion object {
        private const val TEST_ANNOTATION_PREFIX = "Test"

        private val defaultComponentClassName = ClassName.get(SingletonComponent::class.java)
    }
}
