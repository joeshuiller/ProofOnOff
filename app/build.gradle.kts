plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.detekt)
}
detekt {
    buildUponDefaultConfig = true // Usa las reglas por defecto de Detekt
    allRules = false // No actives todas las reglas experimentales
    config.setFrom(files("$rootDir/config/detekt/detekt.yml")) // Tu archivo de reglas personalizado
}
// Configuración opcional para personalizar la documentación
tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("documentation/html"))

    dokkaSourceSets.named("main") {
        moduleName.set("Proof On Off App")

        // Incluye una descripción de la arquitectura en la página de inicio
        includes.from("module.md")

        // No documentar archivos generados (como BuildConfig)
        suppressGeneratedFiles.set(true)
    }
}
android {
    namespace = "com.janes.saenz.puerta.proofonoff"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.janes.saenz.puerta.proofonoff"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // No ofuscar en debug para poder debugear
            applicationIdSuffix = ".debug"
            defaultConfig {
                val urlApi = project.findProperty("URL_API") ?: ""
                // La inyectamos como un String de Java (por eso las comillas escapadas)
                buildConfigField("String", "URL_API", "\"$urlApi\"")


            }
        }
        release {
            isMinifyEnabled = false // No ofuscar en debug para poder debugear
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            defaultConfig {
                val urlApi = project.findProperty("URL_API") ?: ""
                // La inyectamos como un String de Java (por eso las comillas escapadas)
                buildConfigField("String", "URL_API", "\"$urlApi\"")


            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Core & UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)

    // Compose (BOM)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.icons.core)
    implementation(libs.androidx.compose.icons.extended)



    // Networking
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Unit Testing
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)

    // Instrumented Testing

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.test.android.compose.junit4)
    androidTestImplementation(libs.test.android.ext)
    androidTestImplementation(libs.test.android.runner)
    androidTestImplementation(libs.test.android.espresso)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    // Debug Tools
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.test.android.compose.manifest)

    // Utils
    implementation(libs.timber)
    implementation(libs.coil.compose)

    //plugins
    detektPlugins(libs.detekt.formatting)

}
tasks.withType<JacocoReport> {
    classDirectories.setFrom(files(classDirectories.map {
        fileTree(it) {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "android/**/*.*",
                "**/DataModule*",
                "**/Dagger*",
                "**/Hilt*",
                "**/*_MembersInjector*",
                "**/*_Factory*",
                "**/*_Provide*Factory*",
                "**/*Module*",
                "**/*_HiltModules*",
                "**/*HiltWrapper*",
                "**/dagger/hilt/internal/*",
                "**/dagger/hilt/processor/*", // Aquí entra AggregatedRootGenerator
                "**/*_Factory*",
                "**/*_Provides*",
                "**/*_MembersInjector*",
                "**/*_GeneratedInjector*",
                "**/*Module_*"
            )
        }
    }))
}