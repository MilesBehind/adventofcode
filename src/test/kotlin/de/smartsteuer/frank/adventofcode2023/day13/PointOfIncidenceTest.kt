package de.smartsteuer.frank.adventofcode2023.day13

import io.kotest.matchers.*
import io.kotest.matchers.nulls.shouldBeNull
import org.junit.jupiter.api.Test

class PointOfIncidenceTest {
  private val input = listOf(
    "#.##..##.",
    "..#.##.#.",
    "##......#",
    "##......#",
    "..#.##.#.",
    "..##..##.",
    "#.#.##.#.",
    "",
    "#...##..#",
    "#....#..#",
    "..##..###",
    "#####.##.",
    "#####.##.",
    "..##..###",
    "#....#..#",
  )

  @Test
  fun `part 1`() {
    part1(parseMap(input)) shouldBe 405
  }

  @Test
  fun `part 2`() {
    part2(parseMap(input)) shouldBe 400
  }

  @Test
  fun `map can be parsed`() {
    val mirrorMaps = parseMap(input)
    mirrorMaps.map { map -> map.rowBits.map { it.toString(2) } } shouldBe listOf(
      listOf("101100110", "1011010", "110000001", "110000001", "1011010", "1100110", "101011010"),
      listOf("100011001", "100001001", "1100111", "111110110", "111110110", "1100111", "100001001")
    )
    mirrorMaps.map { map -> map.columnBits.map { it.toString(2) } } shouldBe listOf(
      listOf("1011001", "11000", "1100111", "1000010",  "100101",  "100101", "1000010", "1100111",   "11000"),
      listOf("1101101",  "1100",   "11110",   "11110", "1001100", "1100001",   "11110",   "11110", "1110011")
    )
  }

  @Test
  fun `vertical mirror can be found`() {
    val mirrorMaps = parseMap(input)
    mirrorMaps[0].findVerticalMirror() shouldBe 5
    mirrorMaps[1].findVerticalMirror().shouldBeNull()
  }

  @Test
  fun `horizontal mirror can be found`() {
    val mirrorMaps = parseMap(input)
    mirrorMaps[0].findHorizontalMirror().shouldBeNull()
    mirrorMaps[1].findHorizontalMirror() shouldBe 4
  }

  private fun Int.asBinary(digits: Int) = toString(2).padStart(digits, '0').take(digits)

  @Test
  fun `bit sets can be mirrored`() {
    listOf(
      "0101100110",
      "0000111111"
    ).map { it.toInt(2).reverse(10).asBinary(10) } shouldBe listOf(
      "0110011010",
      "1111110000"
    )
  }

  @Test
  fun `bit sets can sliced`() {
    "0101100110".toInt(2).let { input ->
      input.getSlice(10, 1).asBinary(10) shouldBe   "0101100110"
      input.getSlice(10, 2).asBinary(10) shouldBe  "0010110011"
      input.getSlice(10, 3).asBinary(10) shouldBe "0001011001"
      input.getSlice(9, 3).asBinary(10) shouldBe "0001011001"
      input.getSlice(8, 2).asBinary(10) shouldBe  "0000110011"
      input.getSlice(7, 1).asBinary(10) shouldBe   "0001100110"
    }
  }
}