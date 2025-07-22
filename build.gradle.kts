import com.palantir.javaformat.gradle.PalantirJavaFormatPlugin
import name.remal.gradle_plugins.sonarlint.SonarLint
import name.remal.gradle_plugins.sonarlint.SonarLintExtension


plugins {
    java
    alias(libs.plugins.palantire) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.sonarlint) apply false
}

group = "ru.otus.exchange"

val projectVersion: String by project
version = projectVersion


repositories {
    mavenCentral()
}

allprojects {
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

    plugins.apply(PalantirJavaFormatPlugin::class.java)

    buildscript {
        dependencies {
            classpath(rootProject.libs.palantir.javaformat)
        }
        repositories {
            mavenLocal()
            mavenCentral()
        }
    }

    tasks.withType<SonarLint> {
        dependsOn("formatDiff")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        jvmArgs = listOf("-XX:+StartAttachListener")
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
            force("io.micrometer:micrometer-observation:1.15.1")
            force("org.slf4j:slf4j-api:2.0.17")
            force("com.google.guava:guava:32.1.3-jre")

            force("commons-io:commons-io:2.16.1")
            force("org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r")
            force("org.apache.commons:commons-compress:1.26.1")
            force("commons-codec:commons-codec:1.16.1")
            force("org.apache.commons:commons-lang3:3.14.0")
            force("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
            force("com.google.code.gson:gson:2.11.0")
            force("org.jetbrains.kotlin:kotlin-stdlib-common:1.6.10")
            force("com.google.errorprone:error_prone_annotations:2.27.0")
            force("org.jetbrains:annotations:19.0.0")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Or your desired version
    }
}

tasks.test {
    useJUnitPlatform()
}