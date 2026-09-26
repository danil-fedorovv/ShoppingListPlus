import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {

    namespace = "danilfedorov.shoppinglistplus"
    compileSdk = 36

    defaultConfig {
        applicationId = "danilfedorov.shoppinglistplus"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "2.3.11-grape"

        setProperty("archivesBaseName", "slp-$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    val keystorePropsFile = rootProject.file("keystore.properties")
    val keystoreProps = Properties()

    if (keystorePropsFile.exists()) {
        keystoreProps.load(keystorePropsFile.inputStream())
    }

    signingConfigs {
        create("release") {
            storeFile = keystoreProps["storeFile"]?.let { file(it as String) }
            storePassword = keystoreProps["storePassword"] as String?
            keyAlias = keystoreProps["keyAlias"] as String?
            keyPassword = keystoreProps["keyPassword"] as String?
        }
    }

    flavorDimensions += "store"

    val secretsProps = Properties()
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        secretsProps.load(secretsFile.inputStream())
    }

    productFlavors {
        create("RuStore") {
            dimension = "store"
            buildConfigField("String", "YANDEX_BANNER_KEY", "\"${secretsProps.getProperty("YANDEX_BANNER_KEY_RUSTORE") ?: ""}\"")
            buildConfigField("String", "YANDEX_REWARDED_KEY", "\"${secretsProps.getProperty("YANDEX_REWARDED_KEY_RUSTORE") ?: ""}\"")
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
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-domain"))
    implementation(project(":common"))
    implementation(project(":feature-shoppinglist"))
    implementation(project(":feature-productlist"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-archive"))
    implementation(project(":feature-add-product"))

    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation("com.google.firebase:firebase-common")

    implementation(platform("ru.rustore.sdk:bom:2025.08.01"))
    implementation("ru.rustore.sdk:pay")

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