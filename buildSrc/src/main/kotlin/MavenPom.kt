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
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomCiManagement
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomIssueManagement
import org.gradle.api.publish.maven.MavenPomLicenseSpec
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.kotlin.dsl.provideDelegate

fun MavenPom.commonPom(project: Project) {
    val libDescription: String by project.rootProject
    val libUrl: String by project.rootProject
    val gitUrl: String by project.rootProject

    name.set(projectName(libDescription, project))
    description.set(libDescription)
    url.set(libUrl)

    scm { git(gitUrl) }
    licenses { apache() }
    developers { sczerwinski() }
    issueManagement { githubIssues(gitUrl) }
    ciManagement { githubActions(gitUrl) }
}

@Suppress("DefaultLocale")
private fun projectName(libDescription: String, project: Project) =
    "$libDescription: ${project.name.replace('-', ' ').capitalize()}"

private fun MavenPomScm.git(gitUrl: String) {
    connection.set("scm:git:$gitUrl.git")
    developerConnection.set("scm:git:$gitUrl.git")
    url.set(gitUrl)
}

private fun MavenPomLicenseSpec.apache() {
    license {
        name.set("The Apache Software License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0")
    }
}

private fun MavenPomDeveloperSpec.sczerwinski() {
    developer {
        id.set("sczerwinski")
        name.set("Slawomir Czerwinski")
        email.set("slawomir@czerwinski.it")
        url.set("https://czerwinski.it/")
    }
}

private fun MavenPomIssueManagement.githubIssues(gitUrl: String) {
    system.set("GitHub Issues")
    url.set("$gitUrl/issues")
}

private fun MavenPomCiManagement.githubActions(gitUrl: String) {
    system.set("GitHub Actions")
    url.set("$gitUrl/actions")
}
