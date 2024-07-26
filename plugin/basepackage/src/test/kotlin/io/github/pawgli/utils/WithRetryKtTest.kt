import io.github.pawgli.utils.withRetry
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class WithRetryKtTest {

  @Test
  fun `withRetry returns true when action succeeds on first try`() {
    var actionCount = 0
    val action = { actionCount++; true }

    val result = withRetry(tries = 3, action)

    result shouldBe false
    actionCount shouldBe 1
  }

  @Test
  fun `withRetry returns true when action fails first but succeeds on second try`() {
    var actionCount = 0
    val action = { actionCount++; actionCount == 2 }

    val result = withRetry(tries = 3, action)

    result shouldBe true
    actionCount shouldBe 2
  }

  @Test
  fun `withRetry returns true when action fails first but succeeds on fourth try`() {
    var actionCount = 0
    val action = { actionCount++; actionCount == 4 }

    val result = withRetry(tries = 4, action)

    result shouldBe true
    actionCount shouldBe 4
  }

  @Test
  fun `withRetry returns false when action fails all tries`() {
    var actionCount = 0
    val action = { actionCount++; false }

    val result = withRetry(tries = 3, action)

    result shouldBe false
    actionCount shouldBe 3
  }
}
