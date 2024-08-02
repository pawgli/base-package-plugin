package io.github.pawgli.utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Path

class IsExcludedKtTest {

  @Test
  fun `path is excluded if the file's name matches the pattern`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/some_file.py")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe true
  }

  @Test
  fun `path is not excluded if the file's name does not match the pattern`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/other_file.py")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe false
  }

  @Test
  fun `path is excluded if the file's extension matches the pattern`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/*.py")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe true
  }

  @Test
  fun `path is not excluded if the file's extension does not match the pattern`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/*.c")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe false
  }

  @Test
  fun `path is excluded if its directory matches the pattern`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/dir/**")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe true
  }

  @Test
  fun `path is not excluded if its directory does not match the pattern`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/otherdir/**")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe false
  }

  @Test
  fun `path is excluded if at least one of the exclusion criteria is met`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/otherdir/**", "**/*.c", "**/some_file.*")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe true
  }

  @Test
  fun `path is excluded if it matches multiple exclusion patterns`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf("**/dir/**", "**/*.py")

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe true
  }

  @Test
  fun `path is not excluded if exclusion patterns set is empty`() {
    val path = Path.of("some/dir/some_file.py")
    val exclusionPatterns = setOf<String>()

    val result = path.isExcluded(exclusionPatterns)

    result shouldBe false
  }
}
