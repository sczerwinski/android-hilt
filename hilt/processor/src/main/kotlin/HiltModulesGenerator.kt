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

import androidx.annotation.NonNull
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.annotations.FactoryMethod
import it.czerwinski.android.hilt.annotations.BoundTo
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class HiltModulesGenerator : AbstractProcessor() {

    private val filer get() = processingEnv.filer

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(boundToAnnotationClass.canonicalName, factoryMethodAnnotationClass.canonicalName)

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        roundEnvironment.getElementsAnnotatedWith(boundToAnnotationClass)
            .map { element -> createBindings(element) }
            .groupBy { binding -> binding.packageName to binding.componentClassName }
            .forEach { (groupingKey, bindings) ->
                val (packageName, componentClassName) = groupingKey
                BindingsModulePoet.generateModule(packageName, componentClassName, bindings, filer)
            }
        roundEnvironment.getElementsAnnotatedWith(factoryMethodAnnotationClass)
            .map { element -> createFactoryMethods(element) }
            .groupBy { factoryMethod -> factoryMethod.packageName to factoryMethod.componentClassName }
            .forEach { (groupingKey, factoryMethods) ->
                val (packageName, componentClassName) = groupingKey
                FactoryMethodsModulePoet.generateModule(packageName, componentClassName, factoryMethods, filer)
            }
        return true
    }

    private fun createBindings(element: Element): Binding {
        val elementClassName = element.className()
        val boundToAnnotationMirror = element.findAnnotationOfType<BoundTo>()
        val annotationsToCopy = element.scopesAndQualifiers()
        val builder = BindingBuilder(elementClassName)
        boundToAnnotationMirror?.elementValues?.forEach { (element, value) ->
            value.accept(builder, element.simpleName.toString())
        }
        return builder.build(annotations = annotationsToCopy.map(AnnotationSpec::get))
    }

    private fun Element.className(): ClassName = TypeName.get(asType()) as ClassName

    private inline fun <reified T> Element.findAnnotationOfType() = annotationMirrors.find { annotationMirror ->
        annotationMirror.annotationType.asElement().isOfType(T::class.java)
    }

    private fun Element.isOfType(type: Class<*>): Boolean =
        packageName() == type.`package`.name && simpleName() == type.simpleName

    private fun Element.packageName(): String = processingEnv.elementUtils.getPackageOf(this).toString()

    private fun Element.simpleName(): String = simpleName.toString()

    private fun Element.scopesAndQualifiers(): List<AnnotationMirror> = annotationMirrors.filter { annotationMirror ->
        annotationMirror.annotationType.asElement().annotationMirrors.any {
            val annotationElement = it.annotationType.asElement()
            annotationElement.isOfType(scopeAnnotationClass) || annotationElement.isOfType(qualifierAnnotationClass)
        }
    }

    private fun createFactoryMethods(element: Element): FactoryMethodModel {
        require(value = element.kind == ElementKind.METHOD) { "@FactoryMethod is not a method" }
        val annotatedMethodElement = element as ExecutableElement
        val factoryMethodAnnotationMirror = element.findAnnotationOfType<FactoryMethod>()
        val annotationsToCopy = element.scopesAndQualifiers()
        val visitor = FactoryMethodComponentVisitor()
        factoryMethodAnnotationMirror?.elementValues?.forEach { (element, value) ->
            value.accept(visitor, element.simpleName.toString())
        }
        val enclosingElement = element.enclosingElement
        return FactoryMethodModel(
            methodName = element.simpleName.toString(),
            isStatic = Modifier.STATIC in element.modifiers,
            parameters = annotatedMethodElement.parameters.map { parameter ->
                val paramAnnotationsToCopy = parameter.scopesAndQualifiers()
                ParameterSpec.builder(TypeName.get(parameter.asType()), parameter.simpleName())
                    .addAnnotations(paramAnnotationsToCopy.map(AnnotationSpec::get))
                    .apply {
                        if (!parameter.asType().kind.isPrimitive) {
                            addAnnotation(NonNull::class.java)
                        }
                    }
                    .build()
            },
            returnTypeName = TypeName.get(annotatedMethodElement.returnType),
            enclosingClassName = enclosingElement.className(),
            enclosingElementKind = KotlinElementKind.forElement(enclosingElement),
            componentClassName = visitor.componentClassName as? ClassName ?: defaultComponentClassName,
            annotations = annotationsToCopy.map(AnnotationSpec::get)
        )
    }

    companion object {
        private val boundToAnnotationClass = BoundTo::class.java
        private val factoryMethodAnnotationClass = FactoryMethod::class.java
        private val scopeAnnotationClass = Scope::class.java
        private val qualifierAnnotationClass = Qualifier::class.java

        private val defaultComponentClassName = ClassName.get(SingletonComponent::class.java)
    }
}
