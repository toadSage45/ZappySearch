plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    alias(libs.plugins.google.firebase.crashlytics)
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.zappysearch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.zappysearch"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "MAPS_API_KEY", "\"${properties["MAPS_API_KEY"]}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}



dependencies {
    val placesVersion = "3.4.0"
    implementation("com.google.android.libraries.places:places:$placesVersion")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // Google Maps Compose library
    val mapsComposeVersion = "4.4.1"
    implementation("com.google.maps.android:maps-compose:$mapsComposeVersion")
    // Google Maps Compose utility library
    implementation("com.google.maps.android:maps-compose-utils:$mapsComposeVersion")
    // Google Maps Compose widgets library
    implementation("com.google.maps.android:maps-compose-widgets:$mapsComposeVersion")

    implementation("com.google.android.gms:play-services-location:21.0.1")

    //dot-lottie
    implementation("com.github.LottieFiles:dotlottie-android:0.8.0")
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    //coil for image
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    kapt("com.google.dagger:hilt-compiler:2.56.2")

    //Hilt Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // ViewModel
    val lifecycle_version = "2.9.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}

kapt {
    correctErrorTypes =  true
}