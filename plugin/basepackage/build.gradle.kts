plugins {
  `kotlin-dsl`
  alias(libs.plugins.convention.jvm.library)
}

dependencies {
  compileOnly(libs.kotlin.gradle)

  testImplementation(libs.bundles.testSuite)
}

gradlePlugin {
  plugins.register("BasePackagePlugin") {
    id = "BasePackagePlugin"
    implementationClass = "io.github.pawgli.BasePackagePlugin"
  }
}
