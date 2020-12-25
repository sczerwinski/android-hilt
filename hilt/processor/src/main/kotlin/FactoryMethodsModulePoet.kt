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

package it.czerwinski.android.hilt.processor

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import java.util.Locale
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

object FactoryMethodsModulePoet {
    fun generateModule(
        packageName: String,
        componentClassName: ClassName,
        factoryMethods: List<FactoryMethodModel>,
        filer: Filer?
    ) {
        JavaFile.builder(
            packageName,
            generateFactoryMethodModuleClass(packageName, componentClassName, factoryMethods)
        )
            .build()
            .writeTo(filer)
    }

    private fun generateFactoryMethodModuleClass(
        packageName: String,
        componentClassName: ClassName,
        factoryMethods: List<FactoryMethodModel>
    ): TypeSpec =
        TypeSpec.classBuilder(ClassName.get(packageName, "${componentClassName.simpleName()}_FactoryMethodsModule"))
            .addAnnotation(Module::class.java)
            .addAnnotation(
                AnnotationSpec.builder(InstallIn::class.java)
                    .addMember("value", "\$T.class", componentClassName)
                    .build()
            )
            .addMethods(factoryMethods.map { binding -> generateProvideMethod(binding) })
            .addModifiers(Modifier.PUBLIC)
            .build()

    private fun generateProvideMethod(factoryMethod: FactoryMethodModel): MethodSpec =
        MethodSpec.methodBuilder(
            factoryMethod.enclosingClassName.simpleName().decapitalize(Locale.getDefault()) +
                factoryMethod.methodName.capitalize(Locale.getDefault())
        )
            .addAnnotation(Provides::class.java)
            .addAnnotations(factoryMethod.annotations)
            .addModifiers(Modifier.PUBLIC)
            .apply {
                if (factoryMethod.enclosingElementKind == KotlinElementKind.CLASS) {
                    addParameter(factoryMethod.enclosingClassName, "\$receiver")
                }
            }
            .addParameters(factoryMethod.parameters)
            .returns(factoryMethod.returnTypeName)
            .apply {
                when {
                    factoryMethod.isStatic -> {
                        addCode(
                            "return \$T.\$N(\$L);",
                            factoryMethod.enclosingClassName,
                            factoryMethod.methodName,
                            factoryMethod.parameters.joinToString { it.name }
                        )
                    }
                    factoryMethod.enclosingElementKind == KotlinElementKind.OBJECT -> {
                        addCode(
                            "return \$T.INSTANCE.\$N(\$L);",
                            factoryMethod.enclosingClassName,
                            factoryMethod.methodName,
                            factoryMethod.parameters.joinToString { it.name }
                        )
                    }
                    else -> {
                        addCode(
                            "return \$\$receiver.\$N(\$L);",
                            factoryMethod.methodName,
                            factoryMethod.parameters.joinToString { it.name }
                        )
                    }
                }
            }
            .build()
}
