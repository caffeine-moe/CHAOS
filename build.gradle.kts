plugins {
    application
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
    id("com.github.ben-manes.versions") version "0.42.0"
}

application {
    mainClass.set("org.caffeine.chaos.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.caffeine.chaos.MainKt"
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    implementation("commons-validator:commons-validator:1.7")
    implementation("io.ktor:ktor-client-core:2.1.3")
    implementation("io.ktor:ktor-client-cio:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("com.github.lalyos:jfiglet:0.0.9")
    implementation("com.github.oshi:oshi-core:6.3.2")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.1.2")
    implementation("com.github.sealedtx:java-youtube-downloader:3.1.0")
}