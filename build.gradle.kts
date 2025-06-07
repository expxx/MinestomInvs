import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import java.net.URI

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("com.diffplug.spotless") version "7.0.2"
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.32.0"
    signing
}

group = "dev.expx"
version = System.getenv("VERSION") ?: "1.0.6"

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    configure(KotlinJvm(
        javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
        sourcesJar = true
    ))

    coordinates(project.group.toString(), "minestom-invs", project.version.toString())

    signAllPublications()

    pom {
        name.set("Minestom Inventories")
        description.set("A library for creating and managing inventories in Minestom.")
        inceptionYear.set("2024")
        url.set("https://github.com/expxx/MinestomInvs")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/license/mit/")
            }
        }
        developers {
            developer {
                id.set("cammyzed")
                name.set("Cam M.")
                url.set("https://expx.dev")
                email.set("cam@expx.dev")
            }
        }
        scm {
            url.set("https://github.com/expxx/MinestomInvs")
            connection.set("scm:git:github.com/expxx/MinestomInvs.git")
            developerConnection.set("scm:git:ssh://github.com/expxx/MinestomInvs.git")
        }
    }
}

signing {
    val privateKey = System.getenv("ORG_GRADLE_PROJECT_SIGNINGINMEMORYKEY")
    val keyPassphrase = System.getenv("ORG_GRADLE_PROJECT_SIGNINGINMEMORYKEYPASSWORD")
    if (privateKey != null && keyPassphrase != null) {
        useInMemoryPgpKeys(privateKey, keyPassphrase)
    } else {
        logger.warn("GPG keys not found in environment variables. Signing will be skipped.")
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dokka {
    moduleName.set("Minestom Inventories")
    dokkaSourceSets.main {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(URI("https://github.com/expxx/MinestomInvs/tree/main/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }
        pluginsConfiguration.html {
            footerMessage.set("(c) 2025 The Cavern")
        }
    }
}

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

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("net.minestom:minestom-snapshots:87f6524aeb")
    compileOnly("net.kyori:adventure-text-minimessage:4.18.0")

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
    task("codeQuality") {
        dependsOn("spotlessCheck", "detekt")
    }
    task("spotless") {
        dependsOn("spotlessApply")
    }
}