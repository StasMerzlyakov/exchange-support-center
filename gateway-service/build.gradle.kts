plugins {
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":common"))
    implementation(platform(libs.spring.boot.dependencies))
    implementation(platform(libs.spring.cloud.dependencies))

    compileOnly(libs.project.lombok)
    annotationProcessor(libs.project.lombok)

    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")

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

    testImplementation("org.testcontainers:junit-jupiter")
}
