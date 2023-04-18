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
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

fun PublicationContainer.registerAarPublication(project: Project, componentName: String = "release") {
    register("libAar", MavenPublication::class) {
        from(project.components[componentName])
        artifact(project.tasks["javadocJar"])

        artifactId = "${project.parent?.name}-${project.name}"
        groupId = "${project.group}"
        version = "${project.version}"

        pom { commonPom(project) }
    }
}

fun PublicationContainer.registerJarPublication(project: Project, componentName: String = "java") {
    register("libJar", MavenPublication::class) {
        from(project.components[componentName])
        artifact(project.tasks["javadocJar"])
        artifact(project.tasks["sourcesJar"])

        artifactId = "${project.parent?.name}-${project.name}"
        groupId = "${project.group}"
        version = "${project.version}"

        pom { commonPom(project) }
    }
}
