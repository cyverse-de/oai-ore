# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions
of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]
### Added
- A new arity for build-ore.

## [1.0.2]
### Changed
- Replaced `dc:identifier` with `dcterms:identifier` in generated files.
- All archived files, including the resource map now have a `dcterms:identifier` element.
- Metadata attributes will now be trimmed before being placed in the ORE file.

## [1.0.1]
### Added
- format-id constant.

## 1.0.0
### Added
- RdfSerializable protocol.
- Aggregation type.
- Archive type.
- ArchivedFile type.
- Ore type.
- build-ore

[Unreleased]: https://github.com/cyverse-de/oai-ore/compare/1.0.2...HEAD
[1.0.2]: https://github.com/cyverse-de/oai-ore/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/cyverse-de/oai-ore/compare/1.0.0...1.0.1
