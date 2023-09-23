@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.arch.mvi.testing"
    compileSdk = 33

    defaultConfig {
        minSdk = 22
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly(project(mapOf("path" to ":mvi")))
    compileOnly("org.junit.jupiter:junit-jupiter-api:5.8.2")
    compileOnly("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    implementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    implementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    implementation("org.mockito:mockito-junit-jupiter:4.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    implementation("androidx.arch.core:core-testing:2.2.0")
    implementation("androidx.core:core-testing:1.12.0")
}