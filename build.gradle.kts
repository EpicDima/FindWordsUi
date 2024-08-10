import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":core"))

    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "com.epicdima.findwords.MainKt"

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
