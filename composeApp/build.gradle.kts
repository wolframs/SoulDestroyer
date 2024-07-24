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

            // log4j
            // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api
            implementation("org.apache.logging.log4j:log4j-api:3.0.0-beta2")
            // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
            implementation("org.apache.logging.log4j:log4j-core:3.0.0-beta2")
            // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl
            implementation("org.apache.logging.log4j:log4j-slf4j-impl:3.0.0-beta2")

            // Stuff for funkatronics kBorsh
            implementation("io.github.funkatronics:multimult:0.2.0")
            implementation("com.ditchoom:buffer:1.3.0")
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
            // the following modules implementation is here to aid with a bug concerning logging and sqlite
            //modules("java.instrument", "java.management", "java.naming", "java.sql", "jdk.unsupported")
            packageName = "eu.woof.souldestro"
            packageVersion = "1.0.0"
            includeAllModules = true
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    ksp(libs.room.compiler)
}