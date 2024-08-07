package io.github.pawgli.task

import org.gradle.api.logging.Logger
import java.io.File

class ReplaceBasePackage(
  private val projectDir: File,
  private val oldBasePackage: String,
  private val newBasePackage: String,
  private val logger: Logger,
  private val exclusionPatterns: Set<String>
) {

  operator fun invoke() {
    if (newBasePackage == oldBasePackage) {
      logger.lifecycle("The new base package is the same as the old one.")
      return
    }
    replaceBasePackageInExistingFiles(projectDir, oldBasePackage, newBasePackage, exclusionPatterns)
    moveFilesToNewDirectories(projectDir, oldBasePackage, newBasePackage, exclusionPatterns, logger)
    removeEmptyDirectories(projectDir)
  }
}
