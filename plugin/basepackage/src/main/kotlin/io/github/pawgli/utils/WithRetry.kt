package io.github.pawgli.utils

internal fun withRetry(tries: Int = 3, action: () -> Boolean): Boolean {
  var triesCount = 0
  do {
    if (action()) return true
    triesCount++
  } while (triesCount < tries)
  return false
}
