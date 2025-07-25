package de.smartsteuer.frank.katas

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class MazeGeneratorTest {

  private val width  = 10
  private val height = 10
  private val maze   = SimpleMazeGenerator.generate(width, height)

  @Test
  fun `check that all cells are present`() {
    width * height shouldBe maze.cells.size
  }

  @Test
  fun `check that each cell has the correct coordinates`() {
    for (y in 0 until height) {
      for (x in 0 until width) {
        maze.cells.any { it.x == x && it.y == y }.shouldBeTrue()
      }
    }
  }

  @Test
  fun `check that the maze is connected (every cell can be reached from the start)`() {
    val startCell = maze.cells.first()
    val visited = mutableSetOf<MazeGenerator.Cell>()

    // DFS to visit all reachable cells
    fun dfs(cell: MazeGenerator.Cell) {
      if (cell in visited) return
      visited.add(cell)
      for (neighbor in cell.connections) {
        dfs(neighbor)
      }
    }

    dfs(startCell)
    width * height shouldBe visited.size
  }

  @Test
  fun `check that connections are bidirectional`() {
    for (cell in maze.cells) {
      for (neighbor in cell.connections) {
        neighbor.connections.contains(cell).shouldBeTrue()
      }
    }
  }

  @Test
  fun `check that the maze has no cycles (is a tree)`() {
    // A tree with n nodes has exactly n-1 edges
    val totalEdges = maze.cells.sumOf { it.connections.size } / 2  // Divide by 2 because each edge is counted twice
    width * height - 1 shouldBe totalEdges
  }

  @Test
  fun `test renderMaze function`() {
    // Generate a small maze for easier testing
    val smallWidth = 3
    val smallHeight = 3
    val smallMaze = SimpleMazeGenerator.generate(smallWidth, smallHeight)

    // Render the maze
    val renderedMaze = renderMaze(smallMaze)

    // Print the rendered maze for visual inspection
    println("[DEBUG_LOG] Rendered maze (3x3):")
    println("[DEBUG_LOG] $renderedMaze")

    // Also print a larger maze for better visual inspection
    val mediumMaze = SimpleMazeGenerator.generate(5, 5)
    val renderedMediumMaze = renderMaze(mediumMaze)
    println("[DEBUG_LOG] \nRendered maze (5x5):")
    println("[DEBUG_LOG] $renderedMediumMaze")

    // Check that the rendered maze contains the expected Unicode box drawing characters
    renderedMaze shouldContain "┌"
    renderedMaze shouldContain "┐"
    renderedMaze shouldContain "└"
    renderedMaze shouldContain "┘"
    renderedMaze shouldContain "─"
    renderedMaze shouldContain "│"

    // Check that the rendered maze has the correct number of lines
    // For a 3x3 maze, we expect:
    // - 1 line for the top border
    // - 3 lines for the cells
    // - 2 lines for the horizontal walls between rows
    // - 1 line for the bottom border
    // Total: 7 lines
    val lines = renderedMaze.split("\n")
    lines.size shouldBe 7

    // Check that the first line starts with ┌ and ends with ┐
    lines[0].first() shouldBe '┌'
    lines[0].last() shouldBe '┐'

    // Check that the last line starts with └ and ends with ┘
    lines[6].first() shouldBe '└'
    lines[6].last() shouldBe '┘'
  }
}
