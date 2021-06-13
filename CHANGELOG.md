# Changelog

## [Unreleased]
### Changed
- Dependencies:
  - Upgrade Kotlin to `1.5.10`
  - Upgrade Hilt to `2.37`

## [1.1.1]
### Changed
- Upgrade Gradle to `6.9`
- Dependencies:
  - Upgrade Kotlin to `1.5.0`
  - Upgrade Android Gradle Plugin to `4.2.1`
  - Upgrade Hilt to `2.35.1` (stable)
  - Upgrade Ktor Client to `1.5.4` (example only)

## [1.1.0]
### Changed
- Upgrade Gradle to `6.8.2`
- Dependencies:
  - Upgrade Hilt to `2.32-alpha`

## [1.1.0-RC1]
### Changed
- Upgrade Gradle to `6.8`
- Dependencies:
  - Upgrade Kotlin to `1.4.30`
  - Upgrade Android Gradle Plugin to `4.1.2`
  - Upgrade Hilt to `2.31.2-alpha`
  - Upgrade Ktor Client to `1.5.1` (example only)

## [1.1.0-BETA1]
### Changed
- Dependencies:
  - Upgrade `kotlin-serialization` to `1.4.21-2` in "Generated Modules" example

## [1.1.0-ALPHA2]
### Added
- Annotations for testing:
  - `@TestBound` – works like `@Bound`, but generates modules annotated with `@TestInstallIn`
  - `@TestBoundTo` – works like `@BoundTo`, but generates modules annotated with `@TestInstallIn`
  - `@TestFactoryMethod` – works like `@TestFactoryMethod`, but generates modules annotated with `@TestInstallIn`

### Changed
- More specific grouping of generated methods into dagger modules:
  - `@Bound` and `@BoundTo` – grouped by supertype.
  - `@FactoryMethod` – grouped by returned type.

## [1.1.0-ALPHA1]
### Added
- `@Bound` annotation (works like `@BoundTo`, but implicitly uses the direct supertype of the annotated class)

### Changed
- Make `component` in annotations optional and set to `SingletonComponent` by default.
- Dependencies:
  - Upgrade Hilt to `2.31-alpha`
  - Upgrade `org.jetbrains.changelog` to `1.0.1`
  - Upgrade `io.mockk:mockk` to `1.10.5`

## [1.0.0]
No changes since 1.0.0-RC1

## [1.0.0-RC1]
No changes since 1.0.0-BETA1

## [1.0.0-BETA1]
### Fixed
- Fix module names, package names and descriptions in documentation

## [1.0.0-ALPHA3]
### Added
- `HiltFragmentScenario`

### Changed
- **BREAKING CHANGE:** Rename `@Primary` to `@BoundTo`.

### Fixed
- Support for annotated nested classes
- `@NonNull` annotations in generated code

## [1.0.0-ALPHA2]
### Added
- Annotation processor generating Hilt modules providing instances from annotated factory methods.

### Changed
- **BREAKING CHANGE:** Rename `superclass` to `supertype`.
- **BREAKING CHANGE:** Copy `@Scope` and `@Qualifier` annotations from the `@Primary`-annotated type
  to the binding method instead of `scope` and `qualifier` annotation parameters.

## [1.0.0-ALPHA1]
### Added
- Property delegation to:
  - `dagger.Lazy`
  - `javax.inject.Provider`
- Annotation processor generating Hilt modules binding primary implementations to their supertypes.
