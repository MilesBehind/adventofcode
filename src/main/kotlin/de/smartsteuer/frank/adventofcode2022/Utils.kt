package de.smartsteuer.frank.adventofcode2022

import java.net.URL

fun resource(path: String): URL? = object {}.javaClass.getResource(path)

fun lines(path: String): List<String> = resource(path)?.openStream()?.reader()?.readLines() ?: emptyList()

fun linesSequence(path: String): Sequence<String> {
  return resource(path)?.openStream()?.reader()?.buffered(20_000)?.let { reader ->
    generateSequence { reader.readLine() }
  } ?: emptySequence()
}

fun text(path: String): String = resource(path)?.openStream()?.reader()?.readText() ?: ""



fun Sequence<String>.extractNumbers(regex: Regex): Sequence<List<Int>> =
  map { line ->
    regex.matchEntire(line)?.let { matchResult ->
      matchResult.groupValues.drop(1).map { it.toInt() }
    } ?: throw IllegalArgumentException("could not parse '$line'")
  }

fun List<String>.extractStrings(regex: Regex): List<List<String>> =
  map { line ->
    regex.matchEntire(line)?.groupValues?.drop(1) ?: throw IllegalArgumentException("could not parse '$line'")
  }



@Suppress("RemoveExplicitTypeArguments")
fun List<IntRange>.merge(other: IntRange): List<IntRange> {
  tailrec fun merge(intervals: List<IntRange>, result: List<IntRange>): List<IntRange> {
    if (intervals.isEmpty()) return result
    val interval = intervals.first()
    return when {
      result.isEmpty() || result.last().last < interval.first -> {
        merge(intervals.drop(1), result.plus<IntRange>(interval))
      }
      else                                                    -> {
        val newLastInterval = IntRange(result.last().first, result.last().last.coerceAtLeast(interval.last))
        merge(intervals.drop(1), result.dropLast(1).plus<IntRange>(newLastInterval))
      }
    }
  }
  return merge(this.plus<IntRange>(other).sortedBy { it.first }, emptyList())
}

fun IntRange.clamp(other: IntRange): IntRange = when {
  this.first > other.last  -> IntRange.EMPTY
  this.last  < other.first -> IntRange.EMPTY
  else                     -> (first.coerceAtLeast(other.first))..(last.coerceAtMost(other.last))
}



fun <T> List<T>.cycle(): Sequence<T> {
  var index = 0
  return generateSequence { this[index++ % size] }
}

fun <T> List<T>.replaced(index: Int, element: T): List<T> = take(index) + element + drop(index + 1)

/**
 * Creates sequence of each element of the list, where each is combined with a list of all other elements in the list.
 * @return sequence of pairs with each element as first and all other element as second element
 */
fun <T> List<T>.eachAndOthers(): Sequence<Pair<T, List<T>>> =
  if (this.isEmpty()) emptySequence() else indices.asSequence().map { index -> get(index) to take(index) + drop(index + 1) }

fun <T> List<T>.combinations(): List<List<T>> {
  tailrec fun combinations(list: List<T>, result: List<List<T>>): List<List<T>> {
    if (list.size < 2) return result
    val allButFirst = list.drop(1)
    val first = list.first()
    val combinationsWithFirst = allButFirst.map { listOf(first, it) }
    return combinations(allButFirst, result + combinationsWithFirst)
  }
  return combinations(this, emptyList())
}

fun <T> List<T>.permutations(): Sequence<List<T>> {
  if (isEmpty()) return emptySequence()
  if (size < 2) return sequenceOf(this)
  var index = 0
  var head  = listOf(this.first())
  var tail  = subList(1, size).permutations().iterator()
  return generateSequence {
    if (!tail.hasNext()) {
      index++
      if (index >= size) {
        return@generateSequence null
      }
      head = listOf(this[index])
      tail = (this.take(index) + this.drop(index + 1)).permutations().iterator()
    }
    head + tail.next()
  }
}



object GraphAlgorithms {
  data class FloydMarshalResult<N>(val nodeInfos: Map<Pair<N, N>, NodeInfo<N>>) {
    data class NodeInfo<N>(val distance: Int, val successor: N)

    fun path(from: N, to: N): List<N> {
      tailrec fun path(from: N, to: N, result: List<N>): List<N> {
        val nodeInfo = nodeInfos[from to to]
        return if (nodeInfo == null || from == to) result else path(nodeInfo.successor, to, result + nodeInfo.successor)
      }
      return path(from, to, listOf(from))
    }

    fun distance(from: N, to: N): Int? = nodeInfos[from to to]?.distance
  }

  interface Edge<N> {
    fun from(): N
    fun to(): N
    fun distance(): Int
    operator fun contains(node: N) = from() == node || to() == node
    fun other(node: N) = if (from() == node) to() else from()
  }

  fun <N, E: Edge<N>> floydMarshall(nodes: List<N>, edges: List<E>, bidirectional: Boolean = true): FloydMarshalResult<N> {
    operator fun Map<Pair<N, N>, Int>.invoke(key: Pair<N, N>) = this.getOrDefault(key, Int.MAX_VALUE / 2)
    val distances  = mutableMapOf<Pair<N, N>, Int>()
    val successors = mutableMapOf<Pair<N, N>, N>()
    for (edge in edges) {
      distances [edge.from() to edge.to()] = edge.distance()
      successors[edge.from() to edge.to()] = edge.to()
      if (bidirectional) {
        distances [edge.to() to edge.from()] = edge.distance()
        successors[edge.to() to edge.from()] = edge.from()
      }
    }
    for (node in nodes) {
      distances [node to node] = 0
      successors[node to node] = node
    }
    for (k in nodes) {
      for (i in nodes) {
        for (j in nodes) {
          if (distances(i to j) > distances(i to k) + distances(k to j)) {
            distances [i to j] = distances(i to k) + distances(k to j)
            if (successors[i to k] != null) {
              successors[i to j] = successors.getValue(i to k)
            }
          }
        }
      }
    }
    return FloydMarshalResult(distances.mapValues { (key, distance) -> FloydMarshalResult.NodeInfo(distance, successors.getValue(key)) })
  }

  data class State<N>(val result: Map<N, Int>, val predecessors: Map<N, N>)

  private fun <N> dijkstra(start: N, edges: Set<Edge<N>>): Map<N, N> {
    tailrec fun go(active: Set<N>, state: State<N>): State<N> {
      if (active.isEmpty()) return state
      val node = active.minBy { state.result.getValue(it) }
      val cost = state.result.getValue(node)
      val neighboursAndCosts = edges.filter { node in it }.map { it.other(node) }
        .filter { neighbour -> cost + 1 < state.result.getOrDefault(neighbour, Int.MAX_VALUE) }
        .associateWith { cost + 1 }
      val active1 = active - node + neighboursAndCosts.keys
      val predecessors = neighboursAndCosts.mapValues { node }
      return go(active1, State(state.result + neighboursAndCosts, state.predecessors + predecessors))
    }
    return go(setOf(start), State(mapOf(start to 0), emptyMap())).predecessors
  }

  private fun <N> shortestPath(shortestPathTree: Map<N, N>, start: N, end: N): List<N> {
    tailrec fun pathTo(start: N, end: N, result: List<N>): List<N> =
      if (shortestPathTree[end] == null) result + listOf(end) else pathTo(start, shortestPathTree[end]!!, result + listOf(end))
    return pathTo(start, end, emptyList())
  }

  /*
    fun distances(): Map<Valve, Int> {
      val pathTree = dijkstra(start)
      return valves.values.associateWith { valve -> shortestPath(pathTree, start, valve).size - 1 }
    }

    fun maximumPressureGain(): List<Pair<Valve, Int>> {
      val distances = distances()
      return distances.mapValues { (valve, distance) -> (MINUTES - (distance + VALVE_OPEN_NEEDED_MINUTES)) * valve.rate }.toList().sortedBy { it.second }
    }
  */
}
