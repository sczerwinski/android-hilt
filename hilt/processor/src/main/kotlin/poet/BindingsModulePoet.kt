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
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import it.czerwinski.android.hilt.processor.model.Binding
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

object BindingsModulePoet {

    fun generateModule(
        packageName: String,
        componentClassName: ClassName,
        bindings: List<Binding>,
        filer: Filer
    ) {
        JavaFile.builder(
            packageName,
            generateBindingsModuleInterface(packageName, componentClassName, bindings)
        )
            .build()
            .writeTo(filer)
    }

    private fun generateBindingsModuleInterface(
        packageName: String,
        componentClassName: ClassName,
        bindings: List<Binding>
    ): TypeSpec =
        TypeSpec.interfaceBuilder(ClassName.get(packageName, "${componentClassName.simpleName()}_BindingsModule"))
            .addAnnotation(Module::class.java)
            .addAnnotation(
                AnnotationSpec.builder(InstallIn::class.java)
                    .addMember("value", "\$T.class", componentClassName)
                    .build()
            )
            .addModifiers(Modifier.PUBLIC)
            .addMethods(bindings.map { binding -> generateBindingMethod(binding) })
            .build()

    private fun generateBindingMethod(binding: Binding): MethodSpec =
        MethodSpec.methodBuilder("bind${binding.annotatedClassName.simpleName()}")
            .addAnnotation(Binds::class.java)
            .addAnnotations(binding.annotations)
            .addAnnotation(NonNull::class.java)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(
                ParameterSpec.builder(binding.annotatedClassName, "implementation")
                    .addAnnotation(NonNull::class.java)
                    .build()
            )
            .returns(binding.supertypeClassName)
            .build()
}
