package de.smartsteuer.frank.adventofcode2023.day02

import de.smartsteuer.frank.adventofcode2023.day02.CubeColor.*
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CubeConundrumKtTest {
  val input = listOf(
    "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
    "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
    "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
    "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
    "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green",
  )

  @Test
  fun `games can be parsed`() {
    val games = parseGames(input)
    games shouldContainExactly listOf(
      Game(id = 1, sets = listOf(mapOf(BLUE to 3, RED to 4), mapOf(RED to 1, GREEN to 2, BLUE to 6), mapOf(GREEN to 2))),
      Game(id = 2, sets = listOf(mapOf(BLUE to 1, GREEN to 2), mapOf(GREEN to 3, BLUE to 4, RED to 1), mapOf(GREEN to 1, BLUE to 1))),
      Game(id = 3, sets = listOf(mapOf(GREEN to 8, BLUE to 6, RED to 20), mapOf(BLUE to 5, RED to 4, GREEN to 13), mapOf(GREEN to 5, RED to 1))),
      Game(id = 4, sets = listOf(mapOf(GREEN to 1, RED to 3, BLUE to 6), mapOf(GREEN to 3, RED to 6), mapOf(GREEN to 3, BLUE to 15, RED to 14))),
      Game(id = 5, sets = listOf(mapOf(RED to 6, BLUE to 1, GREEN to 3), mapOf(BLUE to 2, RED to 1, GREEN to 2))),
    )
  }

  @Test
  fun `part 1`() {
    part1(parseGames(input)) shouldBe 8
  }

  @Test
  fun `part 2`() {
    part2(parseGames(input)) shouldBe 2286
  }
}