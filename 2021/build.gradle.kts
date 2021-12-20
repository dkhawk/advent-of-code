import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
}

group = "me.dkhawk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

object Versions {
    //    const val gradle = "3.5.0"
    const val kotlin = "1.6.0"
//    const val appcompat = "1.0.2"

    /* test */
//    const val junit = "4.12"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.truth:truth:1.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}