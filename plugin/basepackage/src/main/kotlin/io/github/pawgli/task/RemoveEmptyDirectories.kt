package io.github.pawgli.task

import java.io.File

internal fun removeEmptyDirectories(projectDir: File) {
  projectDir.walkBottomUp().forEach { file ->
    if (file.isDirectory && file.listFiles()?.isEmpty() == true) {
      file.deleteAlongWithEmptyParents()
    }
  }
}

private fun File.deleteAlongWithEmptyParents() {
  val parent = this.parentFile
  if (this.delete()) {
    parent?.let {
      if (it.isDirectory && it.listFiles()?.isEmpty() == true) {
        it.deleteAlongWithEmptyParents()
      }
    }
  }
}
