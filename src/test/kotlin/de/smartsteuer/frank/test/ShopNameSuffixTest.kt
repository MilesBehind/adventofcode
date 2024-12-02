package de.smartsteuer.frank.test

import de.smartsteuer.frank.test.ShopNameSuffixJava.getNameSuffix
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ShopNameSuffixTest {
  @Test
  fun `names can be combined to suffix`() {
    getNameSuffix("",    "")     shouldBe "()"
    getNameSuffix("Max", "")     shouldBe "(Max)"
    getNameSuffix("",    "Susi") shouldBe "(Susi)"
    getNameSuffix("Max", "Susi") shouldBe "(Max + Susi)"
    shouldThrow<NullPointerException> { getNameSuffix("Max", null) }
    shouldThrow<NullPointerException> { getNameSuffix(null, "Susi") }
    shouldThrow<NullPointerException> { getNameSuffix(null, null) }
  }

  @Test
  fun `names can be combined to suffix 2`() {
    getNameSuffix2("",    "")     shouldBe ""
    getNameSuffix2("Max", "")     shouldBe "(Max)"
    getNameSuffix2("",    "Susi") shouldBe "(Susi)"
    getNameSuffix2("Max", "Susi") shouldBe "(Max + Susi)"
    getNameSuffix2("Max", null)   shouldBe "(Max)"
    getNameSuffix2(null,  "Susi") shouldBe "(Susi)"
    getNameSuffix2(null,  null)   shouldBe ""
  }
}