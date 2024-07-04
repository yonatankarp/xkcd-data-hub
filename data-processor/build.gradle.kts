import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

dependencies {
    implementation(libs.kotlin.jackson.module)
    implementation(libs.kotlin.coroutines.core)
    // Vert.x
    implementation("io.vertx:vertx-lang-kotlin-coroutines:4.5.8")
    implementation("io.vertx:vertx-core:4.5.8")
    implementation("io.vertx:vertx-web:4.5.8")
    implementation("io.vertx:vertx-kafka-client:4.5.8")

    implementation(libs.koin)

    if (System.getProperty("os.name").lowercase().contains("mac") &&
        System.getProperty("os.arch").lowercase() == "aarch64"
    ) {
        implementation("io.netty:netty-resolver-dns-native-macos:4.1.111.Final:osx-aarch_64")
    }

    // OpenSearch
    implementation("org.opensearch.client:opensearch-rest-high-level-client:2.15.0")
    implementation("org.opensearch.client:opensearch-rest-client:2.15.0")
    // Tests
    testImplementation("io.vertx:vertx-junit5:4.5.8")
    testImplementation(libs.bundles.tests.all)
}

tasks.withType(KotlinJvmCompile::class.java).configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

val mainVerticleName = "com.xkcddatahub.data.processor.bootstrap.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

application {
    mainClass.set(launcherClassName)
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("fat")
    manifest {
        attributes(mapOf("Main-Verticle" to mainVerticleName))
    }
    mergeServiceFiles()
}

tasks.withType<JavaExec> {
    args =
        listOf(
            "run",
            mainVerticleName,
            "--redeploy=src/**/*",
            "--launcher-class=$launcherClassName",
            "--on-redeploy=$projectDir/gradlew classes",
        )
}
