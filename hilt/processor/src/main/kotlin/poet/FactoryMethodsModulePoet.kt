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

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import it.czerwinski.android.hilt.processor.model.FactoryMethodModel
import it.czerwinski.android.hilt.processor.model.ModuleGroupingKey
import javax.annotation.processing.Filer

object FactoryMethodsModulePoet : BaseModulePoet() {

    private const val MODULE_NAME_FORMAT = "%s_%s_FactoryMethodsModule"

    fun generateModule(
        groupingKey: ModuleGroupingKey,
        factoryMethods: List<FactoryMethodModel>,
        filer: Filer?
    ) {
        JavaFile.builder(
            groupingKey.packageName,
            generateFactoryMethodModuleClass(groupingKey, factoryMethods)
        )
            .build()
            .writeTo(filer)
    }

    private fun generateFactoryMethodModuleClass(
        groupingKey: ModuleGroupingKey,
        factoryMethods: List<FactoryMethodModel>
    ): TypeSpec =
        TypeSpec.classBuilder(createModuleClassName(groupingKey))
            .addCommonModuleSetup(groupingKey.componentClassName)
            .addMethods(factoryMethods.map { binding -> FactoryMethodPoet.generateProvidesMethodSpec(binding) })
            .build()

    private fun createModuleClassName(groupingKey: ModuleGroupingKey): ClassName =
        ClassName.get(
            groupingKey.packageName,
            MODULE_NAME_FORMAT.format(
                groupingKey.returnedTypeName.toModuleNamePrefix(),
                groupingKey.componentClassName.simpleName()
            )
        )
}
