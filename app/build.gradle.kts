plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "tr.com.gndg.self"
    compileSdk = 34
    ndkVersion = "26.1.10909125"

    defaultConfig {
        applicationId = "tr.com.gndg.self"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
        versionName = "1.0.8"

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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Koin
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")

    //gson
    implementation ("com.google.code.gson:gson:2.10.1")
    //json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Joda Time
    implementation ("net.danlew:android.joda:2.12.5")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0") //flow
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4") //state
    implementation("androidx.compose.runtime:runtime-rxjava2:1.5.4") //subscribeAsState()

    // room database
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:2.6.1")


    implementation("com.google.guava:guava:32.1.3-android")
    implementation ("androidx.camera:camera-core:1.3.1")
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")

    implementation ("com.google.accompanist:accompanist-permissions:0.19.0")

    implementation("io.coil-kt:coil-compose:2.5.0")

    //bar code scanner
    implementation ("com.google.android.gms:play-services-code-scanner:16.1.0")

    //drag
    implementation("androidx.compose.foundation:foundation:1.6.0-rc01")

    //pdf
    implementation ("com.itextpdf:itextpdf:5.5.13.3")

    //ROOM BACKUP
    //https://github.com/rafi0101/Android-Room-Database-Backup
    implementation ("de.raphaelebner:roomdatabasebackup:1.0.0-beta12")
    //https://github.com/JakeWharton/ProcessPhoenix
    implementation ("com.jakewharton:process-phoenix:2.1.2")
    implementation ("androidx.activity:activity-ktx:1.8.2")

}