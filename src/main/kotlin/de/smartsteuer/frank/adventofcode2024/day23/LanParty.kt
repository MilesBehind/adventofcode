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
    fun String.connectedToAll(other: Set<String>): Boolean {
      val connected = connections.getOrDefault(this, emptySet())
      return other.all { it in connected }
    }

    fun findNextConnectedSets(sets: Set<Set<String>>): Set<Set<String>> =
      sets.first().size.let { length ->
        sets.flatMapTo(mutableSetOf()) { set: Set<String> ->
          val names: List<String> = connections.keys.filter { name: String -> name !in set && name.connectedToAll(set) }
          names.flatMap { name -> connections.getOrDefault(name, emptySet()).map { set + it } }
        }.filter { it.size == length + 1 }.toSet()
      }

    val length2: Set<Set<String>> = connections.keys.flatMapTo(mutableSetOf()) { start -> connections.getOrDefault(start, emptySet()).map { setOf(start, it) } }
    val length3: Set<Set<String>> = findNextConnectedSets(length2)
    val length4: Set<Set<String>> = findNextConnectedSets(length3)

    println("length2: ${length2.size}")
    println("length3: ${length3.size}")
    println("length4: ${length4.size}")
    return length4.first()
  }

  fun List<String>.parseConnections(): List<Pair<String, String>> =
    map { line ->
      val (a, b) = line.split("-")
      Pair(a, b)
    }
}