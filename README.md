# mc-kotlin-plugin-template
Opinionated template/starter for creating Minecraft plugins in Kotlin using the Spigot API

## Features

- Gradle axion-release-plugin for managing semver
    - automatic updating of `CHANGELOG.md` and `main/resources/plugin.yml` when a release is made
- Github Actions to build PRs and automatically create Github releases when a release tag is pushed
    - Manual Create Release pipeline to increment semver tag and trigger publishing a new version
      - Requires a secret named `PAT` with a GitHub PAT with code read/write permission to the repository
- [`ktlint`](https://github.com/JLLeitschuh/ktlint-gradle) Gradle plugin
- Gradle build generates a standard plugin jar which will download dependencies declared as
[`libraries`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/PluginDescriptionFile.html#getLibraries()) in
`plugin.yml` and an "offline"/shadowed jar containing necessary dependencies

## Usage

1. Use the template to create a new repository: [Create a new repository](https://github.com/SimpleMC/mc-kotlin-plugin-template/generate)
2. Change template repository references
    - `settings.gradle.kts` -> set `rootProject.name`
    - `gradle.properties` -> set `repoRef`
    - `build.gradle.kts` -> set `group`
    - `CHANGELOG.md` -> update links to `SimpleMC/mc-kotlin-plugin-template` to match `repoRef`
    - `src/main/resources/plugin.yml` -> set `name`, `main`, `website`, `author`
    - `src/main/kotlin/org/simplemc/plugintemplate/KotlinPluginTemplate.kt` -> Move packages/rename for your plugin
    - `README.md` -> Update
3. To use the Create Release automation, add PAT secret
    1. Create a Personal Access Token: https://github.com/settings/personal-access-tokens/new
        - Repository Access: "Only select repositories" and pick the plugin template fork
        - Repository Permissions: Contents Read & write
            - This is so the automation can create release commits
    2. Add the PAT as an Actions secret to your new repository: `https://github.com/<repo slug>/settings/secrets/actions/new`
        - Name: `PAT`
        - Secret Contents: Paste the Personal Access Token you created in the previous step

## Examples

Several SimpleMC plugins are built off of this template or were the impetus for it:

- [SimpleNPCs](https://github.com/SimpleMC/SimpleNPCs) - Simple command-based NPC interactions
- [SimpleHealthbars2](https://github.com/SimpleMC/SimpleHealthbars2) - Simple, easy-to-use healthbar plugin with optional player and mob healthbars
- [SimpleAnnounce](https://github.com/SimpleMC/SimpleAnnounce) - SimpleAnnounce is a simple and easy to use, yet powerful automated announcement plugin for the Bukkit Minecraft API.