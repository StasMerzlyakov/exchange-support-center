pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "exchange-support-service"
include("gateway-service")
include("common")
include("models")
include("models:xsd")
findProject(":models:xsd")?.name = "xsd"
include("models:mapping")
include("models:jmh-tests")
findProject(":models:jmh-tests")?.name = "jmh-tests"
include("jfr-image")
include("jfr-image:helper")
findProject(":jfr-image:helper")?.name = "helper"
include("blob-storage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
