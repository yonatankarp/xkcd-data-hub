import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.jackson.module)
    implementation(libs.kotlin.coroutines.core)
    // Vert.x
    implementation("io.vertx:vertx-lang-kotlin-coroutines:4.5.8")
    implementation("io.vertx:vertx-core:4.5.8")
    implementation("io.vertx:vertx-web:4.5.8")
    implementation("io.vertx:vertx-kafka-client:4.5.8")
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

// import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
// import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
// import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
// import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
// import org.jetbrains.kotlin.gradle.dsl.JvmTarget
// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
// import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
//
// plugins {
//    alias(libs.plugins.kotlin.jvm)
//    application
//    id("com.github.johnrengelman.shadow") version "7.1.2"
// }
//
// val vertxVersion = "4.5.8"
// val junitJupiterVersion = "5.9.1"
//
// val mainVerticleName = "com.xkcddatahub.data.processor.MainVerticle"
// val launcherClassName = "io.vertx.core.Launcher"
//
// val watchForChange = "src/**/*"
// val doOnChange = "$projectDir/gradlew classes"
//
// application {
//    mainClass.set(launcherClassName)
// }
//
// dependencies {
//    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
//    implementation("io.vertx:vertx-web")
//    implementation("io.vertx:vertx-lang-kotlin")
//    implementation(kotlin("stdlib-jdk8"))
//    if (System.getProperty("os.name").lowercase().contains("mac")) {
//        implementation("io.netty:netty-resolver-dns-native-macos:4.1.111.Final")
//    }
//    testImplementation("io.vertx:vertx-junit5")
//    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
// }
//
// val compileKotlin: KotlinCompile by tasks
// tasks.withType(KotlinJvmCompile::class.java).configureEach {
//    compilerOptions {
//        jvmTarget.set(JvmTarget.JVM_21)
//    }
// }
//
// tasks.withType<ShadowJar> {
//    archiveClassifier.set("fat")
//    manifest {
//        attributes(mapOf("Main-Verticle" to mainVerticleName))
//    }
//    mergeServiceFiles()
// }
//
// tasks.withType<Test> {
//    useJUnitPlatform()
//    testLogging {
//        events = setOf(PASSED, SKIPPED, FAILED)
//    }
// }
//
// tasks.withType<JavaExec> {
//    args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
// }
