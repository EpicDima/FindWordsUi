import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":core"))

    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.swing)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)
}

compose.desktop {
    application {
        mainClass = "com.epicdima.findwords.FindWordsMainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "FindWords"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "com.epicdima.findwords"
            }
        }
    }
}
