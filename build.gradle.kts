
plugins {
  kotlin("jvm") version "1.9.21"
  java
}

group   = "me.frank"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation    (group = "io.vavr",            name = "vavr",                   version = "0.10.4")
  implementation    (group = "io.github.serpro69", name = "kotlin-faker",           version = "2.0.0-rc.5")
  testImplementation(kotlin("test"))
  testImplementation(group = "io.strikt",          name = "strikt-core",            version = "0.34.1")
  testImplementation(group = "io.kotest",          name = "kotest-assertions-core", version = "5.5.4")
  testImplementation(group = "org.junit.jupiter",  name = "junit-jupiter-params",   version = "5.9.1")
}

tasks.test {
  useJUnitPlatform()
}
