package de.smartsteuer.frank.adventofcode2024.day05

import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.parsePageUpdates
import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.part1
import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.part2
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  val pageUpdates = lines("/adventofcode2024/day05/page-update.txt").parsePageUpdates()
  println("part 1: ${part1(pageUpdates)}")
  println("part 2: ${part2(pageUpdates)}")
}

object PrintQueue {
  fun part1(pageUpdates: PageUpdates): Int =
    pageUpdates.pages.filter { pages: List<Int> ->
      pageUpdates.rules.all { it.isSatisfied(pages) }
    }.sumOf { it[it.size / 2] }

  fun part2(pageUpdates: PageUpdates): Int =
    pageUpdates.pages
      .filterNot { pages: List<Int> -> pageUpdates.rules.all { it.isSatisfied(pages) } }
      .map { it.sortedWith { x, y -> if (Rule(x, y) in pageUpdates.rules) 1 else if (Rule(y, x) in pageUpdates.rules) - 1 else 0 } }
      .sumOf { it[it.size / 2] }

  data class PageUpdates(val rules: List<Rule>, val pages: List<List<Int>>)

  data class Rule(val before: Int, val after: Int) {
    fun isSatisfied(pages: List<Int>): Boolean {
      val indexOfBefore = pages.indexOf(before)
      val indexOfAfter  = pages.indexOf(after)
      return if (indexOfBefore >= 0 && indexOfAfter >= 0) indexOfBefore < indexOfAfter else true
    }
  }

  fun List<String>.parsePageUpdates(): PageUpdates =
    PageUpdates(takeWhile { it.isNotBlank() }.map         { line -> line.split("|").map { it.toInt() } }.map { Rule(it.first(), it.last()) },
                dropWhile { it.isNotBlank() }.drop(1).map { line -> line.split(",").map { it.toInt() } })
}