plugins {
  `kotlin-dsl`
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  compileOnly(libs.kotlin.gradle)
}

gradlePlugin {
  plugins.register("BasePackagePlugin") {
    id = "BasePackagePlugin"
    implementationClass = "io.github.pawgli.BasePackagePlugin"
  }
}
