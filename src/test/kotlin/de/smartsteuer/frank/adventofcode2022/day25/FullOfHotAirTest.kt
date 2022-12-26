package de.smartsteuer.frank.adventofcode2022.day25

import de.smartsteuer.frank.adventofcode2022.day25.Day25.Snafu
import de.smartsteuer.frank.adventofcode2022.day25.Day25.part1
import de.smartsteuer.frank.adventofcode2022.day25.Day25.part2
import de.smartsteuer.frank.adventofcode2022.day25.Day25.toSnafu
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class FullOfHotAirTest {
  private val input = listOf(
    "1=-0-2",
    "12111",
    "2=0=",
    "21",
    "2=01",
    "111",
    "20012",
    "112",
    "1=-1=",
    "1-12",
    "12",
    "1=",
    "122",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe "2=-1=0"  // non-example result: 122-0==-=211==-2-200
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe ""  // non-example result: ???
  }

  private val snafuAndLong = listOf(
    Snafu("1")             to         1L,
    Snafu("2")             to         2L,
    Snafu("1=")            to         3L,
    Snafu("1-")            to         4L,
    Snafu("10")            to         5L,
    Snafu("11")            to         6L,
    Snafu("12")            to         7L,
    Snafu("2=")            to         8L,
    Snafu("2-")            to         9L,
    Snafu("20")            to        10L,
    Snafu("1=0")           to        15L,
    Snafu("1-0")           to        20L,
    Snafu("1=11-2")        to      2022L,
    Snafu("1-0---0")       to     12345L,
    Snafu("1121-1110-1=0") to 314159265L,
    Snafu("1=-0-2")        to      1747L,
    Snafu("12111")         to       906L,
    Snafu("2=0=")          to       198L,
    Snafu("21")            to        11L,
    Snafu("2=01")          to       201L,
    Snafu("111")           to        31L,
    Snafu("20012")         to      1257L,
    Snafu("112")           to        32L,
    Snafu("1=-1=")         to       353L,
    Snafu("1-12")          to       107L,
    Snafu("12")            to         7L,
    Snafu("1=")            to         3L,
    Snafu("122")           to        37L,
  )

  @Test
  fun `snafu can be converted to long`() {
    assertSoftly {
      snafuAndLong.forEach { (snafu, long) ->
        snafu.toLong() shouldBe long
      }
    }
  }

  @Test
  fun `long can be converted to snafu`() {
    assertSoftly {
      snafuAndLong.forEach { (snafu, long) ->
        withClue("decimal $long should be converted to '$snafu'") {
          long.toSnafu() shouldBe snafu
        }
      }
    }
  }
}
