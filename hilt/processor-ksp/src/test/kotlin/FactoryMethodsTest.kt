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

package it.czerwinski.android.hilt.processor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for factory methods modules generated with KSP")
class FactoryMethodsTest {

    @Test
    @DisplayName(
        value = "GIVEN a factory method in nested object, " +
            "WHEN generate factory methods module, " +
            "THEN generated method returns result for the method in nested object"
    )
    fun factoryMethodInNestedObject() {
        val module = Result_SingletonComponent_FactoryMethodsModule

        val result = module.result_factory_create()

        assertEquals(Result.Factory.create(), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN a factory method in companion object, " +
                "WHEN generate factory methods module, " +
                "THEN generated method returns result for the method in companion object"
    )
    fun factoryMethodInCompanionObject() {
        val module = Result_SingletonComponent_FactoryMethodsModule

        val result = module.result_companion_create()

        assertEquals(Result.create(), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN a factory method in file, " +
            "WHEN generate factory methods module, " +
            "THEN generated method returns result for the method in file"
    )
    fun factoryMethodInFile() {
        val module = Result_SingletonComponent_FactoryMethodsModule

        val result = module.factoryMethodsKt_createResult()

        assertEquals(createResult(), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN a factory method in object, " +
            "WHEN generate factory methods module, " +
            "THEN generated method returns result for the method in object"
    )
    fun factoryMethodInObject() {
        val module = Result_SingletonComponent_FactoryMethodsModule

        val result = module.factoryObject_createResult()

        assertEquals(FactoryObject.createResult(), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN a static factory method in object, " +
            "WHEN generate factory methods module, " +
            "THEN generated method returns result for the static method in object"
    )
    fun staticFactoryMethodInObject() {
        val module = Result_SingletonComponent_FactoryMethodsModule

        val result = module.factoryObject_createResultStatic()

        assertEquals(FactoryObject.createResultStatic(), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN a factory method in class, " +
            "WHEN generate factory methods module, " +
            "THEN generated method returns result for the method in class"
    )
    fun factoryMethodInClass() {
        val module = Result_SingletonComponent_FactoryMethodsModule
        val instance = Factory()

        val result = module.factory_createResult(instance)

        assertEquals(instance.createResult(), result)
    }

    @Test
    @DisplayName(
        value = "GIVEN a factory method in class with parameters, " +
            "WHEN generate factory methods module, " +
            "THEN generated method returns result for the method in class with parameters"
    )
    fun factoryMethodInClassWithParams() {
        val module = Result_SingletonComponent_FactoryMethodsModule
        val instance = Factory()

        val result = module.factory_createResult(instance, 123, "test")

        assertEquals(Result(123, "test"), result)
    }
}
