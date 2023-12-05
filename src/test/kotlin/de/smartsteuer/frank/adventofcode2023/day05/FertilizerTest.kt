package de.smartsteuer.frank.adventofcode2023.day05

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class FertilizerTest {
  private val input = listOf(
    "seeds: 79 14 55 13",
    "",
    "seed-to-soil map:",
    "50 98 2",
    "52 50 48",
    "",
    "soil-to-fertilizer map:",
    "0 15 37",
    "37 52 2",
    "39 0 15",
    "",
    "fertilizer-to-water map:",
    "49 53 8",
    "0 11 42",
    "42 0 7",
    "57 7 4",
    "",
    "water-to-light map:",
    "88 18 7",
    "18 25 70",
    "",
    "light-to-temperature map:",
    "45 77 23",
    "81 45 19",
    "68 64 13",
    "",
    "temperature-to-humidity map:",
    "0 69 1",
    "1 0 69",
    "",
    "humidity-to-location map:",
    "60 56 37",
    "56 93 4",
  )

  @Test
  fun `part 1`() {
    part1(parseAlmanac(input)) shouldBe 35
  }

  @Test
  fun `part 2`() {
    part2(parseAlmanac(input)) shouldBe 46
  }

  @Test
  fun `almanac can be parsed`() {
    parseAlmanac(input) shouldBe Almanac(listOf(79, 14, 55, 13), listOf(
      Mapping("seed",        "soil",        listOf(Range(50, 98, 2), Range(52, 50, 48))),
      Mapping("soil",        "fertilizer",  listOf(Range(0, 15, 37), Range(37, 52, 2), Range(39, 0, 15))),
      Mapping("fertilizer",  "water",       listOf(Range(49, 53, 8), Range(0, 11, 42), Range(42, 0, 7), Range(57, 7, 4))),
      Mapping("water",       "light",       listOf(Range(88, 18, 7), Range(18, 25, 70))),
      Mapping("light",       "temperature", listOf(Range(45, 77, 23), Range(81, 45, 19), Range(68, 64, 13))),
      Mapping("temperature", "humidity",    listOf(Range(0, 69, 1), Range(1, 0, 69))),
      Mapping("humidity",    "location",    listOf(Range(60, 56, 37), Range(56, 93, 4))),
    ))
  }

  @Test
  fun `almanac finds mappings`() {
    val almanac = parseAlmanac(input)
    almanac.findMappingBySource     ("seed").from        shouldBe "seed"
    almanac.findMappingBySource     ("soil").from        shouldBe "soil"
    almanac.findMappingBySource     ("fertilizer").from  shouldBe "fertilizer"
    almanac.findMappingBySource     ("water").from       shouldBe "water"
    almanac.findMappingBySource     ("light").from       shouldBe "light"
    almanac.findMappingBySource     ("temperature").from shouldBe "temperature"
    almanac.findMappingBySource     ("humidity").from    shouldBe "humidity"
    almanac.findMappingByDestination("soil").to          shouldBe "soil"
    almanac.findMappingByDestination("fertilizer").to    shouldBe "fertilizer"
    almanac.findMappingByDestination("water").to         shouldBe "water"
    almanac.findMappingByDestination("light").to         shouldBe "light"
    almanac.findMappingByDestination("temperature").to   shouldBe "temperature"
    almanac.findMappingByDestination("humidity").to      shouldBe "humidity"
    almanac.findMappingByDestination("location").to      shouldBe "location"
  }
}