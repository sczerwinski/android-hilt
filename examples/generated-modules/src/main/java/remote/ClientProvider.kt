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

package it.czerwinski.android.hilt.examples.generated.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.ANDROID
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import it.czerwinski.android.hilt.annotations.FactoryMethod
import javax.inject.Singleton

object ClientProvider {

    private const val TIMEOUT = 120_000

    @FactoryMethod
    @Singleton
    fun getClient(): HttpClient = HttpClient(Android) {
        engine {
            connectTimeout = TIMEOUT
            socketTimeout = TIMEOUT
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
    }
}
