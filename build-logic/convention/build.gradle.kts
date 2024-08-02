plugins {
  `kotlin-dsl`
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  compileOnly(libs.android.gradle)
  compileOnly(libs.kotlin.gradle)
  compileOnly(libs.vanniktech.mavenPublish)
}

gradlePlugin {
  plugins {
    registerConvention("AndroidAppConventionPlugin")
    registerConvention("AndroidAppComposeConventionPlugin")
    registerConvention("AndroidLibraryConventionPlugin")
    registerConvention("AndroidLibraryComposeConventionPlugin")
    registerConvention("JavaLibraryConventionPlugin")
    registerConvention("PublishingConventionPlugin")
  }
}

fun NamedDomainObjectContainer<PluginDeclaration>.registerConvention(name: String) {
  register(name) {
    id = name
    implementationClass = name
  }
}
