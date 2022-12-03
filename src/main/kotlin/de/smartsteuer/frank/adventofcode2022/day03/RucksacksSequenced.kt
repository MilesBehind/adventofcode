package de.smartsteuer.frank.adventofcode2022.day03

import de.smartsteuer.frank.adventofcode2022.linesSequence

fun main() {
  println("day 03, part 1: ${part1(linesSequence("/adventofcode2022/day03/rucksack-items.txt"))}")
  println("day 03, part 2: ${part2(linesSequence("/adventofcode2022/day03/rucksack-items.txt"))}")
}

fun part1(rucksacks: Sequence<String>): Int =
  rucksacks.sumOf { rucksack ->
    rucksack.splitHalves().let { (leftCompartment, rightCompartment) ->
      (leftCompartment.first { it in rightCompartment }).priority()
    }
  }

fun part2(rucksacks: Sequence<String>): Int =
  rucksacks.chunked(3).sumOf { (first, second, third) ->
    (first.filter { it in second }.first { it in third }).priority()
  }
