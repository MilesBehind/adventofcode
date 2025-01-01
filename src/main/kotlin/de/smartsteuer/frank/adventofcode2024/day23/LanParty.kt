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

  fun findLargestConnectedSet(connections: Map<String, Set<String>>): Set<String> {
    fun String.connectedToAll(other: Set<String>): Boolean =
      connections.getOrDefault(this, emptySet()).let { connected -> return other.all { it in connected } }

    fun findNextConnectedSets(sets: Set<Set<String>>): Set<Set<String>> =
      sets.flatMapTo(mutableSetOf()) { set ->
        connections.keys.filter { name: String ->
          name !in set && name.connectedToAll(set)
        }.map { candidate -> set + candidate }
      }

    tailrec fun findLargestConnectedSet(sets: Set<Set<String>>): Set<String> =
      if (sets.size == 1) sets.first().sorted().toSet()
      else findLargestConnectedSet(findNextConnectedSets(sets))

    val start: Set<Set<String>> =
      connections.keys
        .flatMapTo(mutableSetOf()) { start ->
          connections.getOrDefault(start, emptySet()).map { setOf(start, it) }
        }
    return findLargestConnectedSet(start)
  }

  fun List<String>.parseConnections(): List<Pair<String, String>> =
    map { line ->
      val (a, b) = line.split("-")
      Pair(a, b)
    }
}