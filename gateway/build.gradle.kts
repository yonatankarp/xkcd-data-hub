plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.springboot)
    alias(libs.plugins.springboot.dependency.management)
    alias(libs.plugins.openapi.generator)
}

dependencies {
    implementation(libs.bundles.kotlin.all)
    implementation(libs.bundles.springboot.all)

    testImplementation(libs.bundles.tests.all)
    testImplementation(libs.bundles.springboot.tests.all)
}

tasks {
    jar {
        enabled = false
    }

    bootJar {
        enabled = true
    }
}
