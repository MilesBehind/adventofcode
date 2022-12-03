package de.smartsteuer.frank.adventofcode2022.day03

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val rucksacks = lines("/adventofcode2022/day03/rucksack-items.txt")
  println("day 03, part 1: ${part1(rucksacks)}")
  println("day 03, part 2: ${part2(rucksacks)}")
}

fun part1(rucksacks: List<String>): Int =
  rucksacks.sumOf { rucksack ->
    rucksack.splitHalves().let { (leftCompartment, rightCompartment) ->
      score(leftCompartment.first { it in rightCompartment })
    }
  }

fun part2(rucksacks: List<String>): Int =
  rucksacks.chunked(3).sumOf { (first, second, third) ->
    score(first.filter { it in second }.first { it in third })
  }

fun score(item: Char): Int = if (item in ('a'..'z')) item.code - 'a'.code + 1 else item.code - 'A'.code + 1 + 26

fun String.splitHalves(): Pair<String, String> = Pair(substring(0, length / 2), substring(length / 2))


//region just for fun: generalize case of finding common characters in several strings:
fun commonCharacters(vararg strings: String): String {
  tailrec fun commonCharacters(strings: Sequence<String>, common: String): String =
    if (!strings.iterator().hasNext()) common else commonCharacters(strings.drop(1), strings.first().filter { it in common })
  return if (!strings.iterator().hasNext()) "" else commonCharacters(strings.asSequence().drop(1), strings.first())
}
//endregion