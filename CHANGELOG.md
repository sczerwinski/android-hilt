# Changelog

## [Unreleased]
### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security
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
