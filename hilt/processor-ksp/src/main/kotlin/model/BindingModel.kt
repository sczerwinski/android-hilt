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

package it.czerwinski.android.hilt.processor.model

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.processor.kotlinpoet.simpleNames

class BindingModel private constructor(
    override val declarationFile: KSFile,
    val annotatedClassName: ClassName,
    val supertypeClassName: TypeName,
    override val componentClassName: ClassName,
    override val annotations: List<AnnotationSpec>,
    override val isTest: Boolean
) : Model {

    override val moduleKind: HiltModuleKind get() = HiltModuleKind.INTERFACE

    override val moduleClassName: ClassName = ClassName(
        packageName = declarationFile.packageName.asString(),
        simpleNames = listOf(
            (if (isTest) TEST_MODULE_NAME_FORMAT else MODULE_NAME_FORMAT)
                .format(supertypeClassName.simpleNames.joinToString(NAME_SEPARATOR), componentClassName.simpleName)
        )
    )

    override val replacesModuleClassName: ClassName = ClassName(
        packageName = declarationFile.packageName.asString(),
        simpleNames = listOf(
            MODULE_NAME_FORMAT
                .format(supertypeClassName.simpleNames.joinToString(NAME_SEPARATOR), componentClassName.simpleName)
        )
    )

    val bindMethodName: String get() = "$BIND_METHOD_NAME_PREFIX${annotatedClassName.simpleName}"

    interface Builder {
        fun setAnnotatedClassName(annotatedClassName: ClassName): Builder
        fun setSupertypeClassName(supertypeClassName: TypeName): Builder
        fun setComponentClassName(componentClassName: ClassName): Builder
        fun addAnnotation(annotation: AnnotationSpec): Builder
        fun testBinding(): Builder
        fun build(): BindingModel
    }

    internal class BuilderImpl(
        private val declarationFile: KSFile
    ) : Builder {
        private var annotatedClassName: ClassName? = null
        private var supertypeClassName: TypeName? = null
        private var componentClassName: ClassName = SingletonComponent::class.asClassName()
        private val annotations: MutableList<AnnotationSpec> = mutableListOf()
        private var isTest: Boolean = false

        override fun setAnnotatedClassName(annotatedClassName: ClassName): Builder {
            this.annotatedClassName = annotatedClassName
            return this
        }

        override fun setSupertypeClassName(supertypeClassName: TypeName): Builder {
            this.supertypeClassName = supertypeClassName
            return this
        }

        override fun setComponentClassName(componentClassName: ClassName): Builder {
            this.componentClassName = componentClassName
            return this
        }

        override fun addAnnotation(annotation: AnnotationSpec): Builder {
            this.annotations += annotation
            return this
        }

        override fun testBinding(): Builder {
            this.isTest = true
            return this
        }

        override fun build(): BindingModel =
            BindingModel(
                declarationFile = this.declarationFile,
                annotatedClassName = checkNotNull(this.annotatedClassName) { "Annotated class name not set" },
                supertypeClassName = checkNotNull(this.supertypeClassName) { "Supertype class name not set" },
                componentClassName = this.componentClassName,
                annotations = this.annotations.toList(),
                isTest = this.isTest
            )
    }

    companion object {
        private const val NAME_SEPARATOR = "_"
        private const val MODULE_NAME_FORMAT = "%s_%s_BindingsModule"
        private const val TEST_MODULE_NAME_FORMAT = "%s_%s_BindingsModule_Test"
        private const val BIND_METHOD_NAME_PREFIX = "bind"

        fun builder(declarationFile: KSFile): Builder =
            BuilderImpl(declarationFile)
    }
}
