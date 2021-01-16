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
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element

private val scopeAnnotationClass = Scope::class.java
private val qualifierAnnotationClass = Qualifier::class.java

fun Element.className(): ClassName = TypeName.get(asType()) as ClassName

inline fun <reified T> Element.findAnnotationOfType(): AnnotationMirror? =
    findAnnotationOfType(T::class.java)

fun <T> Element.findAnnotationOfType(annotationType: Class<T>): AnnotationMirror? =
    annotationMirrors.find { annotationMirror ->
        annotationMirror.annotationType.asElement().isOfType(annotationType)
    }

fun Element.isOfType(type: Class<*>): Boolean =
    packageName() == type.`package`.name && simpleName() == type.simpleName

fun Element.packageName(): String = className().packageName()

fun Element.simpleName(): String = simpleName.toString()

fun Element.scopesAndQualifiers(): List<AnnotationMirror> = annotationMirrors.filter { annotationMirror ->
    annotationMirror.annotationType.asElement().annotationMirrors.any {
        val annotationElement = it.annotationType.asElement()
        annotationElement.isOfType(scopeAnnotationClass) || annotationElement.isOfType(qualifierAnnotationClass)
    }
}
