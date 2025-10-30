import com.android.build.gradle.ProguardFiles.getDefaultProguardFile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
}

android {

    namespace = "danilfedorov.shoppinglistplus"
    compileSdk = 36

    defaultConfig {
        applicationId = "danilfedorov.shoppinglistplus"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.1-grape"

        setProperty("archivesBaseName", "shoppingList $versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-domain"))
    implementation(project(":common"))
    implementation(project(":feature-shoppinglist"))
    implementation(project(":feature-productlist"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-archive"))

    implementation(libs.material)

    implementation("com.yandex.android:mobileads:7.16.1")

    implementation(libs.dagger)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.material3)
    ksp(libs.dagger.compiler)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.foundation)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}