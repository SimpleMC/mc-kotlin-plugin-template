import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import pl.allegro.tech.build.axion.release.domain.hooks.HookContext
import pl.allegro.tech.build.axion.release.domain.hooks.HooksConfig
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.yaml:snakeyaml:1.30")
    }
}

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("pl.allegro.tech.build.axion-release") version "1.13.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = "org.simplemc"
version = scmVersion.version

val mcApiVersion: String by project
val repoRef: String by project

scmVersion {
    versionIncrementer("incrementMinorIfNotOnRelease", mapOf("releaseBranchPattern" to "release/.+"))

    hooks(
        closureOf<HooksConfig> {
            pre(
                "fileUpdate",
                mapOf(
                    "file" to "CHANGELOG.md",
                    "pattern" to KotlinClosure2<String, HookContext, String>({ v, _ ->
                        "\\[Unreleased\\]([\\s\\S]+?)\\n(?:^\\[Unreleased\\]: https:\\/\\/github\\.com\\/$repoRef\\/compare\\/[^\\n]*\$([\\s\\S]*))?\\z"
                    }),
                    "replacement" to KotlinClosure2<String, HookContext, String>({ v, c ->
                        """
                            \[Unreleased\]
                            
                            ## \[$v\] - ${currentDateString()}$1
                            \[Unreleased\]: https:\/\/github\.com\/$repoRef\/compare\/v$v...HEAD
                            \[$v\]: https:\/\/github\.com\/$repoRef\/${if (c.previousVersion == v) "releases/tag/v$v" else "compare/v${c.previousVersion}...v$v"}${'$'}2
                        """.trimIndent()
                    })
                )
            )
            pre("commit")
        }
    )
}

fun currentDateString() = OffsetDateTime.now(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.ISO_DATE)

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    jcenter()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(group = "org.spigotmc", name = "spigot-api", version = "$mcApiVersion+")
}

tasks {
    wrapper {
        gradleVersion = "7.4.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    processResources {
        val placeholders = mapOf(
            "version" to version,
            "apiVersion" to mcApiVersion,
            "kotlinVersion" to project.properties["kotlinVersion"]
        )

        filesMatching("plugin.yml") {
            expand(placeholders)
        }

        // create an "offline" copy/variant of the plugin.yml with `libraries` omitted
        doLast {
            val resourcesDir = sourceSets.main.get().output.resourcesDir
            val yamlDumpOptions =
                // make it pretty for the people
                DumperOptions().also {
                    it.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
                    it.setPrettyFlow(true)
                }
            val yaml = Yaml(yamlDumpOptions)
            val pluginYml: Map<String, Any> = yaml.load(file("$resourcesDir/plugin.yml").inputStream())
            yaml.dump(pluginYml.filterKeys { it != "libraries" }, file("$resourcesDir/offline-plugin.yml").writer())
        }
    }

    jar {
        exclude("offline-plugin.yml")
    }

    // offline jar should be ready to go with all dependencies
    shadowJar {
        minimize()
        archiveClassifier.set("offline")
        exclude("plugin.yml")
        rename("offline-plugin.yml", "plugin.yml")
    }

    // avoid classpath conflicts/pollution via relocation
    val configureShadowRelocation by registering(ConfigureShadowRelocation::class) {
        target = shadowJar.get()
        prefix = "${project.group}.${project.name.toLowerCase()}.libraries"
    }

    build {
        dependsOn(shadowJar).dependsOn(configureShadowRelocation)
    }
}
