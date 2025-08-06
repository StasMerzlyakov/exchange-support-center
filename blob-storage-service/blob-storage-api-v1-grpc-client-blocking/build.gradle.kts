import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.spotless)
    alias(libs.plugins.protobuf)
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.30.2" // TODO toml libs.protobuf.protoc
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.73.0" // TODO toml libs.protoc.gen.grpc.java
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
    sourceSets {
        main {
            proto {
                srcDirs("../../specs/proto")
            }
        }
    }
}

dependencies {
    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)
    implementation(projects.blobStorageService.blobStorageApiCommon)
    implementation(libs.annotation.api)
    implementation(libs.protobuf.protoc)
    implementation(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.stub)
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    implementation(libs.slf4j.api)
    testRuntimeOnly(libs.slf4j.simple)
    implementation(libs.reactor.core)
    testImplementation(libs.mockito.core)
    testImplementation(projects.blobStorageService.testUtils)
    annotationProcessor(libs.mapstruct.processor)
}