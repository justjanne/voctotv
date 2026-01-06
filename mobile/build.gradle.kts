import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dagger.hilt)
}

configure<BasePluginExtension> {
    archivesName.set("${rootProject.name}-$name")
}

android {
    namespace = "de.justjanne.voctotv"
    compileSdk = 36

    fun Project.git(vararg command: String): Provider<String> =
        providers.exec { commandLine("git", *command) }
            .standardOutput
            .asText
            .map { it.trim() }

    defaultConfig {
        applicationId = "de.justjanne.voctotv"
        minSdk = 26
        targetSdk = 36
        versionCode = git("rev-list", "--count", "HEAD", "--tags").orNull?.toIntOrNull() ?: 1
        versionName = git("describe", "--always", "--tags", "HEAD").getOrElse("0.1.0")
    }

    buildFeatures {
        buildConfig = true
    }

    data class SigningData(
        val storeFile: String,
        val storePassword: String,
        val keyAlias: String,
        val keyPassword: String
    )

    fun signingData(properties: Properties?): SigningData? {
        if (properties == null) return null

        val storeFile = properties.getProperty("storeFile") ?: return null
        val storePassword = properties.getProperty("storePassword") ?: return null
        val keyAlias = properties.getProperty("keyAlias") ?: return null
        val keyPassword = properties.getProperty("keyPassword") ?: return null

        return SigningData(storeFile, storePassword, keyAlias, keyPassword)
    }

    fun Project.properties(fileName: String): Provider<Properties> =
        providers.fileContents(rootProject.layout.projectDirectory.file(fileName))
            .asBytes
            .map { Properties().apply { load(it.inputStream()) } }

    signingConfigs {
        signingData(rootProject.properties("signing.properties").orNull)?.let {
            register("release") {
                storeFile = file(it.storeFile)
                storePassword = it.storePassword
                keyAlias = it.keyAlias
                keyPassword = it.keyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.material3)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.extractor)
    implementation(libs.androidx.media3.ui)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":api"))
    implementation(project(":viewmodel"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.coil.network.okhttp)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
}
