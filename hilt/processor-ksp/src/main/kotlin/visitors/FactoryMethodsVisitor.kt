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
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import it.czerwinski.android.hilt.annotations.FactoryMethod
import it.czerwinski.android.hilt.annotations.TestFactoryMethod
import it.czerwinski.android.hilt.processor.model.FactoryMethodModel
import it.czerwinski.android.hilt.processor.model.KotlinElementKind
import it.czerwinski.android.hilt.processor.model.TypesCollection

class FactoryMethodsVisitor : BaseVisitor<FactoryMethodModel>() {

    override fun visitFunctionDeclaration(
        function: KSFunctionDeclaration,
        data: KSPLogger
    ): Sequence<FactoryMethodModel> {
        val factoryMethodAnnotations = function.annotations
            .filter { annotation -> annotation in factoryMethodAnnotations }
        val copiedAnnotations = (function.annotations - factoryMethodAnnotations.toSet())
            .filter { annotation -> annotation.isCopiedAnnotation() }
            .map { annotation -> annotation.toAnnotationSpec() }
            .toList()
        return factoryMethodAnnotations.mapNotNull { annotation ->
            try {
                buildFactoryMethodModel(function, annotation, copiedAnnotations)
            } catch (ignored: Exception) {
                data.error(ignored.message.orEmpty(), function)
                data.exception(ignored)
                null
            }
        }
    }

    private fun buildFactoryMethodModel(
        function: KSFunctionDeclaration,
        factoryMethodAnnotation: KSAnnotation,
        copiedAnnotations: List<AnnotationSpec>
    ): FactoryMethodModel {
        val builder = FactoryMethodModel.builder(
            declarationFile = requireNotNull(function.containingFile) {
                "'${function.qualifiedName}' function declaration is not contained in file"
            }
        )

        builder.setMethodName(function.simpleName.asString())

        for (parameter in function.parameters) {
            builder.addParameter(
                ParameterSpec
                    .builder(
                        name = parameter.name?.asString().orEmpty(),
                        type = parameter.type.toTypeName()
                    )
                    .addAnnotations(
                        parameter.annotations
                            .filter { it.isCopiedAnnotation() }
                            .map { it.toAnnotationSpec() }
                            .toList()
                    )
                    .build()
            )
        }

        val returnType = function.returnType?.toTypeName()
        if (returnType != null) {
            builder.setReturnTypeName(returnType)
        }

        val enclosingDeclaration = function.parentDeclaration as? KSClassDeclaration
        if (enclosingDeclaration != null) {
            builder.setEnclosingClassName(enclosingDeclaration.toClassName())
            builder.setEnclosingElementKind(
                when (enclosingDeclaration.classKind) {
                    ClassKind.INTERFACE,
                    ClassKind.CLASS,
                    ClassKind.ENUM_CLASS,
                    ClassKind.ENUM_ENTRY -> KotlinElementKind.CLASS
                    ClassKind.OBJECT -> KotlinElementKind.OBJECT
                    ClassKind.ANNOTATION_CLASS -> error("Annotations cannot contain factory methods")
                }
            )
        } else {
            builder.setEnclosingElementKind(KotlinElementKind.FILE)
        }


        val component = factoryMethodAnnotation.arguments
            .find { it.name?.asString() == ARG_COMPONENT_NAME }
            ?.value as? KSType
        if (component != null) {
            builder.setComponentClassName(component.toClassName())
        }

        for (copiedAnnotation in copiedAnnotations) {
            builder.addAnnotation(copiedAnnotation)
        }

        if (factoryMethodAnnotation.shortName.asString().startsWith(TEST_ANNOTATION_PREFIX)) {
            builder.testFactoryMethod()
        }

        return builder.build()
    }

    companion object {

        private const val ARG_COMPONENT_NAME = "component"

        val factoryMethodAnnotations = TypesCollection.of(FactoryMethod::class, TestFactoryMethod::class)
    }
}
