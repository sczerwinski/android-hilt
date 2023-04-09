package it.czerwinski.android.hilt.fragment.testing.tests.test

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltAndroidJUnitRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
