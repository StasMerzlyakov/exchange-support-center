plugins {
    alias(libs.plugins.freefair.lombok)
}

dependencies {

    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)

    implementation(libs.slf4j.api)
}

