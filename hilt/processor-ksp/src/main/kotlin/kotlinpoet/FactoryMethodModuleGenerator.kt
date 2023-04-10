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

package it.czerwinski.android.hilt.processor.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import dagger.Provides
import it.czerwinski.android.hilt.processor.model.FactoryMethodModel
import it.czerwinski.android.hilt.processor.model.KotlinElementKind

class FactoryMethodModuleGenerator : BaseGenerator<FactoryMethodModel>() {

    override fun buildFunction(model: FactoryMethodModel): FunSpec =
        FunSpec.builder(name = model.provideMethodName)
            .addAnnotation(Provides::class)
            .addAnnotations(model.annotations)
            .addFactoryParameterIfClass(model)
            .addParameters(model.parameters)
            .returns(model.returnTypeName)
            .addCode(buildProvideMethodCode(model))
            .build()

    private fun FunSpec.Builder.addFactoryParameterIfClass(model: FactoryMethodModel): FunSpec.Builder =
        if (model.enclosingElementKind.isClass && model.enclosingClassName != null) {
            addParameter(createFactoryParameterSpec(model.enclosingClassName))
        }
        else this

    private fun createFactoryParameterSpec(className: ClassName): ParameterSpec =
        ParameterSpec.builder(FACTORY_PARAMETER_NAME, className)
            .build()

    private fun buildProvideMethodCode(model: FactoryMethodModel): CodeBlock =
        when (model.enclosingElementKind) {
            KotlinElementKind.CLASS ->
                CodeBlock.of(
                    PROVIDES_RETURN_FORMAT_INSTANCE, model.methodName,
                    model.formatCallParameters()
                )

            KotlinElementKind.OBJECT ->
                CodeBlock.of(
                    PROVIDES_RETURN_FORMAT_OBJECT,
                    model.enclosingClassName, model.methodName, model.formatCallParameters()
                )

            KotlinElementKind.FILE ->
                CodeBlock.of(
                    PROVIDES_RETURN_FORMAT_FILE,
                    model.methodName, model.formatCallParameters()
                )
        }

    companion object {
        private const val FACTORY_PARAMETER_NAME = "factory"
        private const val PROVIDES_RETURN_FORMAT_INSTANCE = "return factory.%N(%L);"
        private const val PROVIDES_RETURN_FORMAT_OBJECT = "return %T.%N(%L);"
        private const val PROVIDES_RETURN_FORMAT_FILE = "return %N(%L);"
    }
}
