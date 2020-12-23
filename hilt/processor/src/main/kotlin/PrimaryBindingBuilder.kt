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

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleAnnotationValueVisitor7

class PrimaryBindingBuilder(
    private val annotatedClassName: ClassName
) : SimpleAnnotationValueVisitor7<Unit, String>() {

    private var superclassClassName: TypeName? = null
    private var componentClassName: TypeName? = null
    private var scopeClassName: TypeName? = null
    private var qualifierClassName: TypeName? = null

    override fun visitType(typeMirror: TypeMirror, key: String) {
        val className = TypeName.get(typeMirror)
        when (key) {
            "superclass" -> superclassClassName = className
            "component" -> componentClassName = className
            "scope" -> scopeClassName = className
            "qualifier" -> qualifierClassName = className
        }
    }

    fun build(): PrimaryBinding = PrimaryBinding(
        annotatedClassName = annotatedClassName,
        superclassClassName = requireNotNull(superclassClassName as? ClassName),
        componentClassName = requireNotNull(componentClassName as? ClassName),
        scopeClassName = scopeClassName as? ClassName,
        qualifierClassName = qualifierClassName as? ClassName,
    )
}
