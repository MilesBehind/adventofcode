package de.smartsteuer.frank.adventofcode2024.day23

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.day23.LanParty.NodeAndDistance.Companion.nodeAndDistance
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  LanParty.execute(lines("/adventofcode2024/day23/connections.txt"))
}

object LanParty: Day<Int> {
  override fun part1(input: List<String>): Int =
    findTriples(computeConnections(input.parseConnections()))

  override fun part2(input: List<String>): Int {
    TODO("Not yet implemented")
  }

  data class NodeAndDistance(val from: String, val distance: Int) {
    companion object {
      private val cache = mutableMapOf<String, MutableMap<Int, NodeAndDistance>>()
      fun nodeAndDistance(node: String, distance: Int): NodeAndDistance =
        cache.getOrPut(node) { mutableMapOf() }.getOrPut(distance) { NodeAndDistance(node, distance) }
    }
  }

  fun computeConnections(pairs: List<Pair<String, String>>): Map<NodeAndDistance, Set<String>> {
    val connectionsWithDistance = pairs.fold(mutableMapOf<NodeAndDistance, MutableSet<String>>()) { result, (a, b) ->
      result.also {
        result.getOrPut(nodeAndDistance(a, 1)) { mutableSetOf() }.add(b)
        result.getOrPut(nodeAndDistance(b, 1)) { mutableSetOf() }.add(a)
      }
    }
    return connectionsWithDistance
  }

  fun findTriples(connections: Map<NodeAndDistance, Set<String>>): Int {
    val tNodes = connections.keys.map { it.from }.filter { it.first() == 't' }
    val triples = mutableSetOf<Set<String>>()
    tNodes.forEach { tNode ->
      val siblingsLevel1: Set<String> = connections[nodeAndDistance(tNode, 1)] ?: emptySet()
      siblingsLevel1.forEach { siblingLevel1: String ->
        val siblingsLevel2: Set<String> = (connections[nodeAndDistance(siblingLevel1, 1)] ?: emptySet()) - siblingLevel1 - tNode
        siblingsLevel2.forEach { siblingLevel2 ->
          val siblingsLevel3 = (connections[nodeAndDistance(siblingLevel2, 1)] ?: emptySet()) - siblingLevel1 - siblingLevel2
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

  fun List<String>.parseConnections(): List<Pair<String, String>> =
    map { line ->
      val (a, b) = line.split("-")
      Pair(a, b)
    }
}