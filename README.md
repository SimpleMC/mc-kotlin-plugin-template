# mc-kotlin-plugin-template
Opinionated template/starter for creating Minecraft plugins in Kotlin using the Spigot API

## Features

- Gradle axion-release-plugin for managing semver
  - automatic updating of `CHANGELOG.md` and `main/resources/plugin.yml` when a release is made
- Github Actions to build PRs and automatically create Github releases when a release tag is pushed
- [`ktlint`](https://github.com/JLLeitschuh/ktlint-gradle) Gradle plugin
- Gradle build generates a standard plugin jar which will download dependencies declared as
[`libraries`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/PluginDescriptionFile.html#getLibraries()) in
`plugin.yml` and an "offline"/shadowed jar containing necessary dependencies
