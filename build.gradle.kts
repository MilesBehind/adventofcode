import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.20"
}

group   = "me.frank"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  testImplementation(group = "io.strikt",         name = "strikt-core",            version = "0.34.1")
  testImplementation(group = "io.kotest",         name = "kotest-assertions-core", version = "5.5.4")
  testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params",   version = "5.9.1")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "17"
}