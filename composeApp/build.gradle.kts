import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting

        val metaplexSolanaVersion = "0.2.10"
        val serializationVersion = "1.7.0"
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            implementation("foundation.metaplex:solana:$metaplexSolanaVersion")
            implementation("foundation.metaplex:rpc:$metaplexSolanaVersion")
            implementation("foundation.metaplex:solanaeddsa:$metaplexSolanaVersion")

            // Room Database dependencies
            implementation(libs.room.runtime)
            implementation(libs.room.compiler)
            implementation(libs.sqlite.bundled)

            // Kotlin Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

            // Kotlin Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)

            // Compose Navigation
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")

            // MaterialKolor https://github.com/jordond/MaterialKolor
            implementation(libs.material.kolor)

            // Crypto
            implementation("org.bitcoinj:bitcoinj-core:0.16.2")
            implementation("net.i2p.crypto:eddsa:0.3.0")
            implementation("com.diglol.crypto:pkc:0.2.0")

            // OkHttp3
            implementation("com.squareup.okhttp3:okhttp:4.9.0")
        }
        /*jvmMain.dependencies {
            implementation(libs.koin.core)
        }*/
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "eu.woof.souldestro"
            packageVersion = "1.0.0"
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    ksp(libs.room.compiler)
}