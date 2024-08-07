package io.github.pawgli.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import java.util.Optional

internal fun Project.getLibrary(alias: String): Provider<MinimalExternalModuleDependency> =
  libs.findLibrary(alias).get()

internal fun Project.getVersion(alias: String): String = libs.findVersion(alias).getAsString()

internal inline fun <reified T> Project.requireProperty(
  key: String,
  default: T? = null,
): T {
  val property = findProperty(key)
  return if (property is T) {
    property
  } else {
    default ?: error("Property $key not found or not of expected type")
  }
}

internal val Project.libs
  get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Optional<VersionConstraint>.getAsString() = get().toString()
