[![Build](https://github.com/sczerwinski/android-hilt/workflows/Build/badge.svg)][ci-build]

# Extensions for Dagger Hilt

## Hilt Extensions

[![Maven Central](https://img.shields.io/maven-central/v/it.czerwinski.android.hilt/hilt-extensions)][hilt-extensions-release]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.hilt/hilt-extensions?server=https%3A%2F%2Foss.sonatype.org)][hilt-extensions-snapshot]

<details>
  <summary>Kotlin</summary>

  ```kotlin
  dependencies {
      implementation("com.google.dagger:hilt-android:2.33-beta")
      implementation("it.czerwinski.android.hilt:hilt-extensions:[VERSION]")
      kapt("it.czerwinski.android.hilt:hilt-processor:[VERSION]")
  }
  ```
</details>

<details>
  <summary>Groovy</summary>

  ```groovy
  dependencies {
      implementation 'com.google.dagger:hilt-android:2.33-beta'
      implementation 'it.czerwinski.android.hilt:hilt-extensions:[VERSION]'
      kapt 'it.czerwinski.android.hilt:hilt-processor:[VERSION]'
  }
  ```
</details>

### Property Delegation

With this library, it is possible to delegate properties to additional objects.

#### `dagger.Lazy`

```kotlin
val lazy: dagger.Lazy<String>

val property: String by lazy
```

#### `javax.inject.Provider`

```kotlin
val intProvider: Provider<Int>

val property: Int by intProvider
```

### Generating Hilt Modules

#### `@Bound` and `@BoundTo`
Marks implementation bound to the given supertype in the given component.

`@Bound` annotation (added in v1.1.0) works exactly like `@BoundTo` annotation,
but it implicitly uses the direct supertype of the annotated class. It may only
annotate classes having exactly one direct supertype, excluding `java.lang.Object`. 

For example:
```kotlin
interface Repository

@BoundTo(supertype = Repository::class, component = SingletonComponent::class)
class RepositoryA @Inject constructor() : Repository

@BoundTo(supertype = Repository::class, component = SingletonComponent::class)
@Singleton
class RepositoryB @Inject constructor() : Repository

@Bound(component = SingletonComponent::class)
@Named("offline")
class RepositoryC @Inject constructor() : Repository
```
will generate module:
```java
@Module
@InstallIn(SingletonComponent.class)
public interface Repository_SingletonComponent_BindingsModule {

    @Binds
    Repository bindRepositoryA(RepositoryA implementation);

    @Binds
    @Singleton
    Repository bindRepositoryB(RepositoryB implementation);

    @Binds
    @Named("offline")
    Repository bindRepositoryC(RepositoryC implementation);
}
```

Since release 1.1.0, component property is optional, and set to `SingletonComponent` by default.

#### `@FactoryMethod`
Marks factory method for the class returned by the annotated function.

For example, for a Room database:
```kotlin
@Database(
    entities = [
        User::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    @FactoryMethod(component = SingletonComponent::class)
    @Singleton
    abstract fun usersDao(): UsersDao
}
```
and a database factory:
```kotlin
interface DatabaseFactory {

    @FactoryMethod(component = SingletonComponent::class)
    @Singleton
    fun createDatabase(): AppDatabase
}
```
and a database factory provider:
```kotlin
object DatabaseFactoryProvider {

    @FactoryMethod(component = SingletonComponent::class)
    fun createDatabaseFactory(
        @ApplicationContext context: Context
    ): DatabaseFactory =
        if (BuildConfig.DEBUG) TestDatabaseFactory(context)
        else ProductionDatabaseFactory(context)
}
```
annotation processor will generate modules:
```java
@Module
@InstallIn(SingletonComponent.class)
public class UsersDao_SingletonComponent_FactoryMethodsModule {
    @Provides
    @Singleton
    public UsersDao appDatabase_usersDao(AppDatabase $receiver) {
        return $receiver.usersDao();
    }
}
```
```java
@Module
@InstallIn(SingletonComponent.class)
public class AppDatabase_SingletonComponent_FactoryMethodsModule {
    @Provides
    @Singleton
    public AppDatabase databaseFactory_createDatabase(DatabaseFactory $receiver) {
        return $receiver.createDatabase();
    }
}
```
```java
@Module
@InstallIn(SingletonComponent.class)
public class DatabaseFactory_SingletonComponent_FactoryMethodsModule {
    @Provides
    public DatabaseFactory databaseFactoryProvider_createDatabaseFactory(
            @ApplicationContext Context context) {
        return DatabaseFactoryProvider.INSTANCE.createDatabaseFactory(context);
    }
}
```

Since release 1.1.0, component property is optional, and set to `SingletonComponent` by default.

#### `@TestBound`, `@TestBoundTo` and `@TestFactoryMethod`

Version 1.1.0 introduces additional test annotations that can be used to generate modules
annotated with [`@TestInstallIn`][TestInstallIn], instead of `@InstallIn`:
- `@TestBound` (instead of `@Bound`)
- `@TestBoundTo` (instead of `@BoundTo`)
- `@TestFactoryMethod` (instead of `@FactoryMethod`)

Test module generated using `@TestBound` and/or `@TestBoundTo` will replace the module generated using
`@Bound` and/or `@BoundTo`.

Test module generated using `@TestFactoryMethod` will replace the module generated with `@FactoryMethod`.

## Hilt Testing Extensions

[![Maven Central](https://img.shields.io/maven-central/v/it.czerwinski.android.hilt/hilt-fragment-testing)][hilt-fragment-testing-release]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.hilt/hilt-fragment-testing?server=https%3A%2F%2Foss.sonatype.org)][hilt-fragment-testing-snapshot]

Must be used as `debugImplementation` dependency to properly register `EmptyFragmentActivity` in manifest.

<details>
  <summary>Kotlin</summary>

  ```kotlin
  dependencies {
      implementation("com.google.dagger:hilt-android:2.33-beta")

      androidTestImplementation("androidx.test:runner:1.3.0")
      debugImplementation("it.czerwinski.android.hilt:hilt-fragment-testing:[VERSION]")
  }
  ```
</details>

<details>
  <summary>Groovy</summary>

  ```groovy
  dependencies {
      implementation 'com.google.dagger:hilt-android:2.33-beta'

      androidTestImplementation 'androidx.test:runner:1.3.0'
      debugImplementation 'it.czerwinski.android.hilt:hilt-fragment-testing:[VERSION]'
  }
  ```
</details>

### Testing Fragments With Hilt

#### `HiltFragmentScenario`
Works exactly like [FragmentScenario], but supports Hilt dependency injection in fragments.


[ci-build]: https://github.com/sczerwinski/android-hilt/actions?query=workflow%3ABuild
[hilt-extensions-release]: https://repo1.maven.org/maven2/it/czerwinski/android/hilt/hilt-extensions/
[hilt-extensions-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/hilt/hilt-extensions/
[hilt-fragment-testing-release]: https://repo1.maven.org/maven2/it/czerwinski/android/hilt/hilt-fragment-testing/
[hilt-fragment-testing-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/hilt/hilt-fragment-testing/

[FragmentScenario]: https://developer.android.com/guide/fragments/test
[TestInstallIn]: https://dagger.dev/hilt/testing#testinstallin
