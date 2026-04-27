plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.secrets.gradle) // For Google Maps
    id("com.google.gms.google-services") // Add the Google services Gradle plugin
    // For Room
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.venu"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.venu"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose) // to use ViewModel
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.animation) // to use Navigation Component
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.7.7") // Added this navigation dep.
    implementation("androidx.compose.material:material-icons-extended") // For bookmark icon
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.10.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    // Maps SDK (Play services)
    implementation("com.google.android.gms:play-services-maps:20.0.0")
    // Maps Compose UI
    implementation("com.google.maps.android:maps-compose:6.12.0")
    // For Google Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    // For Google Places
    implementation(libs.places)
    // For Google Directions
    implementation(libs.google.google.maps.services)
    // To use ROOM local database
    implementation("androidx.room:room-runtime:2.8.4") // Room core
    implementation("androidx.room:room-ktx:2.8.4") // Kotlin extensions (coroutines support)
    ksp("androidx.room:room-compiler:2.8.4") // Annotation processor (REQUIRED)
}
