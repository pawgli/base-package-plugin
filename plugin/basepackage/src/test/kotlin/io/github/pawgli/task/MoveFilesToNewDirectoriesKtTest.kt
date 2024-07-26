package io.github.pawgli.task

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.gradle.api.logging.Logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class MoveFilesToNewDirectoriesKtTest {

@TempDir
  lateinit var testFolder: File

  @BeforeEach
  fun setUp() {
    RenameDirectoriesTestData.initializeTestDirTree(testFolder)
  }

  @Test
  fun `should move files to new directories based on a new package`() {
    // arrange
    val loggerMock: Logger = mockk {
      every { lifecycle(any()) } just runs
    }

    // act
    moveFilesToNewDirectories(
      projectDir = testFolder,
      oldBasePackage = RenameDirectoriesTestData.INITIAL_PACKAGE,
      newBasePackage = RenameDirectoriesTestData.NEW_PACKAGE,
      exclusionPatterns = RenameDirectoriesTestData.exclusionPatterns,
      logger = loggerMock,
    )


    // assert
    val actualDirTree = testFolder.walkTopDown()
      .map { it.relativeTo(testFolder).toString() }
      .filter { it.isNotEmpty() } // Root directory
      .toList()

    println(actualDirTree)
    println(RenameDirectoriesTestData.expectedDirTree)

    actualDirTree shouldBe RenameDirectoriesTestData.expectedDirTree
  }
}

private object RenameDirectoriesTestData {

  const val INITIAL_PACKAGE = "com.initial.package"
  const val NEW_PACKAGE = "pl.new.package"

  private const val EXCLUDED_DIR_NAME = "excludeddir"
  val exclusionPatterns = setOf("**/$EXCLUDED_DIR_NAME/**")

  val expectedDirTree = listOf(
    "pl",
    "pl/new",
    "pl/new/package",
    "pl/new/package/targetdir",
    "pl/new/package/targetdir/file.txt",

    "excludeddir",
    "excludeddir/com",
    "excludeddir/com/initial",
    "excludeddir/com/initial/package",
    "excludeddir/com/initial/package/targetdir",
    "excludeddir/com/initial/package/targetdir/file.txt",

    "nested",
    "nested/pl",
    "nested/pl/new",
    "nested/pl/new/package",
    "nested/pl/new/package/targetdir",
    "nested/pl/new/package/targetdir/file.txt",

    // Empty leftovers are removed by other use case
    "nested/com",
    "nested/com/initial",

    "com",
    "com/initial",
  )

  fun initializeTestDirTree(folder: File) {
    File(folder, "com").mkdir()
    File(folder, "com/initial").mkdir()
    File(folder, "com/initial/package").mkdir()
    val rootTargetDir = File(folder, "com/initial/package/targetdir")
    rootTargetDir.mkdir()
    File(rootTargetDir, "file.txt").createNewFile()

    File(folder, "nested").mkdir()
    File(folder, "nested/com").mkdir()
    File(folder, "nested/com/initial").mkdir()
    File(folder, "nested/com/initial/package").mkdir()
    val nestedTargetDir = File(folder, "nested/com/initial/package/targetdir")
    nestedTargetDir.mkdir()
    File(nestedTargetDir, "file.txt").createNewFile()

    File(folder, EXCLUDED_DIR_NAME).mkdir()
    File(folder, "$EXCLUDED_DIR_NAME/com").mkdir()
    File(folder, "$EXCLUDED_DIR_NAME/com/initial").mkdir()
    File(folder, "$EXCLUDED_DIR_NAME/com/initial/package").mkdir()
    val excludedTargetDir = File(folder, "$EXCLUDED_DIR_NAME/com/initial/package/targetdir")
    excludedTargetDir.mkdir()
    File(excludedTargetDir, "file.txt").createNewFile()
  }
}
