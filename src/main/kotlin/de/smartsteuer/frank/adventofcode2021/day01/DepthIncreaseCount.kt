package de.smartsteuer.frank.adventofcode2021.day01

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val depths    = lines("/day01/depths.txt").map { it.toInt() }
  val increases = countIncreases(depths)
  println("increases = $increases")
  val windowedIncreases = countIncreasesWindowed(depths)
  println("windowedIncreases = $windowedIncreases")
}

internal fun countIncreases(depths: List<Int>): Int =
  depths.zipWithNext()
        .count { (depth1, depth2) -> depth2 > depth1 }

internal fun countIncreasesWindowed(depths: List<Int>): Int =
  depths.windowed(3)
        .map { it.sum() }
        .zipWithNext()
        .count { (depth1, depth2) -> depth2 > depth1 }

/*
internal fun countIncreasesOldSchool(depths: List<Int>): Int {
  var count = 0
  if (depths.size > 1) {
    for (i in 0..(depths.size - 2)) {
      if (depths[i + 1] > depths[i]) {
        count++
      }
    }
  }
  return count
}

internal fun countIncreasesWindowedOldSchool(depths: List<Int>): Int {
  var count = 0
  if (depths.size > 3) {
    var currentDepthSum = depths[0] + depths[1] + depths[2]
    var nextDepthSum    = currentDepthSum - depths[0] + depths[3]
    if (nextDepthSum > currentDepthSum) {
      count++
    }
    for (i in 4 until depths.size) {
      currentDepthSum = nextDepthSum
      nextDepthSum    = nextDepthSum - depths[i - 3] + depths[i]
      if (nextDepthSum > currentDepthSum) {
        count++
      }
    }
  }
  return count
}
*/