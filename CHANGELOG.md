# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [1.13.0] - 2023-08-01
### Added
- Add autoscaling/v2 namespace

## [1.12.0] - 2023-08-01
### Added
- Add policy/v1 namespace

## [1.11.0] - 2023-08-01
### Added
- Add `kubernetes.api.storage-k8s-io-v1` namespace
- Add `declare` statement to allow static analysis to know about methods on `kubernetes.api.v1`

## [1.10.0] - 2022-07-12
### Added
- Add security.istio.io namespace

## [1.9.0] - 2022-07-06
### Added
- Add networking.istio.io namespace

## [1.8.0] - 2022-12-08
### Added
- Add networking.k8s.io namespace
- Namespace to support openapiv2

## [0.1.1] - 2015-12-02
### Changed
- Documentation on how to make the widgets.

### Removed
- `make-widget-sync` - we're all async, all the time.

### Fixed
- Fixed widget maker to keep working when daylight savings switches over.

## 0.1.0 - 2015-12-02
### Added
- Files from the new template.
- Widget maker public API - `make-widget-sync`.

[unreleased]: https://github.com/your-name/kubernetes.api/compare/0.1.1...HEAD
[0.1.1]: https://github.com/your-name/kubernetes.api/compare/0.1.0...0.1.1
