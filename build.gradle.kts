import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.0"
}

group = "me.frank"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  testImplementation (group = "io.kotlintest", name = "kotlintest-assertions", version = "3.4.2") {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
  }
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "11"
}