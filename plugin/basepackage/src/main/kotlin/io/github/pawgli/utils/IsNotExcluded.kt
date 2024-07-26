package io.github.pawgli.utils

import java.nio.file.FileSystems
import java.nio.file.Path

internal fun Path.isExcluded(exclusionPatterns: Set<String>): Boolean {
  val exclusionMatchers = exclusionPatterns.map {
    FileSystems.getDefault().getPathMatcher("glob:$it")
  }
  return exclusionMatchers.any { it.matches(this) }
}
