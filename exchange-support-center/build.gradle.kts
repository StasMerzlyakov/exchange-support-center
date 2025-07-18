import name.remal.gradle_plugins.sonarlint.SonarLint
import name.remal.gradle_plugins.sonarlint.SonarLintExtension


plugins {
    java
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.sonarlint) apply false
}

group = "ru.otus.exchange"

val projectVersion: String by project
version = projectVersion


subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }


    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }

    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()
    configure<SonarLintExtension> {
        nodeJs {
            detectNodeJs.set(false)
            logNodeJsNotFound.set(false)
        }
    }

    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            palantirJavaFormat("2.39.0")
        }
    }

    tasks.withType<SonarLint> {
        dependsOn("spotlessApply")
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21)) // Or your desired version
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        setJvmArgs(listOf("-XX:+StartAttachListener"))
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }

    dependencies {
        testImplementation(platform(rootProject.libs.junit.bom))
        testImplementation(rootProject.libs.junit.jupiter)
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
        }
    }
}

tasks.test {
    useJUnitPlatform()
}