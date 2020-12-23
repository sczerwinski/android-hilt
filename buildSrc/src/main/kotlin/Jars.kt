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

import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.TaskContainerScope
import org.gradle.kotlin.dsl.create
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.File

fun TaskContainerScope.createJavadocJar(task: TaskProvider<DokkaTask>): Jar =
    create<Jar>("javadocJar") {
        dependsOn.add(task)
        archiveClassifier.set("javadoc")
        from(task)
    }

fun TaskContainerScope.createSourcesJar(files: Set<File>): Jar =
    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(files)
    }
