plugins {
    kotlin("jvm") version "1.9.20-Beta2"
    kotlin("plugin.serialization") version "1.9.20-Beta2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
    id("com.github.ben-manes.versions") version "0.48.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.caffeine-moe:octane:main-SNAPSHOT")
    implementation("io.arrow-kt:arrow-core:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.21")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("commons-validator:commons-validator:1.7")
    implementation("com.github.lalyos:jfiglet:0.0.9")
    implementation("com.github.oshi:oshi-core:6.4.6")
}