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

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for generated bindings module")
class BindingsTest {

    private val repositoryA = Repository.RepositoryA()
    private val repositoryB = RepositoryB()
    private val repositoryC = RepositoryC()
    private val repositoryD = RepositoryD()

    @Test
    @DisplayName(
        value = "GIVEN interface with bindings for different scopes and qualifiers, " +
            "WHEN generate bindings module, " +
            "THEN generated module should contain bindings for all implementations"
    )
    fun singletonComponentBindingsModule() {
        val module = mockk<Repository_SingletonComponent_BindingsModule>()

        every { module.bindRepositoryA(any()) } returnsArgument 0
        every { module.bindRepositoryB(any()) } returnsArgument 0
        every { module.bindRepositoryC(any()) } returnsArgument 0
        every { module.bindRepositoryD(any()) } returnsArgument 0

        assertSame(repositoryA, module.bindRepositoryA(repositoryA))
        assertSame(repositoryB, module.bindRepositoryB(repositoryB))
        assertSame(repositoryC, module.bindRepositoryC(repositoryC))
        assertSame(repositoryD, module.bindRepositoryD(repositoryD))
    }
}
