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

package it.czerwinski.android.hilt

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import javax.inject.Provider

@DisplayName("Tests for delegated properties")
class DelegatesTest {

    @Test
    @DisplayName(
        value = "GIVEN dagger lazily-computed value, " +
            "WHEN delegate property to the lazily-computed value, " +
            "THEN the property should be read"
    )
    fun daggerLazyDelegate() {
        val lazy = mockk<dagger.Lazy<String>>()
        every { lazy.get() } returns "Some text"

        val result by lazy

        assertEquals("Some text", result)
    }

    @Test
    @DisplayName(
        value = "GIVEN injection provider, " +
            "WHEN delegate property to the provider, " +
            "THEN the property should be read"
    )
    fun providerDelegate() {
        val provider = mockk<Provider<String>>()
        every { provider.get() } returns "Some text"

        val result by provider

        assertEquals("Some text", result)
    }
}
