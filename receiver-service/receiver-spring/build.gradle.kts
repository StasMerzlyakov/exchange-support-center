plugins {
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(projects.common)
    implementation(projects.receiverService.receiverApiV1Swagger)
    implementation(platform(libs.spring.boot.dependencies))

    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)

    implementation(libs.spring.boot.starter.web)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    implementation(libs.swagger.codegen){
        exclude(module = libs.slf4j.simple.get().name)
    }

    implementation(libs.starter.actuator)
    implementation(libs.starter.aop)

    testImplementation(libs.junit.jupiter)
}