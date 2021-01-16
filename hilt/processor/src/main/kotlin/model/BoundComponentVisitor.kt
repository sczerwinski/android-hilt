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

import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleAnnotationValueVisitor7

class BoundComponentVisitor : SimpleAnnotationValueVisitor7<Unit, String>() {

    var supertypeClassName: TypeName? = null
        private set

    var componentClassName: TypeName? = null
        private set

    override fun visitType(typeMirror: TypeMirror, key: String) {
        val className = TypeName.get(typeMirror)
        when (key) {
            SUPERTYPE_PARAMETER_NAME -> supertypeClassName = className
            COMPONENT_PARAMETER_NAME -> componentClassName = className
        }
    }

    companion object {
        private const val SUPERTYPE_PARAMETER_NAME = "supertype"
        private const val COMPONENT_PARAMETER_NAME = "component"
    }
}
