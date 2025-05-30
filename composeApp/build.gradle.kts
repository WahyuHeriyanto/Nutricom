import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        val androidMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(platform("com.google.firebase:firebase-bom:33.4.0")) // Pastikan BOM ditambahkan
            implementation("com.google.firebase:firebase-auth-ktx") // Tidak perlu menambahkan lagi di dependencies Android
            implementation("com.google.firebase:firebase-analytics") // Sesuai dengan kebutuhan
            implementation("com.google.android.gms:play-services-auth:20.7.0")
            implementation ("androidx.navigation:navigation-compose:2.6.0")
            implementation ("androidx.media3:media3-exoplayer:1.0.0") // Versi dapat disesuaikan
            implementation ("androidx.media3:media3-ui:1.0.0")
            implementation("androidx.datastore:datastore-preferences:1.0.0")
            implementation("androidx.datastore:datastore:1.0.0")
            implementation("com.google.firebase:firebase-firestore:24.3.0")
            implementation ("org.tensorflow:tensorflow-lite:2.10.0")
            implementation ("com.google.firebase:firebase-storage-ktx")
            implementation("io.coil-kt:coil-compose:2.1.0")
            implementation ("androidx.work:work-runtime-ktx:2.7.1")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")
            implementation("com.google.mlkit:barcode-scanning:17.2.0")
            implementation ("androidx.camera:camera-core:1.3.0")
            implementation ("androidx.camera:camera-camera2:1.3.0")
            implementation ("androidx.camera:camera-lifecycle:1.3.0")
            implementation ("androidx.camera:camera-view:1.3.0")
            implementation ("com.google.android.gms:play-services-mlkit-barcode-scanning:18.2.0")
            implementation("com.airbnb.android:lottie-compose:6.2.0")



        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "org.wahyuheriyanto.nutricom"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.wahyuheriyanto.nutricom"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
        implementation ("androidx.compose.material3:material3:1.1.1")

    }
}
dependencies {
    implementation(libs.androidx.appcompat)
}

compose.desktop {
    application {
        mainClass = "org.wahyuheriyanto.nutricom.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.wahyuheriyanto.nutricom"
            packageVersion = "1.0.0"
        }
    }
}



