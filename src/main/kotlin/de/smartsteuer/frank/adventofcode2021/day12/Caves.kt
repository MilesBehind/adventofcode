package de.smartsteuer.frank.adventofcode2021.day12

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val caves = Caves(lines("/adventofcode2021/day12/caves.txt"))
  println("caves: ${caves.caves}")
  val pathCount = caves.countPaths()
  println("path count = $pathCount")
  val pathCount2 = caves.countPaths2()
  println("path count 2 = $pathCount2")
}

class Caves(lines: List<String>) {
  val caves = fromDefinition(lines)

  private tailrec fun fromDefinition(lines: List<String>, caves: Map<String, Set<String>> = emptyMap()): Map<String, Set<String>> =
    if (lines.isEmpty()) caves else {
      val (cave1, cave2) = lines[0].split('-').take(2)
      val (connected1, connected2) = listOf(cave1, cave2).map { caves.getOrDefault(it, emptySet()) }
      fromDefinition(lines.drop(1), caves + (cave1 to connected1 + cave2) + (cave2 to connected2 + cave1))
    }

  fun countPaths(): Int = countPaths("start")

  fun countPaths2(): Int = countPaths("start", visitedSmallCave = false)

  private fun countPaths(currentCave: String, visitedCaves: Set<String> = setOf(currentCave), visitedSmallCave: Boolean = true): Int {
    val connectedCaves = caves[currentCave]?.filter { cave ->
      (cave.isBig() || cave !in visitedCaves || !visitedSmallCave) && !cave.isStart()
    } ?: error("cave '$currentCave' not found")
    return when {
      currentCave.isEnd()      -> visitedCaves.size.coerceAtMost(1)
      connectedCaves.isEmpty() -> 0
      else                     -> connectedCaves.sumOf { cave -> countPaths(cave, visitedCaves + cave,
                                                                            visitedSmallCave || !cave.isBig() && cave in visitedCaves) }
    }
  }

  private fun String.isBig()   = first().isUpperCase()
  private fun String.isStart() = this == "start"
  private fun String.isEnd()   = this == "end"
}
