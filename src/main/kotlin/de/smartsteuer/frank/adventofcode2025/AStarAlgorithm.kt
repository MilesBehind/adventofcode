package de.smartsteuer.frank.adventofcode2025

import java.util.*

/**
 * Generic interface for nodes used by the A* algorithm.
 * @param T Type of the node
 */
interface Node<T : Node<T>> {
  /**
   * Heuristic estimate of the cost to the goal.
   * @return cost estimate
   */
  val heuristic: Double

  /**
   * Checks whether this state has reached the goal
   * @return `true` if this state is the goal, `false` otherwise
   */
  val isGoal: Boolean

  /**
   * Returns all possible successor states
   * @return list of pairs of successor states and their costs
   */
  val neighbors: List<Pair<T, Double>> // Pair<Neighbor, cost>

  /**
   * Unique key for state comparison
   * @return unique key
   */
  val key: Any
}

/**
 * Generic A* algorithm.
 * @param T Type of the node
 */
class AStar<T : Node<T>> {

  data class SearchNode<T : Node<T>>(val state: T,
                                     val currentCost: Double,        // Cost from the start to here
                                     val parent: SearchNode<T>? = null) : Comparable<SearchNode<T>> {
    val newCost: Double = currentCost + state.heuristic // f = g + h

    override fun compareTo(other: SearchNode<T>): Int =
      newCost.compareTo(other.newCost)
  }

  fun search(start: T): SearchResult<T>? {
    val openSet = PriorityQueue<SearchNode<T>>()
    val closedSet = mutableSetOf<Any>()
    val costMap = mutableMapOf<Any, Double>()

    val startNode = SearchNode(start, 0.0)
    openSet.add(startNode)
    costMap[start.key] = 0.0

    //var count = 0
    while (openSet.isNotEmpty()) {
      //if (count++ % 10_000 == 0) println("checked ${count - 1} steps")
      val current = openSet.poll()

      if (current.state.isGoal) {
        return reconstructPath(current)
      }

      val currentKey = current.state.key
      if (currentKey in closedSet) continue
      closedSet.add(currentKey)

      for ((neighbor, moveCost) in current.state.neighbors) {
        val neighborKey = neighbor.key
        if (neighborKey in closedSet) continue

        val newCost = current.currentCost + moveCost
        val bestKnownCost = costMap[neighborKey] ?: Double.POSITIVE_INFINITY

        if (newCost < bestKnownCost) {
          costMap[neighborKey] = newCost
          openSet.add(SearchNode(neighbor, newCost, current))
        }
      }
    }
    //println("no solution found after $count steps")
    return null // No solution found
  }

  private fun reconstructPath(node: SearchNode<T>): SearchResult<T> {
    val path = mutableListOf<T>()
    var current: SearchNode<T>? = node

    while (current != null) {
      path.add(current.state)
      current = current.parent
    }

    return SearchResult(
      path  = path.reversed(),
      cost  = node.currentCost,
      steps = path.size - 1
    )
  }
}

/**
 * Result of an A* search
 */
data class SearchResult<T>(val path:  List<T>,
                           val cost:  Double,
                           val steps: Int)

