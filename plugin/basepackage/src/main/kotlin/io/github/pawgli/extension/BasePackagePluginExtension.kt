package io.github.pawgli.extension

import io.github.pawgli.BasePackagePlugin
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/**
 * The extension for the [BasePackagePlugin], allowing to set up the plugin's properties.
 *
 * @property basePackage The current base package of the project.
 * @property exclusionPatterns The set of glob patterns that should be excluded
 * when updating the base package in files. Files matching these patterns will not be updated.
 */
interface BasePackagePluginExtension {
  val basePackage: Property<String>
  // TODO: Add newPackage property
  val exclusionPatterns: SetProperty<String>

  /**
   * Sets the glob patterns to exclude when updating the base package in files.
   *
   * @param patterns The glob patterns to exclude.
   */
  fun exclude(vararg patterns: String) {
    exclusionPatterns.set(patterns.toSet())
  }
}
