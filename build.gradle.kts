plugins {
    application
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("java")
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
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("commons-validator:commons-validator:1.7")
    implementation("io.ktor:ktor-client-core:2.0.1")
    implementation("io.ktor:ktor-client-cio:2.0.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
    implementation("com.github.lalyos:jfiglet:0.0.9")
    implementation("com.github.oshi:oshi-core:6.1.6")
}