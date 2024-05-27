plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktor) apply false
}

subprojects {
    group = "com.xkcddatahub"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}
