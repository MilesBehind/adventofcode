package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.MazeGenerator.Maze
import de.smartsteuer.frank.katas.MazeGenerator.Pos
import kotlin.random.Random

interface MazeGenerator {
  data class Pos(val x: Int, val y: Int) {
    fun neighbours() = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos(x, y + 1))
  }
  data class Maze(val cells: Map<Pos, List<Pos>>, val start: Pos, val end: Pos)
  fun generate(width: Int, height: Int): Maze
}



object SimpleMazeGenerator: MazeGenerator {

  override fun generate(width: Int, height: Int): Maze {
    require(width > 0 && height > 0) { "Width and height must be > 0" }

    val grid: MutableMap<Pos, MutableList<Pos>> = mutableMapOf()
    val visited: MutableMap<Pos, Boolean> = mutableMapOf()
    (0 until height).forEach { y ->
      (0 until width).forEach { x ->
        grid[Pos(x, y)] = mutableListOf()
        visited[Pos(x, y)] = false
      }
    }

    fun dfs(pos: Pos) {
      visited[pos] = true
      val neighbours = pos.neighbours().filter { neighbour ->
        neighbour.x in (0 until width) &&
        neighbour.y in (0 until height) &&
        grid.getValue(neighbour).isEmpty()
      }.shuffled()
      for (neighbour in neighbours) {
        if (visited[neighbour] == false) {
          val current = grid.getValue(pos)
          val neighbours = grid.getValue(neighbour)
          current.add(neighbour)
          neighbours.add(pos)
          dfs(neighbour)
        }
      }
    }

    dfs(Pos(0, 0))

    val vertical = Random.nextBoolean()
    val (start, end) = if (vertical) {
      val startX = Random.nextInt(width)
      val endX = Random.nextInt(width)

      val startPos = Pos(startX, -1)
      val endPos = Pos(endX, height)

      grid.getValue(Pos(startX, 0)).add(startPos)
      grid.getValue(Pos(endX, height - 1)).add(endPos)

      (startPos to listOf(Pos(startX, 0))) to (endPos to listOf(Pos(endX, height - 1)))
    } else {
      val startY = Random.nextInt(height)
      val endY = Random.nextInt(height)

      val startPos = Pos(-1, startY)
      val endPos = Pos(width, endY)

      grid.getValue(Pos(0, startY)).add(startPos)
      grid.getValue(Pos(width - 1, endY)).add(endPos)

      (startPos to listOf(Pos(0, startY))) to (endPos to listOf(Pos(width - 1, endY)))
    }

    return Maze(grid + start + end, start.first, end.first)
  }
}

/**
 * Renders a maze as a string using Unicode box drawing characters.
 * @receiver The maze to render
 * @return A string representation of the maze
 */
fun Maze.render(): String {
  data class LineSpec(val left: Boolean, val right: Boolean, val top: Boolean, val bottom: Boolean)
  val mazeCells = cells.filterKeys { it !== start && it !== end }
  val width     = mazeCells.keys.maxOf { it.x } + 1
  val height    = mazeCells.keys.maxOf { it.y } + 1
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
    val pos                 = Pos(x, y)
    val leftPos             = Pos(x - 1, y)
    val topPos              = Pos(x, y - 1)
    val connections         = cells[pos] ?: emptyList()
    val connectedToLeft     = connections.any { it.x == x - 1 }
    val connectedToTop      = connections.any { it.y == y - 1 }
    val leftCellConnections = cells[leftPos] ?: emptyList()
    val topCellConnections  = cells[topPos]  ?: emptyList()
    val leftConnectedToTop  = leftCellConnections.any { it.y == leftPos.y - 1 }
    val topConnectedToLeft  = topCellConnections.any  { it.x == topPos.x  - 1 }
    val lineSpec            = LineSpec(left   = !leftConnectedToTop && (x > 0),
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

fun Maze.renderBlocks(): String =
  buildString {
    val mazeCells = cells.filterKeys { it !== start && it !== end }
    val maxX      = mazeCells.keys.maxOf { it.x } + 1
    val maxY      = mazeCells.keys.maxOf { it.y } + 1
    (0..maxY).forEach { y ->
      (0..maxX).forEach { x ->
        val connections    = cells[Pos(x, y)] ?: emptyList()
        val connectedToTop = connections.any { it.y == y - 1 } || (x == maxX)
        append("██")
        append(if (connectedToTop) "  " else "██")
      }
      appendLine()
      (0..maxX).forEach { x ->
        val connections     = cells[Pos(x, y)] ?: emptyList()
        val connectedToLeft = connections.any { it.x == x - 1 } || (y == maxY)
        append(if (connectedToLeft) "  " else "██")
        append("  ")
      }
      appendLine()
    }
  }


fun main() {
  //val maze = SimpleMazeGenerator.generate(110, 50)
  val maze = SimpleMazeGenerator.generate(10, 10)
  println(maze.render())
  println()
  println(maze.renderBlocks())

  /*
  for (y in 0..9) {
    for (x in 0..9) {
      print(maze.cells[Pos(x, y)]?.let { "$x/$y -> ${it.map { c -> "${c.x}/${c.y}" } } ".padEnd(24)})
    }
    println()
  }
  println("Start: ${maze.start} -> ${maze.cells[maze.start]}")
  println("End:   ${maze.end} -> ${maze.cells[maze.end]}")
  */
}

