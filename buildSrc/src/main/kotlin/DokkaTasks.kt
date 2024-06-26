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

import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.internal.KaptTask

fun DokkaTask.setUpJavadocTask(project: Project) {
    setUpDokkaTask(project, outputDirectoryName = "javadoc")
}

fun DokkaTask.setUpJekyllTask(project: Project) {
    setUpDokkaTask(project, outputDirectoryName = "jekyll")
}

private fun DokkaTask.setUpDokkaTask(project: Project, outputDirectoryName: String) {
    val kaptTasks = project.tasks.withType<KaptTask>().toList()
    dependsOn(*kaptTasks.toTypedArray())
    val libDescription: String by project.rootProject
    outputDirectory.set(project.layout.buildDirectory.asFile.get().resolve(outputDirectoryName))
    dokkaSourceSets {
        named("main") {
            moduleName.set(libDescription)
            includes.from(project.files("packages.md"))
        }
    }
}
