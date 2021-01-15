# Changelog

## [Unreleased]
### Changed
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
