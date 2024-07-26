package io.github.pawgli.task

import io.github.pawgli.task.utils.randomString
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verifySequence
import org.gradle.api.logging.Logger
import org.junit.jupiter.api.Test
import java.io.File

class ReplaceBasePackageTest {

  @Test
  fun `replaces old package occurrences with new ones`() = mockkStatic(::replaceBasePackageInExistingFiles, ::moveFilesToNewDirectories, ::removeEmptyDirectories) {
    // arrange
    val oldBasePackage = randomString()
    val newBasePackage = randomString()
    val exclusionPatterns = setOf(randomString(), randomString())

    val loggerMock: Logger = mockk()
    val projectDirMock: File = mockk()

    every { replaceBasePackageInExistingFiles(projectDirMock, oldBasePackage, newBasePackage, exclusionPatterns) } just runs
    every { moveFilesToNewDirectories(projectDirMock, oldBasePackage, newBasePackage, exclusionPatterns, loggerMock) } just runs
    every { removeEmptyDirectories(projectDirMock) } just runs

    val subject = ReplaceBasePackage(projectDirMock, oldBasePackage, newBasePackage, loggerMock, exclusionPatterns)

    // act
    subject.invoke()

    // assert
    verifySequence {
      replaceBasePackageInExistingFiles(projectDirMock, oldBasePackage, newBasePackage, exclusionPatterns)
      moveFilesToNewDirectories(projectDirMock, oldBasePackage, newBasePackage, exclusionPatterns, loggerMock)
      removeEmptyDirectories(projectDirMock)
    }
  }
}
