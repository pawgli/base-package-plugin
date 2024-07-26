package io.github.pawgli.task

import io.github.pawgli.utils.isNotExcluded
import io.github.pawgli.utils.withRetry
import org.gradle.api.logging.Logger
import java.io.File

private const val PACKAGE_SEPARATOR = '.'

internal fun moveFilesToNewDirectories(
  projectDir: File,
  oldBasePackage: String,
  newBasePackage: String,
  exclusionPatterns: Set<String>,
  logger: Logger,
) {
  val oldPath: String = oldBasePackage.replace(PACKAGE_SEPARATOR, File.separatorChar)
  val newPath = newBasePackage.replace(PACKAGE_SEPARATOR, File.separatorChar)

  projectDir.walkTopDown().forEach { file ->
    val isNotExcluded = file.toPath().isNotExcluded(exclusionPatterns)
    if (file.isDirectory && file.path.contains(oldPath) && isNotExcluded) {
      file.renameDirectory(oldPath, newPath, logger)
    }
  }
}

private fun File.renameDirectory(oldPath: String, newPath: String, logger: Logger) {
  val newDirPath = path.replace(oldPath, newPath)
  val newDir = File(newDirPath)
  newDir.parentFile.mkdirs()
  val isRenamingSuccessful = withRetry { renameTo(newDir) }
  if (isRenamingSuccessful) {
    logger.lifecycle("Renamed directory: $path to $newDirPath")
  } else {
    logger.error("Failed to rename directory: $path to $newDirPath")
  }
}
