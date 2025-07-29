plugins {
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(projects.common)
    implementation(platform(libs.spring.boot.dependencies))

    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)

    implementation(libs.starter.webflux)
    implementation(libs.starter.redis.reactive)

    implementation(libs.starter.actuator)
    implementation(libs.starter.aop)

    testImplementation(libs.junit.jupiter)
}