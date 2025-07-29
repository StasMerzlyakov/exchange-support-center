plugins {
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(projects.common)
    implementation(platform(libs.spring.boot.dependencies))
    implementation(platform(libs.spring.cloud.dependencies))

    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)

    implementation(libs.cloud.starter.webflux)
    implementation(libs.starter.webflux)
    implementation(libs.starter.redis.reactive)

    implementation(libs.starter.actuator)
    implementation(libs.starter.aop)
    implementation(libs.starter.validation)

    implementation(libs.java.uuid.generator)

    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")

    implementation(platform(libs.opentelemetry.instrumentation.bom))
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations")

    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(libs.spring.cloud.contract.wiremock)
    testImplementation(libs.redis.testcontainers)
}
