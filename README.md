# mc-kotlin-plugin-template
Opinionated template/starter for creating Minecraft plugins in Kotlin using the Spigot API

## Features

- Gradle axion-release-plugin for managing semver
    - automatic updating of `CHANGELOG.md` and `main/resources/plugin.yml` when a release is made
- Github Actions to build PRs and automatically create Github releases when a release tag is pushed
    - Manual Create Version pipeline to increment semver tag and trigger publishing a new version
      - Requires a configured deploy key with write permission to the repository (see usage below)
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
3. To use the Create Version automation, add an SSH key
    1. Create an SSH key-pair (no password): `ssh-keygen -t ed25519 -C "your_email@example.com" -f ~/.ssh/<name_your_key>deploy`
    2. Add the Public Key as a Deploy Key (**Important! Enable `Allow write access`**): https://docs.github.com/en/authentication/connecting-to-github-with-ssh/managing-deploy-keys#set-up-deploy-keys an Actions secret to your new repository: `https://github.com/<repo slug>/settings/secrets/actions/new`
    3. Add the Private Key as an Actions secret: `https://github.com/<repo slug>/settings/secrets/actions/new`
        - Name: `COMMIT_KEY`
        - Secret Contents: Paste the Private key
    4. The GitHub Actions are configured to use this key to publish tags and release commits (see `.github/workflows/create-version.yml`)
        - See [axion-release-plugin Authorization](https://axion-release-plugin.readthedocs.io/en/latest/configuration/authorization/) for alternative Auth options

## Examples

Several SimpleMC plugins are built off of this template or were the impetus for it:

- [SimpleNPCs](https://github.com/SimpleMC/SimpleNPCs) - Simple command-based NPC interactions
- [SimpleHealthbars2](https://github.com/SimpleMC/SimpleHealthbars2) - Simple, easy-to-use healthbar plugin with optional player and mob healthbars
- [SimpleAnnounce](https://github.com/SimpleMC/SimpleAnnounce) - SimpleAnnounce is a simple and easy to use, yet powerful automated announcement plugin for the Bukkit Minecraft API.
