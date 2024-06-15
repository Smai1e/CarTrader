import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("androidx.navigation.safeargs.kotlin")
}

val signingPropertiesFile = rootProject.file("signing.properties")
val properties = Properties()
if (signingPropertiesFile.exists()) {
    signingPropertiesFile.inputStream().use {
        properties.load(it)
    }
}

android {
    namespace = "dev.smai1e.carTrader"
    compileSdk = libs.versions.androidSdk.compile.get().toInt()
    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "dev.smai1e.carTrader"
        minSdk = libs.versions.androidSdk.min.get().toInt()
        targetSdk = libs.versions.androidSdk.target.get().toInt()
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations.addAll(listOf("en", "ru"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file(properties.getProperty("RELEASE_STORE_FILE"))
            keyAlias = properties.getProperty("RELEASE_KEY_ALIAS")
            keyPassword = properties.getProperty("RELEASE_KEY_PASSWORD")
            storePassword = properties.getProperty("RELEASE_STORE_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "URL", "\"https://cartrader.ru/\"")
            buildConfigField("String", "WEBSOCKET_URL", "\"ws://cartrader.ru\"")
        }
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "URL", "\"http://10.0.2.2:8080/\"")
            buildConfigField("String", "WEBSOCKET_URL", "\"ws://10.0.2.2:8080\"")
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.kotlinxSerialization)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.logback)

    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.viewpager)
    implementation(libs.androidx.recyclerview)

    implementation(libs.lottie)
    implementation(libs.roundedimageview)
    implementation(libs.filepicker)
    implementation(libs.photoview)
    implementation(libs.masked.edittext)

    implementation(libs.glide)
    ksp(libs.glide.compiler)

    implementation(libs.vico.core)
    implementation(libs.vico.compose)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}