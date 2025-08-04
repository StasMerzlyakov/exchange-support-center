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
include("blob-storage-service")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include("blob-storage-service:blob-storage-api-v1-jackson")
findProject(":blob-storage-service:blob-storage-api-v1-jackson")?.name = "blob-storage-api-v1-jackson"
include("blob-storage-service:blob-storage-core")
findProject(":blob-storage-service:blob-storage-core")?.name = "blob-storage-core"
include("blob-storage-service:blob-storage-spring")
findProject(":blob-storage-service:blob-storage-spring")?.name = "blob-storage-spring"
include("blob-storage-service:blob-storage-api-v1-grpc")
findProject(":blob-storage-service:blob-storage-api-v1-grpc")?.name = "blob-storage-api-v1-grpc"
include("blob-storage-service:blob-storage-api-common")
findProject(":blob-storage-service:blob-storage-api-common")?.name = "blob-storage-api-common"
