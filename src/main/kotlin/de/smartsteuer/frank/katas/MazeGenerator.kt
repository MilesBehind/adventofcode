package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.MazeGenerator.Cell
import de.smartsteuer.frank.katas.MazeGenerator.Maze
import kotlin.random.Random

interface MazeGenerator {
  data class Cell(val x: Int, val y: Int, val connections: List<Cell> = emptyList()) {
    override fun equals(other: Any?): Boolean = this === other || (other is Cell) && x == other.x && y == other.y
    override fun hashCode(): Int = 31 * x + y
  }

  data class Maze(val cells: List<Cell>, val start: Cell, val end: Cell)

  fun generate(width: Int, height: Int): Maze
}



object SimpleMazeGenerator: MazeGenerator {

  override fun generate(width: Int, height: Int): Maze {
    require(width > 0 && height > 0) { "Width and height must be > 0" }

    val grid = Array(height) { y -> Array(width) { x -> Cell(x, y, mutableListOf()) } }
    val visited = Array(height) { BooleanArray(width) }

    fun neighbors(x: Int, y: Int): List<Pair<Int, Int>> =
      listOf(
        x to y - 1, // up
        x + 1 to y, // right
        x to y + 1, // down
        x - 1 to y  // left
      ).filter { (nx, ny) -> nx in 0 until width && ny in 0 until height && !visited[ny][nx] }
        .shuffled()

    fun dfs(x: Int, y: Int) {
      visited[y][x] = true
      for ((nx, ny) in neighbors(x, y)) {
        if (!visited[ny][nx]) {
          val current = grid[y][x]
          val neighbor = grid[ny][nx]
          (current.connections as MutableList).add(neighbor)
          (neighbor.connections as MutableList).add(current)
          dfs(nx, ny)
        }
      }
    }

    dfs(0, 0) // start anywhere; could also randomize

    val cells = grid.flatten()

    // Wähle zufällig obere/untere oder linke/rechte Seite
    val vertical = Random.nextBoolean()

    val (start, end) = if (vertical) {
      // oben/unten
      val startX = Random.nextInt(width)
      val endX = Random.nextInt(width)
      val startCell = Cell(startX, -1, listOf())
      val endCell = Cell(endX, height, listOf())

      // verbinde mit Randzellen
      val entry = grid[0][startX]
      val exit = grid[height - 1][endX]
      (entry.connections as MutableList).add(startCell)
      (exit.connections as MutableList).add(endCell)

      startCell to endCell
    } else {
      // links/rechts
      val startY = Random.nextInt(height)
      val endY = Random.nextInt(height)
      val startCell = Cell(-1, startY, listOf())
      val endCell = Cell(width, endY, listOf())

      val entry = grid[startY][0]
      val exit = grid[endY][width - 1]
      (entry.connections as MutableList).add(startCell)
      (exit.connections as MutableList).add(endCell)

      startCell to endCell
    }
    val connectedStart = start.copy(connections = cells.filter { start in it.connections }.map { it.copy(connections = emptyList()) })
    val connectedEnd   = end.copy  (connections = cells.filter { end   in it.connections }.map { it.copy(connections = emptyList()) })
    return Maze(cells + listOf(connectedStart, connectedEnd), connectedStart, connectedEnd)
  }
}

/**
 * Renders a maze as a string using Unicode box drawing characters.
 * @param maze The maze to render
 * @return A string representation of the maze
 */
fun renderMaze(maze: Maze): String {
  data class Pos(val x: Int, val y: Int)
  data class LineSpec(val left: Boolean, val right: Boolean, val top: Boolean, val bottom: Boolean)
  val mazeCells = maze.cells.filter { it !== maze.start && it !== maze.end }
  val width     = mazeCells.maxOf { it.x } + 1
  val height    = mazeCells.maxOf { it.y } + 1
  val grid      = maze.cells.associateBy { Pos(it.x, it.y) }
  val symbols   = mapOf(
    LineSpec(left = false, right = false, top = false, bottom = false) to "  ",
    LineSpec(left = false, right = false, top = false, bottom = true ) to "╷ ",
    LineSpec(left = false, right = false, top = true,  bottom = false) to "╵ ",
    LineSpec(left = false, right = false, top = true,  bottom = true ) to "│ ",
    LineSpec(left = false, right = true,  top = false, bottom = false) to "╶─",
    LineSpec(left = false, right = true,  top = false, bottom = true ) to "┌─",
    LineSpec(left = false, right = true,  top = true,  bottom = false) to "└─",
    LineSpec(left = false, right = true,  top = true,  bottom = true ) to "├─",
    LineSpec(left = true,  right = false, top = false, bottom = false) to "╴ ",
    LineSpec(left = true,  right = false, top = false, bottom = true ) to "┐ ",
    LineSpec(left = true,  right = false, top = true,  bottom = false) to "┘ ",
    LineSpec(left = true,  right = false, top = true,  bottom = true ) to "┤ ",
    LineSpec(left = true,  right = true,  top = false, bottom = false) to "──",
    LineSpec(left = true,  right = true,  top = false, bottom = true ) to "┬─",
    LineSpec(left = true,  right = true,  top = true,  bottom = false) to "┴─",
    LineSpec(left = true,  right = true,  top = true,  bottom = true ) to "┼─"
  )

  fun renderCell(x: Int, y: Int): String {
    val pos                = Pos(x, y)
    val cell               = grid[pos] ?: Cell(x, y)
    val connectedToLeft    = cell.connections.any { it.x == x - 1 }
    val connectedToTop     = cell.connections.any { it.y == y - 1 }
    val leftCell           = grid[Pos(x - 1, y    )] ?: Cell(x - 1, y)
    val topCell            = grid[Pos(x,     y - 1)] ?: Cell(x,     y - 1)
    val leftConnectedToTop = leftCell.connections.any { it.y == leftCell.y - 1 }
    val topConnectedToLeft = topCell.connections.any  { it.x == topCell.x  - 1 }
    val lineSpec           = LineSpec(left   = !leftConnectedToTop && (x > 0),
                                      right  = !connectedToTop     && (x < width),
                                      top    = !topConnectedToLeft && (y > 0),
                                      bottom = !connectedToLeft    && (y < height))
    return symbols.getValue(lineSpec)
  }
  return buildString {
    (0..height).forEach { y ->
      (0..width).forEach { x ->
        append(renderCell(x, y))
      }
      appendLine()
    }
  }
}


fun main() {
  val maze = SimpleMazeGenerator.generate(110, 50)
  println(renderMaze(maze))
  /*
  for (y in 0..9) {
    for (x in 0..9) {
      print(maze.cells.find { it.x == x && it.y == y }?.let { "${it.x}/${it.y} -> ${it.connections.map { c -> "${c.x}/${c.y}" } } ".padEnd(24)})
    }
    println()
  }
  println("Start: ${maze.start}")
  println("End:   ${maze.end}")
  */
}
