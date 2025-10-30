import com.android.build.gradle.ProguardFiles.getDefaultProguardFile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.avdev.core_data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
dependencies {
    implementation(project(":core-domain"))
    implementation("androidx.datastore:datastore-preferences:1.1.7")


    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}