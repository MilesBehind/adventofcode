package de.smartsteuer.frank

import de.smartsteuer.frank.adventofcode2022.GraphAlgorithms
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DijkstraTest {

  data class Connection(val from: String, val to: String): GraphAlgorithms.Edge<String> {
    override fun from()     = from
    override fun to()       = to
    override fun distance() = 1
  }

  private val edges = setOf(
    Connection("A", "B"),
    Connection("A", "D"),
    Connection("B", "C"),
    Connection("B", "E"),
    Connection("B", "G"),
    Connection("D", "B"),
    Connection("D", "E"),
    Connection("E", "F"),
  )

  @Test
  fun `dijkstra works`() {
    val pathTree = GraphAlgorithms.dijkstra("A", edges, biDirectionalEdges = false)
    GraphAlgorithms.shortestPath(pathTree, "A", "F") shouldBe listOf("A", "B", "E", "F")
    GraphAlgorithms.shortestPath(pathTree, "A", "G") shouldBe listOf("A", "B", "G")
  }
}