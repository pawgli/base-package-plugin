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
    replaceBasePackageInExistingFiles(projectDir, oldBasePackage, newBasePackage, exclusionPatterns)
    renameDirectories(projectDir, oldBasePackage, newBasePackage, exclusionPatterns, logger)
    removeEmptyDirectories(projectDir)
  }
}
