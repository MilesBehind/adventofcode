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
      (leftCompartment.first { it in rightCompartment }).priority()
    }
  }

fun part2(rucksacks: List<String>): Int =
  rucksacks.chunked(3).sumOf { (first, second, third) ->
    (first.filter { it in second }.first { it in third }).priority()
  }

fun Char.priority(): Int = when (this) {
  in ('a'..'z') -> code - 'a'.code + 1
  else          -> code - 'A'.code + 1 + 26
}

fun String.splitHalves(): Pair<String, String> = Pair(substring(0, length / 2), substring(length / 2))
