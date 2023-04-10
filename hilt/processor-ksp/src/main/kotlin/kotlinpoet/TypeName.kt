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
import com.squareup.kotlinpoet.Dynamic
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.WildcardTypeName

private const val LAMBDA_TYPE_NAME = "Lambda"
private const val DYNAMIC_TYPE_NAME = "Dynamic"
private const val WILDCARD_TYPE_NAME = "Wildcard"

val TypeName.simpleName: String
    get() = when (this) {
        is ClassName -> simpleName
        is ParameterizedTypeName -> rawType.simpleName
        is LambdaTypeName -> "${returnType.simpleName}$LAMBDA_TYPE_NAME"
        Dynamic -> DYNAMIC_TYPE_NAME
        is TypeVariableName -> name
        is WildcardTypeName -> WILDCARD_TYPE_NAME
    }

val TypeName.simpleNames: List<String>
    get() = when (this) {
        is ClassName -> simpleNames
        is ParameterizedTypeName -> rawType.simpleNames
        is LambdaTypeName -> returnType.simpleNames + LAMBDA_TYPE_NAME
        Dynamic,
        is TypeVariableName,
        is WildcardTypeName -> listOf(simpleName)
    }
