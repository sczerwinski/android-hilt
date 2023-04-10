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
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.processor.kotlinpoet.fileClassName
import it.czerwinski.android.hilt.processor.kotlinpoet.simpleNames
import java.util.*

@Suppress("LongParameterList")
class FactoryMethodModel private constructor(
    override val declarationFile: KSFile,
    val methodName: String,
    val parameters: List<ParameterSpec>,
    val returnTypeName: TypeName,
    val enclosingClassName: ClassName?,
    val enclosingElementKind: KotlinElementKind,
    override val componentClassName: ClassName,
    override val annotations: List<AnnotationSpec>,
    override val isTest: Boolean
) : Model {

    override val moduleKind: HiltModuleKind get() = HiltModuleKind.OBJECT

    override val moduleClassName: ClassName = ClassName(
        packageName = declarationFile.packageName.asString(),
        simpleNames = listOf(
            (if (isTest) TEST_MODULE_NAME_FORMAT else MODULE_NAME_FORMAT)
                .format(returnTypeName.simpleNames.joinToString(NAME_SEPARATOR), componentClassName.simpleName)
        )
    )

    override val replacesModuleClassName: ClassName = ClassName(
        packageName = declarationFile.packageName.asString(),
        simpleNames = listOf(
            MODULE_NAME_FORMAT
                .format(returnTypeName.simpleNames.joinToString(NAME_SEPARATOR), componentClassName.simpleName)
        )
    )

    val provideMethodName: String
        get() = ((enclosingClassName?.simpleNames ?: listOf(declarationFile.fileClassName)) + methodName)
            .joinToString(NAME_SEPARATOR) { name -> name.replaceFirstChar { it.lowercase(Locale.ENGLISH) } }

    fun formatCallParameters(): String = parameters.joinToString { it.name }

    interface Builder {
        fun setMethodName(methodName: String): Builder
        fun addParameter(parameter: ParameterSpec): Builder
        fun setReturnTypeName(returnTypeName: TypeName): Builder
        fun setEnclosingClassName(enclosingClassName: ClassName): Builder
        fun setEnclosingElementKind(enclosingElementKind: KotlinElementKind): Builder
        fun setComponentClassName(componentClassName: ClassName): Builder
        fun addAnnotation(annotation: AnnotationSpec): Builder
        fun testFactoryMethod(): Builder
        fun build(): FactoryMethodModel
    }

    class BuilderImpl(
        private val declarationFile: KSFile
    ): Builder {
        private var methodName: String? = null
        private var parameters: MutableList<ParameterSpec> = mutableListOf()
        private var returnTypeName: TypeName? = null
        private var enclosingClassName: ClassName? = null
        private var enclosingElementKind: KotlinElementKind? = null
        private var componentClassName: ClassName = SingletonComponent::class.asClassName()
        private var annotations: MutableList<AnnotationSpec> = mutableListOf()
        private var isTest: Boolean = false

        override fun setMethodName(methodName: String): Builder {
            this.methodName = methodName
            return this
        }

        override fun addParameter(parameter: ParameterSpec): Builder {
            this.parameters += parameter
            return this
        }

        override fun setReturnTypeName(returnTypeName: TypeName): Builder {
            this.returnTypeName = returnTypeName
            return this
        }

        override fun setEnclosingClassName(enclosingClassName: ClassName): Builder {
            this.enclosingClassName = enclosingClassName
            return this
        }

        override fun setEnclosingElementKind(enclosingElementKind: KotlinElementKind): Builder {
            this.enclosingElementKind = enclosingElementKind
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

        override fun testFactoryMethod(): Builder {
            this.isTest = true
            return this
        }

        override fun build(): FactoryMethodModel =
            FactoryMethodModel(
                declarationFile = this.declarationFile,
                methodName = checkNotNull(this.methodName) { "Method name not set" },
                parameters = this.parameters,
                returnTypeName = checkNotNull(this.returnTypeName) { "Return type name not set" },
                enclosingClassName = this.enclosingClassName,
                enclosingElementKind = checkNotNull(this.enclosingElementKind) { "Enclosing element kind not set" },
                componentClassName = this.componentClassName,
                annotations = this.annotations,
                isTest = this.isTest
            )
    }

    companion object {
        private const val NAME_SEPARATOR = "_"
        private const val MODULE_NAME_FORMAT = "%s_%s_FactoryMethodsModule"
        private const val TEST_MODULE_NAME_FORMAT = "%s_%s_FactoryMethodsModule_Test"

        fun builder(declarationFile: KSFile): BuilderImpl =
            BuilderImpl(declarationFile)
    }
}
