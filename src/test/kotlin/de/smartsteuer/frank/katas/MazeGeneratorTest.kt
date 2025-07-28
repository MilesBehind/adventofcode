package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.MazeGenerator.Pos
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MazeGeneratorTest {

  private val width  = 10
  private val height = 10
  private val maze   = SimpleMazeGenerator.generate(width, height)

  @Test
  fun `check that all cells are present`() {
    width * height + 2 shouldBe maze.cells.size
  }

  @Test
  fun `check that each cell has the correct coordinates`() {
    for (y in 0 until height) {
      for (x in 0 until width) {
        maze.cells.keys.any { it.x == x && it.y == y }.shouldBeTrue()
      }
    }
  }

  @Test
  fun `check that the maze is connected (every cell can be reached from the start)`() {
    val startCell = maze.cells.keys.first()
    val visited = mutableSetOf<Pos>()

    // DFS to visit all reachable cells
    fun dfs(pos: Pos) {
      if (pos in visited) return
      visited += pos
      for (neighbor in maze.cells.getValue(pos)) {
        dfs(neighbor)
      }
    }
    dfs(startCell)
    width * height + 2 shouldBe visited.size
  }

  @Test
  fun `check that connections are bidirectional`() {
    for (cell in maze.cells.keys.filter { it != maze.start && it != maze.end }) {
      for (neighbor in maze.cells.getValue(cell)) {
        val neighbourCells = maze.cells.getValue(neighbor)
        withClue("cell = $cell, neighbourCells = $neighbourCells") {
          neighbourCells.any { it.x == cell.x && it.y == cell.y}.shouldBeTrue()
        }
      }
    }
  }

  @Test
  fun `check that the maze has no cycles (is a tree)`() {
    // A tree with n nodes has exactly n-1 edges
    val totalEdges = maze.cells.values.sumOf { it.size } / 2  // Divide by 2 because each edge is counted twice
    width * height + 1 shouldBe totalEdges
  }
}
