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

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import dagger.Module
import dagger.hilt.InstallIn
import it.czerwinski.android.hilt.processor.model.ModuleGroupingKey
import it.czerwinski.android.hilt.processor.rawType
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

open class BaseModulePoet {

    protected fun TypeName.toModuleNamePrefix(): String {
        return box().rawType().simpleNames().joinToString(SIMPLE_NAME_SEPARATOR)
    }

    protected fun TypeSpec.Builder.addCommonModuleSetup(
        groupingKey: ModuleGroupingKey,
        replacesClassName: ClassName
    ): TypeSpec.Builder = this
        .addAnnotation(moduleClassName)
        .addAnnotation(
            if (groupingKey.isTest) createTestInstallInAnnotationSpec(groupingKey.componentClassName, replacesClassName)
            else createInstallInAnnotationSpec(groupingKey.componentClassName)
        )
        .addModifiers(Modifier.PUBLIC)

    private fun createInstallInAnnotationSpec(componentClassName: ClassName): AnnotationSpec =
        AnnotationSpec.builder(installInClassName)
            .addMember(COMPONENT_MEMBER_NAME, COMPONENT_MEMBER_FORMAT, componentClassName)
            .build()

    private fun createTestInstallInAnnotationSpec(
        componentClassName: ClassName,
        replacesClassName: ClassName
    ): AnnotationSpec =
        AnnotationSpec.builder(testInstallInClassName)
            .addMember(TEST_COMPONENT_MEMBER_NAME, COMPONENT_MEMBER_FORMAT, componentClassName)
            .addMember(TEST_REPLACES_MEMBER_NAME, REPLACES_MEMBER_FORMAT, replacesClassName)
            .build()

    protected fun TypeSpec.Builder.addOriginatingElements(originatingElements: Iterable<Element>): TypeSpec.Builder =
        apply {
            for (element in originatingElements) {
                addOriginatingElement(element)
            }
        }

    companion object {

        private const val SIMPLE_NAME_SEPARATOR = "_"

        private const val COMPONENT_MEMBER_NAME = "value"
        private const val TEST_COMPONENT_MEMBER_NAME = "components"
        private const val TEST_REPLACES_MEMBER_NAME = "replaces"
        private const val COMPONENT_MEMBER_FORMAT = "\$T.class"
        private const val REPLACES_MEMBER_FORMAT = "\$T.class"

        private val moduleClassName = ClassName.get(Module::class.java)
        private val installInClassName = ClassName.get(InstallIn::class.java)
        private val testInstallInClassName =
            ClassName.get("dagger.hilt.testing", "TestInstallIn")
    }
}
