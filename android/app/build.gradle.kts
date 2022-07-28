plugins {
    id("com.android.application") version "7.0.4"
    kotlin("android") version "1.7.0"
    kotlin("kapt") version "1.7.0"
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "de.stuermerbenjamin.ble"
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }

    composeOptions {
        // check mapping table: https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xallow-unstable-dependencies"
        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi"
            freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.flow.InternalCoroutinesApi"
            freeCompilerArgs + "-Xopt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi"
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

    packagingOptions {
        resources.excludes.add("**/attach_hotspot_windows.dll")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/licenses/ASM")
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")

    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")

    // Kotlin components
    val coroutines = "1.3.4"
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    // Compose
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
    implementation("androidx.navigation:navigation-compose:2.4.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")
    // Needed for createComposeRule, but not createAndroidComposeRule:
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.1")

    // Accompanist
    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")
    implementation("com.google.accompanist:accompanist-insets:0.23.1")
    implementation("com.google.accompanist:accompanist-pager:0.23.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.23.1")
    implementation("com.google.accompanist:accompanist-permissions:0.23.1")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.23.1")

    // data binding
    kapt("com.android.databinding:compiler:4.0.1")

    // Material design
    val materialVersion = "1.4.0"
    implementation("com.google.android.material:material:$materialVersion")

    // Koin
    implementation("io.insert-koin:koin-core:3.1.4")
    testImplementation("io.insert-koin:koin-test:3.1.4")
    implementation("io.insert-koin:koin-android:3.2.0")
    implementation("io.insert-koin:koin-androidx-navigation:3.1.4")
    implementation("io.insert-koin:koin-androidx-compose:3.2.0")

    // for new API replaced startActivityForResult
    implementation("androidx.fragment:fragment-ktx:1.4.0")

    // Mocking
    androidTestApi("io.mockk:mockk-android:1.12.0")
    androidTestApi("org.mockito:mockito-core:3.11.2")
    androidTestApi("org.mockito:mockito-inline:3.10.0")
    androidTestApi("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}
