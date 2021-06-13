package it.czerwinski.android.hilt.processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName

fun TypeName.rawType(): ClassName {
    return when (this) {
        is ClassName -> this
        is ParameterizedTypeName -> rawType
        else -> throw IllegalArgumentException("Unsupported type $this")
    }
}
