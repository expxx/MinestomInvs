import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("com.diffplug.spotless") version "7.0.2"
    id("org.jetbrains.dokka") version "2.0.0"
    id("maven-publish")
    id("signing")
    id("java-library")
}

java {
    withJavadocJar()
    withSourcesJar()
}

group = "dev.expx"
version = "1.0.4-SNAPSHOT"

detekt {
    buildUponDefaultConfig = true
    allRules = false
    baseline = file("config/baseline.xml")
}
spotless {
    kotlin {
        licenseHeaderFile(file("config/HEADER.txt"))
        leadingTabsToSpaces(4)
        targetExclude("src/main/kotlin/dev/expx/minestominvs/util/MiniMessage.kt")
    }
}

group = "dev.expx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom-snapshots:87f6524aeb")
    implementation("net.kyori:adventure-text-minimessage:4.18.0")


    // Detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-ruleauthors:1.23.7")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    withType<Detekt>().configureEach {
        jvmTarget = "21"
    }
    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "21"
    }
    jar {
        manifest {
            attributes["Main-Class"] = "host.minestudio.lobby.LobbyMainKt"
        }
    }
    task("codeQuality") {
        dependsOn("spotlessCheck", "detekt")
    }
    task("spotless") {
        dependsOn("spotlessApply")
    }
}

kotlin {
    jvmToolchain(21)
}