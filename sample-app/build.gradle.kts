plugins {
  alias(libs.plugins.convention.android.app)
  alias(libs.plugins.convention.android.app.compose)
  id("io.github.pawgli.basepackage") version "0.1.3"
}

android {
  namespace = "io.github.pawgli.basepackagesample"

  defaultConfig {
    applicationId = "io.github.pawgli.basepackagesample"
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
}

basePackagePlugin {
  basePackage = "io.github.pawgli"
  exclude("**/excludeddir/**", "**/excluded-file.txt")
}

dependencies {
  implementation(libs.androidx.activityCompose)

  testImplementation(libs.bundles.testSuite)
}
