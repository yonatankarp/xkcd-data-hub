import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

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

/*
 ********************************************
 ********* OPEN API SPEC GENERATION *********
 ********************************************
*/

val apiDirectoryPath = "${project.projectDir.parent}/api-contract"
val openApiGenerateOutputDir = "${layout.buildDirectory.get()}/generated/openapi"

data class ApiSpec(
    val name: String,
    val taskName: String,
    val directoryPath: String,
    val outputDir: String,
    val specFileName: String,
    val generatorType: String,
    val packageName: String,
    val modelPackageName: String,
    val config: Map<String, String> = emptyMap(),
    val templatesDir: String? = null,
)

/**
 * List of all api specs to generate
 */
val supportedApis =
    listOf(
        ApiSpec(
            name = "Gateway API",
            taskName = "generateGatewayApi",
            directoryPath = apiDirectoryPath,
            outputDir = "$openApiGenerateOutputDir/gateway",
            specFileName = "xkcd-data-hub-api.yaml",
            generatorType = "kotlin-spring",
            packageName = "com.xkcddatahub.gateway.openapi.v1",
            modelPackageName = "com.xkcddatahub.gateway.openapi.v1.models",
            config =
                mapOf(
                    "dateLibrary" to "java8",
                    "enumPropertyNaming" to "UPPERCASE",
                    "interfaceOnly" to "true",
                    "implicitHeaders" to "true",
                    "hideGenerationTimestamp" to "true",
                    "useTags" to "true",
                    "documentationProvider" to "none",
                    "useSpringBoot3" to "true",
                    "reactive" to "true",
                ),
        ),
    )

// Iterate over the api list and register them as generator tasks
supportedApis.forEach { api ->
    tasks.register<GenerateTask>(api.taskName) {
        group = "openapi tools"
        description = "Generate the code for ${api.name}"

        generatorName.set(api.generatorType)
        inputSpec.set("${api.directoryPath}/${api.specFileName}")
        outputDir.set(api.outputDir)
        apiPackage.set(api.packageName)
        modelPackage.set(api.modelPackageName)
        configOptions.set(api.config)
        api.templatesDir?.let { templateDir.set(it) }
    }
}

tasks {
    register("cleanGeneratedCodeTask") {
        description = "Removes generated Open API code"
        group = "openapi tools"

        doLast {
            logger.info("Cleaning up generated code")
            File(openApiGenerateOutputDir).deleteRecursively()
        }
    }

    clean {
        dependsOn("cleanGeneratedCodeTask")
        supportedApis.forEach { finalizedBy(it.taskName) }
    }

    compileKotlin {
        supportedApis.forEach { dependsOn(it.taskName) }
    }
}

supportedApis.forEach {
    sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].java {
        srcDir("${it.outputDir}/src/main/kotlin")
    }
}

/*
 ********************************************
 ******* OPEN API SPEC GENERATION END *******
 ********************************************
*/
