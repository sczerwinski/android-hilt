/*
 * Copyright 2020-2023 Slawomir Czerwinski
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
 */

package it.czerwinski.android.hilt.processor

import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.annotations.Bound
import it.czerwinski.android.hilt.annotations.BoundTo
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

interface Repository {

    @BoundTo(supertype = Repository::class)
    class RepositoryA @Inject constructor() : Repository
}

@BoundTo(supertype = Repository::class, component = SingletonComponent::class)
@Singleton
class RepositoryB @Inject constructor() : Repository

@Bound
@Named("offline")
class RepositoryC @Inject constructor() : Repository

@Bound(component = SingletonComponent::class)
@Named("debug")
class RepositoryD @Inject constructor() : Repository
