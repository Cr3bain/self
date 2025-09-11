import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "tr.com.gndg.self"
    compileSdk = 36
    ndkVersion = "26.1.10909125"

    defaultConfig {
        applicationId = "tr.com.gndg.self"
        minSdk = 24
        targetSdk = 36
        versionCode = 11
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            debugSymbolLevel  = "SYMBOL_TABLE"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.3")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation(platform("androidx.compose:compose-bom:2025.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.9.4")

    // Koin
    implementation("io.insert-koin:koin-androidx-compose:4.1.1")

    //gson
    implementation ("com.google.code.gson:gson:2.13.2")
    //json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Joda Time
    implementation ("net.danlew:android.joda:2.13.1")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.3") //flow
    implementation("androidx.compose.runtime:runtime:1.9.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.9.1") //state
    implementation("androidx.compose.runtime:runtime-rxjava2:1.9.1") //subscribeAsState()

    // room database
    implementation("androidx.room:room-runtime:2.8.0")
    annotationProcessor("androidx.room:room-compiler:2.8.0")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.8.0")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:2.8.0")


    implementation("com.google.guava:guava:33.4.8-android")
    implementation ("androidx.camera:camera-core:1.5.0")
    implementation ("androidx.camera:camera-camera2:1.5.0")
    implementation ("androidx.camera:camera-lifecycle:1.5.0")
    implementation ("androidx.camera:camera-view:1.5.0")

    implementation ("com.google.accompanist:accompanist-permissions:0.37.3")

    implementation("io.coil-kt:coil-compose:2.7.0")

    //bar code scanner
    implementation ("com.google.android.gms:play-services-code-scanner:16.1.0")

    //drag
    implementation("androidx.compose.foundation:foundation:1.9.1")

    //pdf
    implementation ("com.itextpdf:itextpdf:5.5.13.4")

    //ROOM BACKUP
    //https://github.com/rafi0101/Android-Room-Database-Backup
    implementation ("de.raphaelebner:roomdatabasebackup:1.0.2")
    //https://github.com/JakeWharton/ProcessPhoenix
    implementation ("com.jakewharton:process-phoenix:3.0.0")
    implementation ("androidx.activity:activity-ktx:1.11.0")

}