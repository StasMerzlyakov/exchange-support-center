plugins {
    alias(libs.plugins.mijmh)
}

dependencies {
    implementation(project(":models:xsd"))
    implementation(libs.jakarta.bind.api)
    testRuntimeOnly(libs.jaxb.runtime)
}