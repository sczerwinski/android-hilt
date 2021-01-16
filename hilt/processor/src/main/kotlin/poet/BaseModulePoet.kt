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
import com.squareup.javapoet.TypeSpec
import dagger.Module
import dagger.hilt.InstallIn
import javax.lang.model.element.Modifier

open class BaseModulePoet {

    protected fun TypeSpec.Builder.addCommonModuleSetup(componentClassName: ClassName): TypeSpec.Builder = this
        .addAnnotation(moduleClassName)
        .addAnnotation(createInstallInAnnotationSpec(componentClassName))
        .addModifiers(Modifier.PUBLIC)

    private fun createInstallInAnnotationSpec(componentClassName: ClassName): AnnotationSpec =
        AnnotationSpec.builder(installInClassName)
            .addMember(COMPONENT_MEMBER_NAME, COMPONENT_MEMBER_FORMAT, componentClassName)
            .build()

    companion object {

        private const val COMPONENT_MEMBER_NAME = "value"
        private const val COMPONENT_MEMBER_FORMAT = "\$T.class"

        private val moduleClassName = ClassName.get(Module::class.java)
        private val installInClassName = ClassName.get(InstallIn::class.java)
    }
}
