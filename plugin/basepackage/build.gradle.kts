plugins {
  `kotlin-dsl`
  alias(libs.plugins.convention.jvm.library)
  alias(libs.plugins.convention.publishing)
}

dependencies {
  compileOnly(libs.kotlin.gradle)

  testImplementation(libs.bundles.testSuite)
}
