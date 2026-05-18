plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zano.mistcafe"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.zano.mistcafe"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.12.0")

    implementation("androidx.core:core-splashscreen:1.2.0")

    //authtoken preferences
    implementation("androidx.datastore:datastore-preferences:1.2.0")
    //ui material3
    implementation("androidx.compose.material3:material3:1.3.0")// Check for latest version
    implementation("com.google.android.material:material:1.11.0-alpha01")
//viewModel and hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-compiler:2.57.2")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.36.0")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    //map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:2.11.4")

    //Google fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.10.0")
    //navigation
    implementation("androidx.navigation:navigation-compose:2.9.6")

    //lottie ani
    implementation("com.airbnb.android:lottie-compose:6.1.0")

}