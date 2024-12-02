package de.smartsteuer.frank.adventofcode2023.day24

import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import java.lang.ArithmeticException

import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.rational
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.r
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.over
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.plus
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.minus
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.times
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.div
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.abs
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.min
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.max
import de.smartsteuer.frank.adventofcode2023.day24.Rational.Companion.percentOf

internal class RationalTest {
  @Test
  fun `rationals can be created by using default constructor`() {
    Rational().toString() shouldBe "0"
  }

  @Test
  fun `rationals can be specified as integer, string and double values`() {
    10.rational.toString() shouldBe          "10"
    (-10).rational.toString() shouldBe         "-10"
    0.rational.toString() shouldBe           "0"
    1_000_000_000.rational.toString() shouldBe  "1000000000"
    (-1_000_000_000).rational.toString() shouldBe "-1000000000"

    10.0.rational.toString() shouldBe          "10"
    (-10.0).rational.toString() shouldBe         "-10"
    0.0.rational.toString() shouldBe           "0"
    1_000_000_000.0.rational.toString() shouldBe  "1000000000"
    (-1_000_000_000.0).rational.toString() shouldBe "-1000000000"

    10.1.rational.toString(1) shouldBe          "10.1"
    (-10.1).rational.toString(1) shouldBe         "-10.1"
    0.1.rational.toString(1) shouldBe           "0.1"
    1_000_000_000.1.rational.toString(1) shouldBe  "1000000000.1"
    (-1_000_000_000.1).rational.toString(1) shouldBe "-1000000000.1"

    "10".rational.toString() shouldBe          "10"
    "-10".rational.toString() shouldBe         "-10"
    "0".rational.toString() shouldBe           "0"
    "1000000000".rational.toString() shouldBe  "1000000000"
    "-1000000000".rational.toString() shouldBe "-1000000000"

    "10.0".rational.toString() shouldBe          "10"
    "-10.0".rational.toString() shouldBe         "-10"
    "0.0".rational.toString() shouldBe           "0"
    "1000000000.0".rational.toString() shouldBe  "1000000000"
    "-1000000000.0".rational.toString() shouldBe "-1000000000"

    "10.1".rational.toString(1) shouldBe          "10.1"
    "-10.1".rational.toString(1) shouldBe         "-10.1"
    "0.1".rational.toString(1) shouldBe           "0.1"
    "1000000000.1".rational.toString(1) shouldBe  "1000000000.1"
    "-1000000000.1".rational.toString(1) shouldBe "-1000000000.1"

    "40/16".rational.toString(1) shouldBe   "2.5"
    "5/10".rational.toString(1) shouldBe   "0.5"
    "100/1".rational.toString(1) shouldBe "100.0"
    "1024/256".rational.toString(1) shouldBe   "4.0"
    "3/126".rational.toString(1) shouldBe   "0.0"
    "3/126".rational.toString(2) shouldBe   "0.02"
    "126/3".rational.toString(1) shouldBe  "42.0"

    shouldThrow<NumberFormatException> { "".rational }
  }

  @Test
  fun `rationals can be added`() {
    10.rational +    20.rational shouldBe 30.rational
    10.rational +     0.rational shouldBe 10.rational
    0.rational +    10.rational shouldBe 10.rational
    10.rational + (-10).rational shouldBe  0.rational

    (101 over 10) +  (202 over 10) shouldBe (303 over 10)
    (101 over 10) +  0.rational    shouldBe (101 over 10)
    0.rational +  (109 over 10) shouldBe (109 over 10)
    (101 over 10) + (-101 over 10) shouldBe 0.rational

    (101 over 10) +    10 shouldBe (201 over 10)
    (101 over 10) +     0 shouldBe (101 over 10)
    0.rational +    10 shouldBe ( 10 over  1)
    (101 over 10) + (-10) shouldBe   (1 over 10)

    10 + (101 over 10) shouldBe (201 over 10)
    0 + (101 over 10) shouldBe (101 over 10)
    10 +    0.rational shouldBe ( 10 over  1)
    (-10) + (101 over 10) shouldBe   (1 over 10)
  }

  @Test
  fun `rationals can be subtracted`() {
    10.rational -    20.rational shouldBe (-10).rational
    10.rational -     0.rational shouldBe    10.rational
    0.rational - (-10).rational shouldBe    10.rational
    10.rational -    10.rational shouldBe     0.rational

    (101 over 10) - (202 over 10) shouldBe (-101 over 10)
    (101 over 10) -    0.rational shouldBe  (101 over 10)
    0.rational - (109 over 10) shouldBe (-109 over 10)
    (101 over 10) - (101 over 10) shouldBe     0.rational

    (101 over 10) -    10 shouldBe (  1 over 10)
    (101 over 10) -     0 shouldBe (101 over 10)
    0.rational -    10 shouldBe (-10 over  1)
    (101 over 10) - (-10) shouldBe (201 over 10)

    10 - (101 over 10) shouldBe (  -1 over 10)
    0 - (101 over 10) shouldBe (-101 over 10)
    10 -    0.rational shouldBe (  10 over  1)
    (-10) - (101 over 10) shouldBe (-201 over 10)
  }

  @Test
  fun `unary minus can be applied to rationals`() {
    -(10.rational) shouldBe (-10).rational
    -((-10).rational) shouldBe    10.rational

    -((109 over 10)) shouldBe (-109 over 10)
    -((-108 over 10)) shouldBe  ( 54 over 5)
  }

  @Test
  fun `rationals can be decremented and incremented`() {
    10.rational.inc() shouldBe    11.rational
    (-10).rational.inc() shouldBe  (-9).rational
    10.rational.dec() shouldBe     9.rational
    (-10).rational.dec() shouldBe (-11).rational

    (101 over 10).inc() shouldBe  (111 over 10)
    (-101 over 10).inc() shouldBe  (-91 over 10)
    (101 over 10).dec() shouldBe   (91 over 10)
    (-101 over 10).dec() shouldBe (-111 over 10)
  }

  @Test
  fun `rationals can be compared`() {
    (10.rational >   9.rational).shouldBeTrue()
    (10.rational >  11.rational).shouldBeFalse()
    (10.rational <  11.rational).shouldBeTrue()
    (10.rational <   9.rational).shouldBeFalse()
    (10.rational <= 11.rational).shouldBeTrue()
    (10.rational <= 10.rational).shouldBeTrue()
    (10.rational <=  9.rational).shouldBeFalse()
    (10.rational >=  9.rational).shouldBeTrue()
    (10.rational >=  9.rational).shouldBeTrue()
    (10.rational >= 11.rational).shouldBeFalse()
    (10.rational == 10.rational).shouldBeTrue()
    (10.rational == 11.rational).shouldBeFalse()
    (10.rational != 11.rational).shouldBeTrue()
    (10.rational != 10.rational).shouldBeFalse()

    ("10.1002".rational >  "10.1001".rational).shouldBeTrue()
    ("10.1002".rational >  "10.1002".rational).shouldBeFalse()
    ("10.1002".rational <  "10.1003".rational).shouldBeTrue()
    ("10.1002".rational <  "10.1002".rational).shouldBeFalse()
    ("10.1002".rational <= "10.1003".rational).shouldBeTrue()
    ("10.1002".rational <= "10.1002".rational).shouldBeTrue()
    ("10.1002".rational <= "10.1001".rational).shouldBeFalse()
    ("10.1002".rational >= "10.1001".rational).shouldBeTrue()
    ("10.1002".rational >= "10.1002".rational).shouldBeTrue()
    ("10.1002".rational >= "10.1003".rational).shouldBeFalse()
    ("10.1002".rational == "10.1002".rational).shouldBeTrue()
    ("10.1002".rational == "10.1001".rational).shouldBeFalse()
    ("10.1002".rational != "10.1001".rational).shouldBeTrue()
    ("10.1002".rational != "10.1002".rational).shouldBeFalse()
  }

  @Test
  fun `rationals can be multiplied`() {
    10.rational *    20 shouldBe    200.rational
    10.rational *     0 shouldBe      0.rational
    0.rational *    10 shouldBe      0.rational
    10.rational * (-10) shouldBe (-100).rational

    "10.01".rational *    20 shouldBe  "200.20".rational
    "10.01".rational *     0 shouldBe    "0.00".rational
    "0.01".rational *    10 shouldBe    "0.10".rational
    "0.00".rational *    10 shouldBe    "0.00".rational
    "10.01".rational * (-10) shouldBe "-100.10".rational

    "10.01".rational *  "20.5".rational shouldBe  "205.205".rational
    "10.01".rational *   "0.5".rational shouldBe    "5.005".rational
    "0.01".rational *  "10.5".rational shouldBe    "0.105".rational
    "0.00".rational *  "10.5".rational shouldBe    "0.000".rational
    "10.01".rational * "-10.5".rational shouldBe "-105.105".rational

    "10.01".rational *  "20.5".rational shouldBe  "205.205".rational
    "10.01".rational *   "0.5".rational shouldBe    "5.005".rational
    "0.01".rational *  "10.5".rational shouldBe    "0.105".rational
    "0.00".rational *  "10.5".rational shouldBe    "0.000".rational
    "10.01".rational * "-10.5".rational shouldBe "-105.105".rational

    20 * 10.rational shouldBe    200.rational
    0 * 10.rational shouldBe      0.rational
    10 *  0.rational shouldBe      0.rational
    (-10) * 10.rational shouldBe (-100).rational

    20 * "10.01".rational shouldBe  "200.20".rational
    0 * "10.01".rational shouldBe    "0.00".rational
    10 *  "0.01".rational shouldBe    "0.10".rational
    10 *  "0.00".rational shouldBe    "0.00".rational
    (-10) * "10.01".rational shouldBe "-100.10".rational

    "20.5".rational * "10.01".rational shouldBe  "205.205".rational
    "0.5".rational * "10.01".rational shouldBe    "5.005".rational
    "10.5".rational *  "0.01".rational shouldBe    "0.105".rational
    "10.5".rational *  "0.00".rational shouldBe    "0.000".rational
    "-10.5".rational * "10.01".rational shouldBe "-105.105".rational
  }

  @Test
  fun `rationals can be divided`() {
    10.rational /    20 shouldBe  "1/2".rational
    0.rational /    10 shouldBe      0.rational
    10.rational / (-10) shouldBe   (-1).rational
    10.rational /     0 shouldBe "10/0".rational
    shouldThrow<ArithmeticException> { (10.rational / 0).toBigDecimal() }

    "10.01".rational /    20 shouldBe "0.5005".rational
    "0.01".rational /    10 shouldBe  "0.001".rational
    "0.00".rational /    10 shouldBe   "0.00".rational
    "10.01".rational / (-10) shouldBe "-1.001".rational
    "10.01".rational / 0     shouldBe (1001 over 100) * (1 over 0)
    shouldThrow<ArithmeticException> { ("10.01".rational / 0).toBigDecimal() }

    "10.01".rational /  "20.5".rational shouldBe (1001 over 2050)
    "10.01".rational /   "0.5".rational shouldBe "20.02".rational
    "0.01".rational /  "10.5".rational shouldBe (1 over 1050)
    "0.00".rational /  "10.5".rational shouldBe 0.rational
    "10.01".rational / "-10.5".rational shouldBe (143 over -150)

    20 / 10.rational shouldBe "2".rational
    10 /  0.rational shouldBe (10 over 0)
    (-10) / 10.rational shouldBe (-1).rational
    0 / 10.rational shouldBe 0.rational
  }

  @Test
  fun `rationals can be coerced to minimum value`() {
    10.rational.coerceAtLeast(    9.rational) shouldBe    10.rational
    10.rational.coerceAtLeast(   10.rational) shouldBe    10.rational
    10.rational.coerceAtLeast(   11.rational) shouldBe    11.rational
    (-10).rational.coerceAtLeast( (-9).rational) shouldBe  (-9).rational
    (-10).rational.coerceAtLeast((-10).rational) shouldBe (-10).rational
    (-10).rational.coerceAtLeast((-11).rational) shouldBe (-10).rational

    "10.1".rational.coerceAtLeast( "10.0".rational) shouldBe  "10.1".rational
    "10.1".rational.coerceAtLeast( "10.1".rational) shouldBe  "10.1".rational
    "10.1".rational.coerceAtLeast( "10.2".rational) shouldBe  "10.2".rational
    "-10.1".rational.coerceAtLeast("-10.0".rational) shouldBe "-10.0".rational
    "-10.1".rational.coerceAtLeast("-10.1".rational) shouldBe "-10.1".rational
    "-10.1".rational.coerceAtLeast("-10.2".rational) shouldBe "-10.1".rational
  }

  @Test
  fun `rationals can be coerced to maximum value`() {
    10.rational.coerceAtMost(    9.rational) shouldBe     9.rational
    10.rational.coerceAtMost(   10.rational) shouldBe    10.rational
    10.rational.coerceAtMost(   11.rational) shouldBe    10.rational
    (-10).rational.coerceAtMost( (-9).rational) shouldBe (-10).rational
    (-10).rational.coerceAtMost((-10).rational) shouldBe (-10).rational
    (-10).rational.coerceAtMost((-11).rational) shouldBe (-11).rational

    "10.1".rational.coerceAtMost( "10.0".rational) shouldBe  "10.0".rational
    "10.1".rational.coerceAtMost( "10.1".rational) shouldBe  "10.1".rational
    "10.1".rational.coerceAtMost( "10.2".rational) shouldBe  "10.1".rational
    "-10.1".rational.coerceAtMost("-10.0".rational) shouldBe "-10.1".rational
    "-10.1".rational.coerceAtMost("-10.1".rational) shouldBe "-10.1".rational
    "-10.1".rational.coerceAtMost("-10.2".rational) shouldBe "-10.2".rational
  }

  @Test
  fun `rationals can be coerced to some range`() {
    10.rational.coerceIn( 9.rational..11.rational) shouldBe 10.rational
    10.rational.coerceIn( 8.rational..10.rational) shouldBe 10.rational
    10.rational.coerceIn(10.rational..12.rational) shouldBe 10.rational
    10.rational.coerceIn( 7.rational..9.rational)  shouldBe  9.rational
    10.rational.coerceIn(11.rational..13.rational) shouldBe 11.rational

    10.rational.coerceIn( 9.rational, 11.rational) shouldBe 10.rational
    10.rational.coerceIn( 8.rational, 10.rational) shouldBe 10.rational
    10.rational.coerceIn(10.rational, 12.rational) shouldBe 10.rational
    10.rational.coerceIn( 7.rational, 9.rational)  shouldBe  9.rational
    10.rational.coerceIn(11.rational, 13.rational) shouldBe 11.rational

    "10.1".rational.coerceIn("10.0".rational.."10.5".rational) shouldBe "10.1".rational
    "10.1".rational.coerceIn( "9.8".rational.."10.1".rational) shouldBe "10.1".rational
    "10.1".rational.coerceIn("10.1".rational.."10.3".rational) shouldBe "10.1".rational
    "10.1".rational.coerceIn( "9.7".rational.."10.0".rational) shouldBe "10.0".rational
    "10.1".rational.coerceIn("10.2".rational.."10.5".rational) shouldBe "10.2".rational

    "10.1".rational.coerceIn("10.0".rational, "10.5".rational) shouldBe "10.1".rational
    "10.1".rational.coerceIn( "9.8".rational, "10.1".rational) shouldBe "10.1".rational
    "10.1".rational.coerceIn("10.1".rational, "10.3".rational) shouldBe "10.1".rational
    "10.1".rational.coerceIn( "9.7".rational, "10.0".rational) shouldBe "10.0".rational
    "10.1".rational.coerceIn("10.2".rational, "10.5".rational) shouldBe "10.2".rational
  }

  @Test
  fun `absolute values of rationals can be computed`() {
    10.rational.abs() shouldBe 10.rational
    0.rational.abs() shouldBe  0.rational
    (-10).rational.abs() shouldBe 10.rational

    abs(   10.rational) shouldBe 10.rational
    abs(    0.rational) shouldBe  0.rational
    abs((-10).rational) shouldBe 10.rational

    "10.1".rational.abs() shouldBe "10.1".rational
    "-10.1".rational.abs() shouldBe "10.1".rational

    abs( "10.1".rational) shouldBe "10.1".rational
    abs("-10.1".rational) shouldBe "10.1".rational
  }

  @Test
  fun `percentage of rationals can be computed`() {
    5 percentOf 80.rational shouldBe "4.00".rational
    "5.5".rational percentOf 80.rational shouldBe "4.400".rational
  }

  @Test
  fun `rationals can be converted to strings`() {
    "1.4".rational.toString() shouldBe  "7/5"
    "1.5".rational.toString() shouldBe  "3/2"
    "-1.4".rational.toString() shouldBe "-7/5"
    "-1.5".rational.toString() shouldBe "-3/2"
    "1.44".rational.toString() shouldBe  "36/25"
    "1.45".rational.toString() shouldBe  "29/20"
    "1.46".rational.toString() shouldBe  "73/50"
    "1.444".rational.toString() shouldBe  "361/250"
    "1.455".rational.toString() shouldBe  "291/200"
    "1.466".rational.toString() shouldBe  "733/500"
    ("10.01".rational / "20.5".rational).toString() shouldBe "1001/2050"
  }

  @Test
  fun `rationals can be converted to strings with specified number of digits`() {
    "1.4".rational.toString(0) shouldBe  "1"
    "1.5".rational.toString(0) shouldBe  "2"
    "-1.4".rational.toString(0) shouldBe "-1"
    "-1.5".rational.toString(0) shouldBe "-2"
    "1.44".rational.toString(0) shouldBe  "1"
    "1.45".rational.toString(0) shouldBe  "1"
    "1.46".rational.toString(0) shouldBe  "1"
    "1.444".rational.toString(0) shouldBe  "1"
    "1.455".rational.toString(0) shouldBe  "1"
    "1.466".rational.toString(0) shouldBe  "1"
    ("10.01".rational / "20.5".rational).toString(0) shouldBe "0"

    "1.4".rational.toString(1) shouldBe  "1.4"
    "1.5".rational.toString(1) shouldBe  "1.5"
    "-1.4".rational.toString(1) shouldBe "-1.4"
    "-1.5".rational.toString(1) shouldBe "-1.5"
    "1.44".rational.toString(1) shouldBe  "1.4"
    "1.45".rational.toString(1) shouldBe  "1.5"
    "1.46".rational.toString(1) shouldBe  "1.5"
    "1.444".rational.toString(1) shouldBe  "1.4"
    "1.455".rational.toString(1) shouldBe  "1.5"
    "1.466".rational.toString(1) shouldBe  "1.5"
    ("10.01".rational / "20.5".rational).toString(1) shouldBe "0.5"

    "1.4".rational.toString(2) shouldBe  "1.40"
    "1.5".rational.toString(2) shouldBe  "1.50"
    "-1.4".rational.toString(2) shouldBe "-1.40"
    "-1.5".rational.toString(2) shouldBe "-1.50"
    "1.44".rational.toString(2) shouldBe  "1.44"
    "1.45".rational.toString(2) shouldBe  "1.45"
    "1.46".rational.toString(2) shouldBe  "1.46"
    "1.444".rational.toString(2) shouldBe  "1.44"
    "1.455".rational.toString(2) shouldBe  "1.46"
    "1.466".rational.toString(2) shouldBe  "1.47"
    ("10.01".rational / "20.5".rational).toString(2) shouldBe "0.49"

    "1.4".rational.toString(3) shouldBe  "1.400"
    "1.5".rational.toString(3) shouldBe  "1.500"
    "-1.4".rational.toString(3) shouldBe "-1.400"
    "-1.5".rational.toString(3) shouldBe "-1.500"
    "1.44".rational.toString(3) shouldBe  "1.440"
    "1.45".rational.toString(3) shouldBe  "1.450"
    "1.46".rational.toString(3) shouldBe  "1.460"
    "1.444".rational.toString(3) shouldBe  "1.444"
    "1.455".rational.toString(3) shouldBe  "1.455"
    "1.466".rational.toString(3) shouldBe  "1.466"
    ("10.01".rational / "20.5".rational).toString(3) shouldBe "0.488"
  }

  @Test
  fun `rationals can be rounded to the ceiling`() {
    "1.4".rational.ceil() shouldBe  "2".rational
    "1.5".rational.ceil() shouldBe  "2".rational
    "-1.4".rational.ceil() shouldBe "-2".rational
    "-1.5".rational.ceil() shouldBe "-2".rational
    "1.44".rational.ceil() shouldBe  "2".rational
    "1.45".rational.ceil() shouldBe  "2".rational
    "1.46".rational.ceil() shouldBe  "2".rational
    "1.444".rational.ceil() shouldBe  "2".rational
    "1.455".rational.ceil() shouldBe  "2".rational
    "1.466".rational.ceil() shouldBe  "2".rational
  }

  @Test
  fun `rationals can be rounded to the floor`() {
    "1.4".rational.floor() shouldBe  "1".rational
    "1.5".rational.floor() shouldBe  "1".rational
    "-1.4".rational.floor() shouldBe "-1".rational
    "-1.5".rational.floor() shouldBe "-1".rational
    "1.44".rational.floor() shouldBe  "1".rational
    "1.45".rational.floor() shouldBe  "1".rational
    "1.46".rational.floor() shouldBe  "1".rational
    "1.444".rational.floor() shouldBe  "1".rational
    "1.455".rational.floor() shouldBe  "1".rational
    "1.466".rational.floor() shouldBe  "1".rational
  }

  @Test
  fun `minimum of 2 rationals can be computed`() {
    min(   10.rational,     9.rational) shouldBe     9.rational
    min(   10.rational,    10.rational) shouldBe    10.rational
    min(   10.rational,    11.rational) shouldBe    10.rational
    min((-10).rational,  (-9).rational) shouldBe (-10).rational
    min((-10).rational, (-10).rational) shouldBe (-10).rational
    min((-10).rational, (-11).rational) shouldBe (-11).rational

    min( "10.1".rational, "10.0".rational) shouldBe  "10.0".rational
    min( "10.1".rational, "10.1".rational) shouldBe  "10.1".rational
    min( "10.1".rational, "10.2".rational) shouldBe  "10.1".rational
    min("-10.1".rational,"-10.0".rational) shouldBe "-10.1".rational
    min("-10.1".rational,"-10.1".rational) shouldBe "-10.1".rational
    min("-10.1".rational,"-10.2".rational) shouldBe "-10.2".rational
  }

  @Test
  fun `maximum of 2 rationals can be computed`() {
    max(   10.rational,     9. rational) shouldBe    10.rational
    max(   10.rational,    10. rational) shouldBe    10.rational
    max(   10.rational,    11. rational) shouldBe    11.rational
    max((-10).rational,  (-9). rational) shouldBe  (-9).rational
    max((-10).rational, (-10). rational) shouldBe (-10).rational
    max((-10).rational, (-11). rational) shouldBe (-10).rational

    max( "10.1".rational, "10.0".rational) shouldBe  "10.1".rational
    max( "10.1".rational, "10.1".rational) shouldBe  "10.1".rational
    max( "10.1".rational, "10.2".rational) shouldBe  "10.2".rational
    max("-10.1".rational,"-10.0".rational) shouldBe "-10.0".rational
    max("-10.1".rational,"-10.1".rational) shouldBe "-10.1".rational
    max("-10.1".rational,"-10.2".rational) shouldBe "-10.1".rational
  }

  @Test
  fun `rationals can be converted to doubles`() {
    10.rational.toDouble() shouldBe  10.0
    0.rational.toDouble() shouldBe   0.0
    (-10).rational.toDouble() shouldBe -10.0

    "10.5".rational.toDouble() shouldBe  10.5
    "0.5".rational.toDouble() shouldBe   0.5
    "-10.5".rational.toDouble() shouldBe -10.5
  }

  @Test
  fun `rationals can be converted to big-decimals`() {
    10.rational.toBigDecimal() shouldBe  "10".toBigDecimal()
    0.rational.toBigDecimal() shouldBe   "0".toBigDecimal()
    (-10).rational.toBigDecimal() shouldBe "-10".toBigDecimal()

    "10.5".rational.toBigDecimal() shouldBe  "10.5".toBigDecimal()
    "0.5".rational.toBigDecimal() shouldBe   "0.5".toBigDecimal()
    "-10.5".rational.toBigDecimal() shouldBe "-10.5".toBigDecimal()
  }

  @Test
  fun `rationals can be converted to integers`() {
    10.rational.toInt() shouldBe  10
    0.rational.toInt() shouldBe   0
    (-10).rational.toInt() shouldBe -10

    "10.5".rational.toInt() shouldBe  10
    "0.5".rational.toInt() shouldBe   0
    "-10.5".rational.toInt() shouldBe -10
  }

  @Test
  fun `reciprocals of rationals can be computed`() {
    ( 1 over  2).reciprocal().toString() shouldBe "2"
    (40 over 16).reciprocal().toString() shouldBe "2/5"
    10.rational.reciprocal().toString()  shouldBe "1/10"
  }

  @Test
  fun `rationals can be simplified`() {
    (  8 over    4).canonical().toString() shouldBe "2"
    (  4 over    8).canonical().toString() shouldBe "1/2"
    (512 over 1024).canonical().toString() shouldBe "1/2"
    ( 40 over   16).canonical().toString() shouldBe "5/2"
    10.rational.canonical().toString()     shouldBe "10"
  }

  @Test
  fun `tax should be computed correctly`() {
    computeTax("123456.78".r) shouldBe 42_515
    computeTax( "76543.21".r) shouldBe 22_811
  }

  private fun computeTax(taxableIncome: Rational): Int {
    val income = taxableIncome.toInt().coerceIn(0..1_000_000_000).rational
    return when {
      income <=  10_347.rational -> 0.rational
      income <=  14_926.rational -> ((income - 10_347.rational) / 10_000).let { y: Rational -> ((1_088_67 over 100) * y + 1_400) * y }
      income <=  58_596.rational -> ((income - 14_926.rational) / 10_000).let { y: Rational -> ((  200_43 over 100) * y + 2_397) * y + (869_32 over 100) }
      income <= 277_825.rational -> (42 over 100) * income -  (9_336_45 over 100)
      else                       -> (45 over 100) * income - (17_671_20 over 100)
    }.toInt()
  }
}
