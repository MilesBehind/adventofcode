package de.smartsteuer.frank.adventofcode2024.day13

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  ClawContraption.execute(lines("/adventofcode2024/day13/games.txt"))
}

object ClawContraption : Day {

  override fun part1(input: List<String>): Long =
    input.parseGames().mapNotNull { game -> game.computeMinimumTokensToWin() }.sum()

  override fun part2(input: List<String>): Long =
    input.parseGames().mapNotNull { game -> game.transform().computeMinimumTokensToWin() }.sum()

  data class Movement(val x: Long, val y: Long)

  data class Game(val buttonA: Movement, val buttonB: Movement, val prizePosition: Movement) {
    fun computeMinimumTokensToWin(): Long? {
      val divisor = (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
      if (divisor == 0L) return null
      val b = (buttonA.y * prizePosition.x - buttonA.x * prizePosition.y) / divisor
      val a = (prizePosition.x - b * buttonB.x) / buttonA.x
      return if (a * buttonA.x + b * buttonB.x == prizePosition.x && a * buttonA.y + b * buttonB.y == prizePosition.y) a * 3 + b else null
    }

    fun transform() =
      Game(buttonA, buttonB, Movement(prizePosition.x + 10_000_000_000_000, prizePosition.y + 10_000_000_000_000))
  }

  internal fun List<String>.parseGames(): List<Game> {
    val buttonRegex = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
    val priceRegex  = """Prize: X=(\d+), Y=(\d+)""".toRegex()
    fun String.parseMovement(regex: Regex): Movement =
      (regex.matchEntire(this) ?: throw IllegalArgumentException("invalid line: $this")).groupValues.let { (_, x, y) -> Movement(x.toLong(), y.toLong()) }

    return this.chunked(4).map { chunk ->
      Game(buttonA       = chunk[0].parseMovement(buttonRegex),
           buttonB       = chunk[1].parseMovement(buttonRegex),
           prizePosition = chunk[2].parseMovement(priceRegex))
    }
  }
}
