plugins {
    alias(libs.plugins.freefair.lombok)
}

dependencies {
    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)
    implementation(libs.murmur3)
    implementation(libs.jackson.databind)
    implementation(libs.slf4j.api)
    implementation(libs.json.unit.assertj)
    testImplementation(libs.mockito.core)
}

