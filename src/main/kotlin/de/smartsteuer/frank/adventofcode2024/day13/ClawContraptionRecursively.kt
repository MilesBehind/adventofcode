package de.smartsteuer.frank.adventofcode2024.day13

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines
import de.smartsteuer.frank.adventofcode2024.day13.ClawContraption.parseGames
import de.smartsteuer.frank.adventofcode2024.day13.ClawContraption.Movement

fun main() {
  ClawContraption.execute(lines("/adventofcode2024/day13/games.txt"))
}

@Suppress("unused")
object ClawContraptionRecursively : Day<Long> {

  override fun part1(input: List<String>): Long =
    input.parseGames().mapNotNull { game -> game.computeMinimumTokensToWin() }.sum()

  override fun part2(input: List<String>): Long =
    TODO()

  data class ButtonPresses(val a: Int, val b: Int) {
    fun tokensNeeded() = 3 * a + b
  }

  operator fun Movement.plus(other: Movement) = Movement(x + other.x, y + other.y)

  data class Game(val buttonA: Movement, val buttonB: Movement, val prizePosition: Movement) {
    fun computeMinimumTokensToWin(): Int? {
      tailrec fun computeMinimumTokensToWin(unfinished: MutableSet<Pair<ButtonPresses, Movement>>, finished: MutableSet<Pair<ButtonPresses, Movement>>): Int? {
        unfinished.removeAll { (_, position) -> position.x > prizePosition.x || position.y > prizePosition.y }
        if (unfinished.isEmpty() && finished.isEmpty()) return null
        val (newFinished, newUnfinished) = unfinished.partition { (_, position) -> position == prizePosition }
        if (newUnfinished.isEmpty()) return finished.apply { addAll(newFinished) }.minOf { it.first.tokensNeeded() }
        val nextUnfinished = newUnfinished.fold(mutableSetOf<Pair<ButtonPresses, Movement>>()) { result, (presses, position) ->
          result.also {
            result += ButtonPresses(presses.a + 1, presses.b    ) to position + buttonA
            result += ButtonPresses(presses.a,     presses.b + 1) to position + buttonB
          }
        }
        return computeMinimumTokensToWin(nextUnfinished, finished.apply { addAll(newFinished) })
      }
      return computeMinimumTokensToWin(mutableSetOf(ButtonPresses(1, 0) to buttonA, ButtonPresses(0, 1) to buttonB), mutableSetOf())
    }
  }
}
