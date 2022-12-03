package de.smartsteuer.frank.adventofcode2022.day03

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val itemLists = lines("/adventofcode2022/day03/rucksack-items.txt")
  println("day 03, part 1: ${part1(itemLists)}")
}

fun part1(itemLists: List<String>): Int =
  itemLists.sumOf { itemList ->
    itemList.splitHalves().let { (leftCompartment, rightCompartment) ->
      score(leftCompartment.first { it in rightCompartment })
    }
  }

fun score(item: Char): Int = if (item in ('a'..'z')) item.code - 'a'.code + 1 else item.code - 'A'.code + 1 + 26

fun String.splitHalves(): Pair<String, String> = Pair(substring(0, length / 2), substring(length / 2))