package de.smartsteuer.frank.adventofcode2025.day10

import de.smartsteuer.frank.adventofcode2025.lines

data class Machine2(
  val targetLights: List<Boolean>,
  val buttons: List<List<Int>>,
  val targetJoltage: List<Int>
)

/**
 * Part 1: Simple DFS with memoization
 */
fun dfsPart1(
  lights: BooleanArray,
  targetLights: List<Boolean>,
  i: Int,
  buttons: List<List<Int>>,
  cache: MutableMap<Pair<List<Boolean>, Int>, Long>
): Long {
  if (lights.toList() == targetLights) {
    return 0
  }
  if (i == buttons.size) {
    return -1
  }

  val key = lights.toList() to i
  cache[key]?.let { return it }

  var result = Long.MAX_VALUE
  for (j in i until buttons.size) {
    // Toggle lights
    for (k in buttons[j]) {
      lights[k] = !lights[k]
    }

    val r = 1 + dfsPart1(lights, targetLights, j + 1, buttons, cache)
    if (r > 0) {
      result = minOf(result, r)
    }

    // Toggle back
    for (k in buttons[j]) {
      lights[k] = !lights[k]
    }
  }

  cache[key] = result
  return result
}

/**
 * Initialize a list of length `m` with the contents `[0, 0, ..., n]` and then
 * repeatedly call this function to obtain all possible combinations of `m`
 * integers that sum to `n`. The function returns `false` if there is no other
 * combination.
 *
 * Example (m=3, n=4):
 * ```
 * ```
 */
fun nextCombination(combinations: IntArray): Boolean {
  val i = combinations.indexOfLast { it != 0 }
  if (i == 0) {
    return false
  }
  val v = combinations[i]
  combinations[i - 1] += 1
  combinations[i] = 0
  combinations[combinations.size - 1] = v - 1
  return true
}

fun isButtonAvailable(i: Int, mask: Int): Boolean {
  return mask and (1 shl i) > 0
}

/**
 * Part 2: Optimized DFS that tries to prune as many branches as possible
 */
fun dfsPart2(joltage: List<Int>, availableButtonsMask: Int, buttons: List<List<Int>>): Int {
  if (joltage.all { it == 0 }) {
    return 0
  }

  // Important optimization: Find the joltage value with the lowest number of
  // combinations of buttons to try. This allows us to prune branches as early
  // as possible.
  // Second optimization (not so important, but still quite good): If multiple
  // joltage values are affected by the same number of buttons, select the
  // highest value
  val (mini: Int, min: Int) = joltage
    .withIndex()
    .filter { it.value > 0 }
    .minByOrNull { (i: Int, v: Int) ->
      buttons
        .withIndex()
        .count { (j: Int, b: List<Int>) -> isButtonAvailable(j, availableButtonsMask) && i in b }
    } ?: return Int.MAX_VALUE

  // Get the buttons that affect the joltage value at position `mini`
  val matchingButtons = buttons
    .withIndex()
    .filter { (i, b) -> isButtonAvailable(i, availableButtonsMask) && mini in b }
    .toList()

  var result = Int.MAX_VALUE

  if (matchingButtons.isNotEmpty()) {
    // Create new mask so only those buttons remain that do not affect the
    // joltage value at position `mini`
    var newMask = availableButtonsMask
    for ((i, _) in matchingButtons) {
      newMask = newMask and (1 shl i).inv()
    }

    // Try all combinations of matching buttons
    val newJoltage = joltage.toMutableList()
    val counts = IntArray(matchingButtons.size)
    counts[matchingButtons.size - 1] = min

    do {
      // Count down joltage values and make sure we don't press a button
      // too often (i.e. that the number of button presses is not higher
      // than any of the values to decrease)
      var good = true
      newJoltage.indices.forEach { newJoltage[it] = joltage[it] }

      for (bi in counts.indices) {
        val cnt = counts[bi]
        if (cnt == 0) continue

        for (j in matchingButtons[bi].value) {
          if (newJoltage[j] >= cnt) {
            newJoltage[j] -= cnt
          } else {
            good = false
            break
          }
        }
        if (!good) break
      }

      if (good) {
        // Recurse with decreased joltage values and with remaining buttons
        val r = dfsPart2(newJoltage, newMask, buttons)
        if (r != Int.MAX_VALUE) {
          result = minOf(result, min + r)
        }
      }
    } while (nextCombination(counts))
  }

  return result
}

fun main() {
  val input = lines("/adventofcode2025/day10/machines.txt")

  // Parse input
  val machines = input.map { line ->
    val parts = line.split(" ")
    val targetLights = parts[0].substring(1, parts[0].length - 1)
      .map { it == '#' }
    val buttons = parts.subList(1, parts.size - 1)
      .map { b ->
        b.substring(1, b.length - 1)
          .split(',')
          .map { it.toInt() }
      }
    val targetJoltage = parts.last().substring(1, parts.last().length - 1)
      .split(',')
      .map { it.toInt() }

    Machine2(targetLights, buttons, targetJoltage)
  }

  // Part 1 - simple DFS with memoization
  var total1 = 0L
  for (m in machines) {
    val lights = BooleanArray(m.targetLights.size)
    total1 += dfsPart1(lights, m.targetLights, 0, m.buttons, mutableMapOf())
  }
  println("part 1: $total1")

  // Part 2 - optimized DFS that tries to prune as many branches as possible
  var total2 = 0
  for (m in machines) {
    val result = dfsPart2(m.targetJoltage, (1 shl m.buttons.size) - 1, m.buttons)
    println("result: $result")
    total2 += result
  }
  println("part 1: $total2")
}
