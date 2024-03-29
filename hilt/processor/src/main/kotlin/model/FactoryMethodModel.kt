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

package it.czerwinski.android.hilt.processor.model

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element

data class FactoryMethodModel(
    val originatingElement: Element,
    val methodName: String,
    val isStatic: Boolean,
    val parameters: List<ParameterSpec>,
    val returnTypeName: TypeName,
    val enclosingClassName: ClassName,
    val enclosingElementKind: KotlinElementKind,
    val componentClassName: ClassName,
    val annotations: List<AnnotationSpec>,
    val isTest: Boolean
) {

    val groupingKey: ModuleGroupingKey =
        ModuleGroupingKey(enclosingClassName.packageName(), returnTypeName, componentClassName, isTest)

    fun formatCallParameters(): String = parameters.joinToString { it.name }
}
