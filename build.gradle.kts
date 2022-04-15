plugins {
    application
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
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
    maven(url = "https://m2.dv8tion.net/releases")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation("commons-validator:commons-validator:1.7")
    implementation("io.ktor:ktor-client-core:2.0.0")
    implementation("io.ktor:ktor-client-cio:2.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.0")
}
