# mc-kotlin-plugin-template
Opinionated template/starter for creating Minecraft plugins in Kotlin using the Spigot API

## Features

- Gradle axion-release-plugin for managing semver
  - automatic updating of `CHANGELOG.md` and `main/resources/plugin.yml` when a release is made
- Github Actions to build PRs and automatically create Github releases when a release tag is pushed
- [`ktlint`](https://github.com/JLLeitschuh/ktlint-gradle) Gradle plugin
- Gradle build generates both a shadow jar which includes kotlin stdlib and a normal jar without
  - Users with the stdlib already on the classpath can use the smaller jar
