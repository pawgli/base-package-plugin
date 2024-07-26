package io.github.pawgli.task

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File


class ReplaceBasePackageInExistingFilesKtTest {

  @TempDir
  lateinit var testFolder: File

  @BeforeEach
  fun setUp() {
    BasePackageInFilesTestData.initializeTestDirTree(testFolder)
  }

  @Test
  fun `should remove empty directories`() {
    replaceBasePackageInExistingFiles(
      projectDir = testFolder,
      oldBasePackage = BasePackageInFilesTestData.INITIAL_PACKAGE,
      newBasePackage = BasePackageInFilesTestData.NEW_PACKAGE,
      exclusionPatterns = BasePackageInFilesTestData.exclusionPatterns,
    )

    BasePackageInFilesTestData.EXCLUDED_ROOT_LEVEL_FILE.content() shouldBe BasePackageInFilesTestData.initialFileContent
    BasePackageInFilesTestData.ROOT_LEVEL_FILE.content() shouldBe BasePackageInFilesTestData.updatedFileContent
    BasePackageInFilesTestData.NESTED_FILE.content() shouldBe BasePackageInFilesTestData.updatedFileContent
    BasePackageInFilesTestData.EXCLUDED_NESTED_FILE.content() shouldBe BasePackageInFilesTestData.initialFileContent
    BasePackageInFilesTestData.FILE_1_FROM_EXCLUDED_DIR.content() shouldBe BasePackageInFilesTestData.initialFileContent
    BasePackageInFilesTestData.FILE_2_FROM_EXCLUDED_DIR.content() shouldBe BasePackageInFilesTestData.initialFileContent

  }

  private fun String.content() = File(testFolder, /* path */ this).readText()
}

private object BasePackageInFilesTestData {

  const val INITIAL_PACKAGE = "com.initial.package"
  const val NEW_PACKAGE = "pl.new.package"

  const val EXCLUDED_ROOT_LEVEL_FILE = "excludedRootLevelFile.kt"
  const val ROOT_LEVEL_FILE = "rootLevelFile.kt"

  private const val NESTED_DIR_NAME = "nested"
  const val NESTED_FILE = "$NESTED_DIR_NAME/nestedFile.txt"
  private const val EXCLUDED_NESTED_FILE_NAME = "excludedNestedFile.txt"
  const val EXCLUDED_NESTED_FILE = "$NESTED_DIR_NAME/$EXCLUDED_NESTED_FILE_NAME"

  private const val EXCLUDED_DIR_NAME = "excluded-dir"
  const val FILE_1_FROM_EXCLUDED_DIR = "$EXCLUDED_DIR_NAME/fileFromExcludedDir.kt"
  const val FILE_2_FROM_EXCLUDED_DIR = "$EXCLUDED_DIR_NAME/fileFromExcludedDir.txt"

  val exclusionPatterns = setOf("**/$EXCLUDED_ROOT_LEVEL_FILE", "**/$EXCLUDED_NESTED_FILE_NAME", "**/$EXCLUDED_DIR_NAME/**")

  val initialFileContent = """
    package $INITIAL_PACKAGE
    
    import $INITIAL_PACKAGE.SomeClass
    
    val myPackage = "$INITIAL_PACKAGE" 
  """.trimIndent()

  val updatedFileContent = """
    package $NEW_PACKAGE
    
    import $NEW_PACKAGE.SomeClass
    
    val myPackage = "$NEW_PACKAGE" 
  """.trimIndent()

  fun initializeTestDirTree(folder: File) {
    val excludedRootLevelFile = File(folder, EXCLUDED_ROOT_LEVEL_FILE)
    excludedRootLevelFile.createNewFile()
    excludedRootLevelFile.writeText(initialFileContent)

    val rootLevelFile = File(folder, ROOT_LEVEL_FILE)
    rootLevelFile.createNewFile()
    rootLevelFile.writeText(initialFileContent)

    File(folder, NESTED_DIR_NAME).mkdir()
    val nestedFile = File(folder, NESTED_FILE)
    nestedFile.createNewFile()
    nestedFile.writeText(initialFileContent)

    val excludedNestedFile = File(folder, EXCLUDED_NESTED_FILE)
    excludedNestedFile.createNewFile()
    excludedNestedFile.writeText(initialFileContent)

    File(folder, EXCLUDED_DIR_NAME).mkdir()
    val fileFromExcludedDir = File(folder, FILE_1_FROM_EXCLUDED_DIR)
    fileFromExcludedDir.createNewFile()
    fileFromExcludedDir.writeText(initialFileContent)

    val fileFromExcludedDir2 = File(folder, FILE_2_FROM_EXCLUDED_DIR)
    fileFromExcludedDir2.createNewFile()
    fileFromExcludedDir2.writeText(initialFileContent)
  }
}
