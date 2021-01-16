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

data class Binding(
    val annotatedClassName: ClassName,
    val supertypeClassName: ClassName,
    val componentClassName: ClassName,
    val annotations: List<AnnotationSpec>
) {

    val groupingKey: ModuleGroupingKey = ModuleGroupingKey(annotatedClassName.packageName(), componentClassName)
}
