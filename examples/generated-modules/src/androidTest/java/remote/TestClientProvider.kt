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

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.MessageLengthLimitingLogger
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import it.czerwinski.android.hilt.annotations.TestFactoryMethod
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Singleton

object TestClientProvider {

    private val context: Context get() = InstrumentationRegistry.getInstrumentation().context

    private val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    @TestFactoryMethod
    @Singleton
    fun getClient(): HttpClient = HttpClient(
        MockEngine { request ->
            try {
                @Suppress("BlockingMethodInNonBlockingContext")
                val responseBody = context.assets.open(request.url.fullPath.removePrefix(File.separator))
                    .use { input -> input.bufferedReader().readText() }
                respond(content = responseBody, headers = responseHeaders)
            } catch (exception: FileNotFoundException) {
                respondError(
                    status = HttpStatusCode.NotFound,
                    content = exception.message ?: HttpStatusCode.NotFound.description
                )
            }
        }
    ) {
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
