plugins {
    alias(libs.plugins.bjornvester.xjc)
}

dependencies {
    xjcPlugins(libs.jvnet.jaxb2.basics)
    implementation(libs.jakarta.bind.api)
    xjcPlugins(libs.jaxb.api)
    testRuntimeOnly(libs.jaxb.runtime)
}


xjc {
    xsdDir.set(layout.projectDirectory.dir("src/main/schema"))
    generateEpisode.set(true)
    bindingFiles.setFrom(
        layout.projectDirectory.dir("src/main/schema")
            .asFileTree.matching { include("**/*.xjb.xml") })
    options.addAll(
        listOf(
            "-Xannotate",
            "-extension",
            "-Xsetters", // Настройки для генерации setter-ов для коллекций
            "-Xsetters-mode=direct", // Настройки для генерации setter-ов для коллекций
            "-Xinheritance", // Для добавления родителя или интерфейса к сгенерированным классам jaxb
        )
    )
}

tasks.withType<JavaCompile> {
    dependsOn("xjc")
}
