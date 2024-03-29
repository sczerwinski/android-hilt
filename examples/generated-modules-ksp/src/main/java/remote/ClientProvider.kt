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
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.MessageLengthLimitingLogger
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import it.czerwinski.android.hilt.annotations.FactoryMethod
import javax.inject.Singleton

object ClientProvider {

    private const val TIMEOUT = 120_000L

    @FactoryMethod
    @Singleton
    fun getClient(): HttpClient = HttpClient(Android) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "czerwinski.it"
                path("api/")
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
            connectTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = MessageLengthLimitingLogger(delegate = AndroidLogger)
            level = LogLevel.BODY
        }
    }

}
