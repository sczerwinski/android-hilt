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
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import dagger.Provides
import it.czerwinski.android.hilt.processor.model.FactoryMethodModel
import java.util.Locale
import javax.lang.model.element.Modifier

object FactoryMethodPoet {

    private const val PROVIDES_RETURN_FORMAT_STATIC = "return \$T.\$N(\$L);"
    private const val PROVIDES_RETURN_FORMAT_OBJECT = "return \$T.INSTANCE.\$N(\$L);"
    private const val PROVIDES_RETURN_FORMAT_INSTANCE = "return \$\$receiver.\$N(\$L);"

    fun generateProvidesMethodSpec(factoryMethod: FactoryMethodModel): MethodSpec =
        MethodSpec.methodBuilder(createProvidesMethodName(factoryMethod))
            .addAnnotation(Provides::class.java)
            .addAnnotations(factoryMethod.annotations)
            .addAnnotation(NonNull::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addReceiverParameterIfClass(factoryMethod)
            .addParameters(factoryMethod.parameters)
            .returns(factoryMethod.returnTypeName)
            .addProvidesMethodCode(factoryMethod)
            .build()

    private fun createProvidesMethodName(factoryMethod: FactoryMethodModel) =
        factoryMethod.enclosingClassName.simpleName().decapitalize(Locale.ENGLISH) +
            factoryMethod.methodName.capitalize(Locale.ENGLISH)

    private fun MethodSpec.Builder.addReceiverParameterIfClass(factoryMethod: FactoryMethodModel): MethodSpec.Builder =
        if (factoryMethod.enclosingElementKind.isClass) addReceiverParameter(factoryMethod)
        else this

    private fun MethodSpec.Builder.addReceiverParameter(factoryMethod: FactoryMethodModel): MethodSpec.Builder =
        addParameter(createReceiverParameterSpec(factoryMethod))

    private fun createReceiverParameterSpec(factoryMethod: FactoryMethodModel): ParameterSpec =
        ParameterSpec.builder(factoryMethod.enclosingClassName, "\$receiver")
            .addAnnotation(NonNull::class.java)
            .build()

    private fun MethodSpec.Builder.addProvidesMethodCode(factoryMethod: FactoryMethodModel): MethodSpec.Builder = when {
        factoryMethod.isStatic -> addProvidesMethodCodeStatic(factoryMethod)
        factoryMethod.enclosingElementKind.isObject -> addProvidesMethodCodeObject(factoryMethod)
        else -> addProvidesMethodCodeInstance(factoryMethod)
    }

    private fun MethodSpec.Builder.addProvidesMethodCodeStatic(
        factoryMethod: FactoryMethodModel
    ): MethodSpec.Builder =
        addCode(
            PROVIDES_RETURN_FORMAT_STATIC,
            factoryMethod.enclosingClassName,
            factoryMethod.methodName,
            factoryMethod.formatCallParameters()
        )

    private fun MethodSpec.Builder.addProvidesMethodCodeObject(
        factoryMethod: FactoryMethodModel
    ): MethodSpec.Builder =
        addCode(
            PROVIDES_RETURN_FORMAT_OBJECT,
            factoryMethod.enclosingClassName,
            factoryMethod.methodName,
            factoryMethod.formatCallParameters()
        )

    private fun MethodSpec.Builder.addProvidesMethodCodeInstance(
        factoryMethod: FactoryMethodModel
    ): MethodSpec.Builder =
        addCode(
            PROVIDES_RETURN_FORMAT_INSTANCE,
            factoryMethod.methodName,
            factoryMethod.formatCallParameters()
        )
}
