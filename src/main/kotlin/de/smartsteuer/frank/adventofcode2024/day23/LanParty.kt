package de.smartsteuer.frank.adventofcode2024.day23

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  LanParty.execute(lines("/adventofcode2024/day23/connections.txt"))
}

object LanParty: Day<String> {
  override fun part1(input: List<String>): String =
    findTriples(computeConnections(input.parseConnections())).toString()

  override fun part2(input: List<String>): String =
    findLargestConnectedSet(computeConnections(input.parseConnections())).joinToString(",")

  fun computeConnections(pairs: List<Pair<String, String>>): Map<String, Set<String>> {
    val connectionsWithDistance = pairs.fold(mutableMapOf<String, MutableSet<String>>()) { result, (a, b) ->
      result.also {
        result.getOrPut(a) { mutableSetOf() }.add(b)
        result.getOrPut(b) { mutableSetOf() }.add(a)
      }
    }
    return connectionsWithDistance
  }

  fun findTriples(connections: Map<String, Set<String>>): Int {
    val tNodes = connections.keys.filter { it.first() == 't' }
    val triples = mutableSetOf<Set<String>>()
    tNodes.forEach { tNode ->
      val siblingsLevel1: Set<String> = connections[tNode] ?: emptySet()
      siblingsLevel1.forEach { siblingLevel1: String ->
        val siblingsLevel2: Set<String> = (connections[siblingLevel1] ?: emptySet()) - siblingLevel1 - tNode
        siblingsLevel2.forEach { siblingLevel2 ->
          val siblingsLevel3 = (connections[siblingLevel2] ?: emptySet()) - siblingLevel1 - siblingLevel2
          siblingsLevel3.forEach { siblingLevel3 ->
            if (siblingLevel3 == tNode) {
              triples += setOf(tNode, siblingLevel1, siblingLevel2)
            }
          }
        }
      }
    }
    println("triples:")
    println(triples.joinToString("\n"))
    return triples.size
  }

  fun findLargestConnectedSet(connections: Map<String, Set<String>>): List<String> {
    fun connections(name: String): Set<String> =
      connections.getOrDefault(name, emptySet())

    infix fun String.isConnectedTo(otherName: String) =
      otherName in connections.getOrDefault(this, setOf(otherName))

    var count = 0

    tailrec fun findLargestConnectedSet(sequences: MutableList<MutableList<String>>, set: MutableList<String>, largestSet: List<String>): List<String> {
      //println("findLargestConnectedSet(${sequences.dropLast(1).size}, ${sequences.last()}, $set, $largestSet)")
      if (count++ % 100_000 == 0) println(count)
      if (sequences.isEmpty()) return largestSet
      val lastSequence = sequences.last()
      if (lastSequence.isEmpty()) return findLargestConnectedSet(sequences.apply { removeLast() }, set.apply { if (set.isNotEmpty()) removeLast() }, largestSet)
      if (sequences.size == 1) {
        println("-------------" + sequences.last().last() + "---------------")
      }
      val index = lastSequence.indexOfLast { it !in set }
      if (index < 0) return findLargestConnectedSet(sequences.apply { removeLast() }, set.apply { if (set.isNotEmpty()) removeLast() }, largestSet)
      repeat(index) { lastSequence.removeLast() }
      val nextName = lastSequence.removeLast()
      //println("nextName = $nextName")
      //if (nextName == "co") {
      //  println("co!!!!!!!!!!!!!!!!!!")
      //}
      //println("$nextName is connected to ${connections(nextName)}")
      val nextSequence = connections(nextName).filter { next -> set.all { other -> next isConnectedTo other } }
      set += nextName
      val newLargestSet = if (set.size > largestSet.size) set.toMutableList() else largestSet
      //println("nextSequence: $nextSequence")
      return findLargestConnectedSet(sequences.apply { add(nextSequence.toMutableList()) }, set, newLargestSet)
    }
    return findLargestConnectedSet(mutableListOf(connections.keys.toMutableList()), mutableListOf(), listOf()).sorted()
  }

  fun List<String>.parseConnections(): List<Pair<String, String>> =
    map { line ->
      val (a, b) = line.split("-")
      Pair(a, b)
    }
}