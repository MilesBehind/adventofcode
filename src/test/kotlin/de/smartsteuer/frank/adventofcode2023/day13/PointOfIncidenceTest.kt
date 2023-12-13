package de.smartsteuer.frank.adventofcode2023.day13

import io.kotest.matchers.*
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
    part2(parseMap(input)) shouldBe 0
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
  }

  @Test
  fun `bit sets can be mirrored`() {
    listOf(
      "0101100110",
      "0000111111"
    ).map { it.toInt(2).reverse(10).toString(2).padStart(10, '0').take(10) } shouldBe listOf(
      "0110011010",
      "1111110000"
    )
  }

  @Test
  fun `bit sets can masked`() {
    println(masks)
    "0101100110".toInt(2).clearLeftBits(2).toString(2).padStart(10, '0').take(10) shouldBe "0000000010"
    List(10) { "0101100110" }.mapIndexed { index, input -> input.toInt(2).clearLeftBits(index + 1).toString(2).padStart(10, '0').take(10) } shouldBe listOf(
      "0000000000",
      "0000000010",
      "0000000110",
      "0000000110",
      "0000000110",
      "0000100110",
      "0001100110",
      "0001100110",
      "0101100110",
      "0101100110",
    )
  }
}