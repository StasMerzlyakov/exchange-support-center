import com.google.protobuf.gradle.id

plugins {
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
        artifact = "com.google.protobuf:protoc:4.31.1"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.73.0"
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
   // implementation(projects.blobStorageService.blobStorageCore)
    implementation(libs.annotation.api)
    implementation(libs.protoc)
    implementation(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.stub)
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
}