plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.pitest)
}

dependencies {
    testImplementation(kotlin("test"))
}
