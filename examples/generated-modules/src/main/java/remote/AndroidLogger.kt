package it.czerwinski.android.hilt.examples.generated.remote

import android.util.Log
import io.ktor.client.plugins.logging.Logger

object AndroidLogger : Logger {

    private const val TAG = "HttpClient"

    override fun log(message: String) {
        for (line in message.lines()) {
            Log.d(TAG, line)
        }
    }
}
