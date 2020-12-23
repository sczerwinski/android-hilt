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
import org.gradle.api.artifacts.dsl.RepositoryHandler

private const val SNAPSHOTS_REPOSITORY_URL = "https://oss.sonatype.org/content/repositories/snapshots/"
private const val RELEASE_REPOSITORY_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

private const val ENV_SONATYPE_USERNAME = "SONATYPE_USERNAME"
private const val ENV_SONATYPE_PASSWORD = "SONATYPE_PASSWORD"

fun RepositoryHandler.sonatype(project: Project) {
    val isSnapshot = project.version.toString().endsWith("SNAPSHOT")
    maven {
        if (System.getenv(ENV_SONATYPE_USERNAME) != null) {
            url = project.uri(if (isSnapshot) SNAPSHOTS_REPOSITORY_URL else RELEASE_REPOSITORY_URL)
            credentials {
                username = System.getenv(ENV_SONATYPE_USERNAME)
                password = System.getenv(ENV_SONATYPE_PASSWORD)
            }
        } else {
            url = project.uri("${project.buildDir}/maven")
        }
    }
}
