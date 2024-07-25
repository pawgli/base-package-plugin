package io.github.pawgli.task

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class RemoveEmptyDirectoriesKtTest {

  @TempDir
  lateinit var testFolder: File

  @BeforeEach
  fun setUp() {
    EmptyDirectoriesTestData.initializeTestDirTree(testFolder)
  }

  @Test
  fun `should remove empty directories`() {
    removeEmptyDirectories(testFolder)

    val actualDirTree = testFolder.walkTopDown()
      .map { it.relativeTo(testFolder).toString() }
      .filter { it.isNotEmpty() } // Root directory
      .toList()

    actualDirTree shouldBe EmptyDirectoriesTestData.expectedDirTree
  }
}

private object EmptyDirectoriesTestData {

  fun initializeTestDirTree(folder: File) {
    File(folder, "rootLevelEmptyDir").mkdir()

    val rootLevelNonEmptyDir = File(folder, "rootLevelNonEmptyDir")
    rootLevelNonEmptyDir.mkdir()
    File(rootLevelNonEmptyDir, "File.kt").createNewFile()

    File(folder, "nested").mkdir()
    File(folder, "nested/emptyDir").mkdir()

    val nestedNonEmptyDir = File(folder, "nested/nonEmptyDir")
    nestedNonEmptyDir.mkdir()
    File(nestedNonEmptyDir, "file.txt").createNewFile()
  }

  val expectedDirTree = listOf(
    "rootLevelNonEmptyDir",
    "rootLevelNonEmptyDir/File.kt",
    "nested",
    "nested/nonEmptyDir",
    "nested/nonEmptyDir/file.txt",
  )
}
