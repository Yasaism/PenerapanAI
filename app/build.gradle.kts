plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.tugaspenerapanai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tugaspenerapanai"
        minSdk = 24
        targetSdk = 34
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
        viewBinding = true
    }
}


dependencies {
    // Core Android Libraries
    implementation("androidx.core:core-ktx:1.10.1")       // Android Core KTX extensions
    implementation("androidx.appcompat:appcompat:1.6.1")   // AppCompat untuk kompatibilitas mundur
    implementation("com.google.android.material:material:1.9.0") // Komponen Material Design
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // ConstraintLayout
    implementation("androidx.recyclerview:recyclerview:1.2.1") // RecyclerView untuk menampilkan daftar

    // Lifecycle Components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1") // Lifecycle runtime
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1") // ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1") // LiveData

    // Testing
    testImplementation("junit:junit:4.13.2")                  // Pengujian unit dengan JUnit
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Dukungan JUnit untuk pengujian Android
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Espresso untuk pengujian UI

    // Retrofit untuk panggilan API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    // Kotlin Coroutines untuk tugas latar belakang
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Opsional: Glide untuk memuat gambar
    implementation("com.github.bumptech.glide:glide:4.15.1") // Glide untuk memuat gambar
}