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

#### `@Primary`
Marks primary implementation of the given supertype.

For example:
```kotlin
interface Repository

@Primary(supertype = Repository::class, component = SingletonComponent::class)
class RepositoryA @Inject constructor() : Repository

@Primary(supertype = Repository::class, component = SingletonComponent::class)
@Singleton
class RepositoryB @Inject constructor() : Repository

@Primary(supertype = Repository::class, component = SingletonComponent::class)
@Named("offline")
class RepositoryC @Inject constructor() : Repository
```
will generate module:
```java
@Module
@InstallIn(SingletonComponent.class)
public interface SingletonComponent_PrimaryModule {

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
annotation processor will generate module:
```java
@Module
@InstallIn(SingletonComponent.class)
public class SingletonComponent_FactoryMethodsModule {

    @Provides
    public DatabaseFactory databaseFactoryProviderCreateDatabaseFactory(
            @ApplicationContext Context context) {
        return DatabaseFactoryProvider.INSTANCE.createDatabaseFactory(context);
    }

    @Provides
    @Singleton
    public AppDatabase databaseFactoryCreateDatabase(DatabaseFactory $receiver) {
        return $receiver.createDatabase();
    }

    @Provides
    @Singleton
    public UsersDao appDatabaseUsersDao(AppDatabase $receiver) {
        return $receiver.usersDao();
    }
}
```

## Hilt Testing Extensions

[![Maven Central](https://img.shields.io/maven-central/v/it.czerwinski.android.hilt/hilt-fragment-testing)][hilt-fragment-testing-release]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/it.czerwinski.android.hilt/hilt-fragment-testing?server=https%3A%2F%2Foss.sonatype.org)][hilt-fragment-testing-snapshot]

<details>
  <summary>Kotlin</summary>

  ```kotlin
  dependencies {
      implementation("com.google.dagger:hilt-android:2.30.1-alpha")
      androidTestImplementation("it.czerwinski.android.hilt:hilt-fragment-testing:[VERSION]")
  }
  ```
</details>

<details>
  <summary>Groovy</summary>

  ```groovy
  dependencies {
      implementation 'com.google.dagger:hilt-android:2.30.1-alpha'
      androidTestImplementation 'it.czerwinski.android.hilt:hilt-fragment-testing:[VERSION]'
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
