package de.smartsteuer.frank.adventofcode2021.day14

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val lines = lines("/adventofcode2021/day14/polymer-template.txt")
  val start = lines.first()
  val rules = parseRules(lines.drop(2))
  val maxMinusMinAfter10Rounds = maxMinusMin(applyRules(start, rules, 10))
  println("max - min after 10 rounds = $maxMinusMinAfter10Rounds")
  val polymer = Polymer(start, rules)
  val maxMinusMinAfter10Rounds2 = polymer.applyRules(times = 10).maxMinusMin()
  println("max - min after 10 rounds = $maxMinusMinAfter10Rounds2")
  val maxMinusMinAfter40Rounds = polymer.applyRules(times = 40).maxMinusMin()
  println("max - min after 40 rounds = $maxMinusMinAfter40Rounds")
}


internal fun parseRules(lines: List<String>) =
  lines.associate { line ->
    val (from, to) = """(\w{2}) -> (\w)""".toRegex().matchEntire(line)?.destructured ?: error("could not parse line '$line'")
    from to to
  }

class Polymer(private val patterns: Map<String, Long>, private val rules: Map<String, String>, private val lastElement: Char) {
  constructor(text: String, rules: Map<String, String>):
          this(text.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }, rules, text.last())

  internal tailrec fun applyRules(times: Int, result: Polymer = this): Polymer =
    if (times == 0) result else applyRules(times - 1, result.applyRules())

  internal fun maxMinusMin(): Long = counts().values.maxOf { it } - counts().values.minOf { it }

  internal fun counts(): Map<Char, Long> {
    val allButLast: Map<Char, Long> = patterns.entries.fold(emptyMap()) { result, (pattern, count) ->
      result + (pattern[0] to result.getOrDefault(pattern[0], 0) + count)
    }
    return allButLast + (lastElement to allButLast.getOrDefault(lastElement, 0) + 1)
  }

  private fun applyRules(): Polymer =
    Polymer(patterns.entries.fold(emptyMap()) { result, (pattern, count) ->
      val newElement = rules[pattern] ?: error("could not find rule for pattern $pattern")
      val newPattern1 = pattern[0] + newElement
      val newPattern2 = newElement + pattern[1]
      val newPattern1Entry = newPattern1 to result.getOrDefault(newPattern1, 0) + count
      val newPattern2Entry = newPattern2 to result.getOrDefault(newPattern2, 0) + count
      result + newPattern1Entry + newPattern2Entry
    }, rules, lastElement)

  override fun toString() = patterns.toString()
}



internal tailrec fun applyRules(result: String, rules: Map<String, String>, times: Int): String =
  if (times == 0) result
  else applyRules(result.windowed(2).joinToString("") { fragment -> fragment[0] + rules.getOrDefault(fragment, "") } + result.last(),
                  rules, times - 1)

internal fun maxMinusMin(polymer: String): Long {
  val counts = polymer.groupBy { it }.mapValues { it.value.size.toLong() }.values
  return counts.maxOf { it } - counts.minOf { it }
}
