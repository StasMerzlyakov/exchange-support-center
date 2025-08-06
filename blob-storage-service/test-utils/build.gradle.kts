plugins {
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.spotless)
}

dependencies {
    implementation(libs.reactor.core)
    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)
    implementation(projects.blobStorageService.blobStorageApiCommon)
    testImplementation(projects.blobStorageService.testUtils)
}