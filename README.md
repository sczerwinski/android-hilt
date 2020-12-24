# Extensions for Dagger Hilt

## Hilt Extensions

[![Maven Central](https://img.shields.io/maven-central/v/it.czerwinski.android.hilt/hilt-extensions)][hilt-extensions-release]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.hilt/hilt-extensions?server=https%3A%2F%2Foss.sonatype.org)][hilt-extensions-snapshot]

<details>
  <summary>Kotlin</summary>

  ```kotlin
  dependencies {
      implementation("com.google.dagger:hilt-android:2.30.1-alpha")
      implementation("it.czerwinski.android.hilt:hilt-extensions:[VERSION]")
      kapt("it.czerwinski.android.hilt:hilt-processor:[VERSION]")
  }
  ```
</details>

<details>
  <summary>Groovy</summary>

  ```groovy
  dependencies {
      implementation 'com.google.dagger:hilt-android:2.30.1-alpha'
      implementation 'it.czerwinski.android.hilt:hilt-extensions:[VERSION]'
      kapt 'it.czerwinski.android.hilt:hilt-processor:[VERSION]'
  }
  ```
</details>

### Generating Modules With Primary Implementations

#### `@Primary`
Marks primary implementation of the given superclass.

For example:
```kotlin
@Qualifier
annotation class Offline

interface Repository

@Primary(
    superclass = Repository::class,
    component = SingletonComponent::class
)
class RepositoryA @Inject constructor() : Repository

@Primary(
    superclass = Repository::class,
    component = SingletonComponent::class,
    scope = Singleton::class
)
class RepositoryB @Inject constructor() : Repository

@Primary(
    superclass = Repository::class,
    component = SingletonComponent::class,
    qualifier = Offline::class
)
class RepositoryC @Inject constructor() : Repository
```
will generate module:
```java
@Module
@InstallIn(SingletonComponent.class)
public interface SingletonComponentPrimaryBindingsModule {

    @Binds
    Repository bindRepositoryA(RepositoryA implementation);

    @Binds
    @Singleton
    Repository bindRepositoryB(RepositoryB implementation);

    @Binds
    @Offline
    Repository bindRepositoryC(RepositoryC implementation);
}
```


[ci-build]: https://github.com/sczerwinski/android-hilt/actions?query=workflow%3ABuild
[hilt-extensions-release]: https://repo1.maven.org/maven2/it/czerwinski/android/hilt/hilt-extensions/
[hilt-extensions-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/hilt/hilt-extensions/
