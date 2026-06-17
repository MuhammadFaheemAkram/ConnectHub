# Security Policy

ConnectHub is a portfolio/demo Android app and does not connect to a real backend. It still treats security reports seriously because the project demonstrates production-style structure.

## Supported Versions

Only the latest `main` branch is supported.

## Reporting A Vulnerability

Please do not open public issues for sensitive security reports. Instead, contact the repository maintainer privately through the GitHub security advisory flow or the maintainer contact listed on the repository profile.

Include:

- A clear description of the issue
- Steps to reproduce
- Affected files or features
- Expected impact
- Suggested fix, if known

## Demo App Notes

- Authentication is fake and should not be reused as real authentication.
- Fake API services use hard-coded data and simulated delay.
- No production secrets should be committed to this repository.
- `local.properties` and generated build outputs are ignored.
