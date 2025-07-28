import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import name.remal.gradle_plugins.sonarlint.SonarLint
import name.remal.gradle_plugins.sonarlint.SonarLintExtension
import name.remal.gradle_plugins.sonarlint.SonarLintPlugin


plugins {
    java
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.sonarlint) apply false
}

group = "ru.otus.exchange"

val projectVersion: String by project
version = projectVersion


repositories {
    mavenLocal()
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
        options.compilerArgs.addAll(
            listOf(
                "-Xlint:all,-serial,-processing"
            )
        )
    }

    apply<SonarLintPlugin>()
    configure<SonarLintExtension> {

        nodeJs {
            detectNodeJs.set(false)
            logNodeJsNotFound.set(false)
        }
    }


    apply<SpotlessPlugin>()
    configure<SpotlessExtension> {
        java {
            removeUnusedImports()
            trimTrailingWhitespace()
            palantirJavaFormat("2.72.0")
        }
    }


    tasks.withType<SonarLint> {
        dependsOn("spotlessApply")
        exclude("src/test/resources/*")
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
        testImplementation(platform(rootProject.libs.testcontainers.bom))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            force("io.micrometer:micrometer-observation:1.15.0")
            force("io.micrometer:micrometer-core:1.15.0")
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

            force("io.projectreactor:reactor-core:3.7.6")
            force("org.jboss.logging:jboss-logging:3.6.1.Final")
            force("com.fasterxml:classmate:1.7.0")

            force("io.netty:netty-common:4.1.121.Final")
            force("io.netty:netty-handler:4.1.121.Final")
            force("io.netty:netty-transport:4.1.121.Final")
            force("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")

            force("org.projectlombok:lombok:1.18.38")

            force("org.hamcrest:hamcrest:3.0")
            force("net.bytebuddy:byte-buddy:1.17.5")
            force("net.bytebuddy:byte-buddy-agent:1.17.5")

            force("org.junit.jupiter:junit-jupiter-api:5.12.2")
            force("net.minidev:json-smart:2.5.2")

            force("org.apache.httpcomponents.client5:httpclient5:5.4.4")
            force("org.awaitility:awaitility:4.3.0")


            force("org.testcontainers:testcontainers:1.21.0")
            force("org.hamcrest:hamcrest-core:3.0")

            force("com.fasterxml.jackson.core:jackson-databind:2.19.1")
            force("com.fasterxml.jackson.core:jackson-annotations:2.19.1")
            force("com.fasterxml.jackson.core:jackson-core:2.19.1")
            force("com.fasterxml.jackson:jackson-bom:2.19.1")

            force("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.19.1")
            force("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.1")
            force("com.fasterxml.jackson.module:jackson-module-parameter-names:2.19.1")

            force("org.apache.httpcomponents.core5:httpcore5:5.3.4")

            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.25")

            force("javax.xml.bind:jaxb-api:2.3.0")

            force("org.junit:junit-bom:5.12.2")
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