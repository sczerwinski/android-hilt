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
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import it.czerwinski.android.hilt.annotations.Bound
import it.czerwinski.android.hilt.annotations.BoundTo
import it.czerwinski.android.hilt.annotations.TestBound
import it.czerwinski.android.hilt.annotations.TestBoundTo
import it.czerwinski.android.hilt.processor.model.BindingModel
import it.czerwinski.android.hilt.processor.model.TypesCollection

class BoundImplementationVisitor : BaseVisitor<BindingModel>() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: KSPLogger): Sequence<BindingModel> {
        val boundAnnotations = classDeclaration.annotations
            .filter { annotation -> annotation in boundAnnotations }
        val copiedAnnotations = (classDeclaration.annotations - boundAnnotations.toSet())
            .filter { annotation -> annotation.isCopiedAnnotation() }
            .map { annotation -> annotation.toAnnotationSpec() }
            .toList()
        return boundAnnotations.mapNotNull { annotation ->
            try {
                buildBindingModel(classDeclaration, annotation, copiedAnnotations)
            } catch (ignored: Exception) {
                data.error(ignored.message.orEmpty(), classDeclaration)
                data.exception(ignored)
                null
            }
        }
    }

    private fun buildBindingModel(
        classDeclaration: KSClassDeclaration,
        boundAnnotation: KSAnnotation,
        copiedAnnotations: List<AnnotationSpec>
    ): BindingModel {
        val builder = BindingModel.builder(
            declarationFile = requireNotNull(classDeclaration.containingFile) {
                "'${classDeclaration.qualifiedName}' class declaration is not contained in file"
            }
        )

        builder.setAnnotatedClassName(classDeclaration.toClassName())

        val supertype = boundAnnotation.arguments
            .find { it.name?.asString() == ARG_SUPERTYPE_NAME }
            ?.value as? KSType
        builder.setSupertypeClassName(supertype?.toTypeName() ?: classDeclaration.findSupertype())

        val component = boundAnnotation.arguments
            .find { it.name?.asString() == ARG_COMPONENT_NAME }
            ?.value as? KSType
        if (component != null) {
            builder.setComponentClassName(component.toClassName())
        }

        for (copiedAnnotation in copiedAnnotations) {
            builder.addAnnotation(copiedAnnotation)
        }

        if (boundAnnotation.shortName.asString().startsWith(TEST_ANNOTATION_PREFIX)) {
            builder.testBinding()
        }

        return builder.build()
    }

    private fun KSClassDeclaration.findSupertype(): TypeName =
        superTypes
            .singleOrNull { it.toTypeName() != Object::class.asTypeName() }
            ?.toTypeName()
            ?: error(
                "Class $simpleName does not have a single supertype and should be annotated with BoundTo or TestBoundTo"
            )

    companion object {

        private const val ARG_SUPERTYPE_NAME = "supertype"
        private const val ARG_COMPONENT_NAME = "component"

        val boundAnnotations = TypesCollection.of(Bound::class, BoundTo::class, TestBound::class, TestBoundTo::class)
    }
}
