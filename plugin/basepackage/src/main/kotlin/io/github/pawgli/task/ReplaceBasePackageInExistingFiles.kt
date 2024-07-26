package io.github.pawgli.task

import io.github.pawgli.utils.isExcluded
import java.io.File

internal fun replaceBasePackageInExistingFiles(
  projectDir: File,
  oldBasePackage: String,
  newBasePackage: String,
  exclusionPatterns: Set<String>,
) {
  projectDir.walkTopDown().forEach { file ->
    val filePath = file.toPath()
    if (file.isFile && !filePath.isExcluded(exclusionPatterns)) {
      var text = file.readText()
      text = text.replace(oldBasePackage, newBasePackage)
      file.writeText(text)
    }
  }
}
