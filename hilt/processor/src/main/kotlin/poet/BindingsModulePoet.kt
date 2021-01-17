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

package it.czerwinski.android.hilt.processor.poet

import androidx.annotation.NonNull
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import it.czerwinski.android.hilt.processor.model.Binding
import it.czerwinski.android.hilt.processor.model.ModuleGroupingKey
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

object BindingsModulePoet : BaseModulePoet() {

    private const val MODULE_NAME_FORMAT = "%s_%s_BindingsModule"
    private const val TEST_MODULE_NAME_FORMAT = "%s_%s_BindingsModule_Test"
    private const val BINDS_METHOD_NAME_FORMAT = "bind%s"
    private const val BINDS_METHOD_PARAM_NAME = "implementation"

    fun generateModule(
        groupingKey: ModuleGroupingKey,
        bindings: List<Binding>,
        filer: Filer
    ) {
        JavaFile.builder(
            groupingKey.packageName,
            generateBindingsModuleInterface(groupingKey, bindings)
        )
            .build()
            .writeTo(filer)
    }

    private fun generateBindingsModuleInterface(
        groupingKey: ModuleGroupingKey,
        bindings: List<Binding>
    ): TypeSpec =
        TypeSpec.interfaceBuilder(createModuleClassName(groupingKey))
            .addCommonModuleSetup(groupingKey, createModuleClassName(groupingKey.copy(isTest = false)))
            .addMethods(bindings.map { binding -> generateBindsMethodSpec(binding) })
            .build()

    private fun createModuleClassName(groupingKey: ModuleGroupingKey): ClassName =
        ClassName.get(
            groupingKey.packageName,
            (if (groupingKey.isTest) TEST_MODULE_NAME_FORMAT else MODULE_NAME_FORMAT).format(
                groupingKey.returnedTypeName.toModuleNamePrefix(),
                groupingKey.componentClassName.simpleName()
            )
        )

    private fun generateBindsMethodSpec(binding: Binding): MethodSpec =
        MethodSpec.methodBuilder(createBindsMethodName(binding.annotatedClassName))
            .addAnnotation(Binds::class.java)
            .addAnnotations(binding.annotations)
            .addAnnotation(NonNull::class.java)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(generateBindsMethodParamSpec(binding))
            .returns(binding.supertypeClassName)
            .build()

    private fun createBindsMethodName(annotatedClassName: ClassName): String =
        BINDS_METHOD_NAME_FORMAT.format(annotatedClassName.simpleName())

    private fun generateBindsMethodParamSpec(binding: Binding): ParameterSpec =
        ParameterSpec.builder(binding.annotatedClassName, BINDS_METHOD_PARAM_NAME)
            .addAnnotation(NonNull::class.java)
            .build()
}
