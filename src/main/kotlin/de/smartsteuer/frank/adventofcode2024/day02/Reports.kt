package de.smartsteuer.frank.adventofcode2024.day02

import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  val reports: List<List<Int>> = lines("/adventofcode2024/day02/reports.txt").parseReports()
  println("part 1: ${part1(reports)}")
  println("part 2: ${part2(reports)}")
}

internal fun part1(reports: List<List<Int>>): Int =
  reports.count { report ->
    report.zipWithNext { first, second -> first - second }.let { differences ->
      differences.all { it in 1..3} ||
      differences.all { it in -1 downTo -3 }
    }
  }

internal fun part2(reports: List<List<Int>>): Int =
  reports.count { report ->
    (report.indices.map { indexToDrop -> report.filterIndexed { index, _  -> index != indexToDrop } } + listOf(report)).any { modifiedReport ->
      modifiedReport.zipWithNext { first, second -> first - second }.let { differences ->
        differences.all { it in 1..3 } || differences.all { it in -1 downTo -3 }
      }
    }
  }

internal fun List<String>.parseReports(): List<List<Int>> =
  this.map { report -> report.split(' ').map { level -> level.toInt() } }
