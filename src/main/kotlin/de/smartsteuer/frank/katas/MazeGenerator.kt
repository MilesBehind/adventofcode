package de.smartsteuer.frank.katas

import java.time.Duration
import kotlin.random.Random

data class Pos(val x: Int, val y: Int) {
  fun neighbours() = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos(x, y + 1))
}
data class Maze(val cells: Map<Pos, List<Pos>>, val start: Pos, val end: Pos)


fun generateMaze(width: Int, height: Int): Maze {
  require(width > 0 && height > 0) { "Width and height must be positive" }
  val connections = mutableMapOf<Pos, MutableList<Pos>>()
  for (x in 0 until width) for (y in 0 until height) connections[Pos(x, y)] = mutableListOf()
  val visited = mutableSetOf<Pos>()
  val startInner = Pos(0, 0)
  visited += startInner

  tailrec fun carve(stack: List<Pos>) {
    if (stack.isEmpty()) return
    val current = stack.first()
    val unvisited = current.neighbours().filter { it.x in 0 until width && it.y in 0 until height && it !in visited }
    if (unvisited.isNotEmpty()) {
      val next = unvisited.random()
      connections[current]!!.add(next)
      connections[next]!!.add(current)
      visited += next
      carve(listOf(next) + stack)
    } else {
      carve(stack.drop(1))
    }
  }

  carve(listOf(startInner))
  val horizontal = Random.nextBoolean()
  val start: Pos
  val end: Pos
  if (horizontal) {
    val y = Random.nextInt(height)
    start = Pos(-1, y).also {
      connections.getOrPut(it) { mutableListOf() }.add(Pos(0, y))
      connections[Pos(0, y)]!!.add(it)
    }
    end = Pos(width, y).also {
      connections.getOrPut(it) { mutableListOf() }.add(Pos(width - 1, y))
      connections[Pos(width - 1, y)]!!.add(it)
    }
  } else {
    val x = Random.nextInt(width)
    start = Pos(x, -1).also {
      connections.getOrPut(it) { mutableListOf() }.add(Pos(x, 0))
      connections[Pos(x, 0)]!!.add(it)
    }
    end = Pos(x, height).also {
      connections.getOrPut(it) { mutableListOf() }.add(Pos(x, height - 1))
      connections[Pos(x, height - 1)]!!.add(it)
    }
  }
  return Maze(cells = connections, start = start, end = end)
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
  val symbols = listOf("  ", "╷ ", "╵ ", "│ ", "╶─", "┌─", "└─", "├─", "╴ ", "┐ ", "┘ ", "┤ ", "──", "┬─", "┴─", "┼─").withIndex()
    .associate { (index, value) ->
      LineSpec(index and 0b1000 != 0, index and 0b0100 != 0, index and 0b0010 != 0, index and 0b0001 != 0) to value
    }

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
  }.split("\n").joinToString(separator = "\n") { it.trim() }
}

fun Maze.renderBlocks(): String =
  buildString {
    val mazeCells = cells.filterKeys { it !== start && it !== end }
    val maxX      = mazeCells.keys.maxOf { it.x } + 1
    val maxY      = mazeCells.keys.maxOf { it.y } + 1
    val block     = "██"
    val space     = "  "
    (0..maxY).forEach { y ->
      (0..maxX).forEach { x ->
        val connections    = cells[Pos(x, y)] ?: emptyList()
        val connectedToTop = connections.any { it.y == y - 1 } || (x == maxX)
        append(block).append(if (connectedToTop) space else block)
      }
      appendLine()
      (0..maxX).forEach { x ->
        val connections     = cells[Pos(x, y)] ?: emptyList()
        val connectedToLeft = connections.any { it.x == x - 1 } || (y == maxY)
        append(if (connectedToLeft) space else block).append(space)
      }
      appendLine()
    }
  }.split("\n").dropLast(2).joinToString(separator = "\n") { it.dropLast(2) }


fun parseMaze(rendered: String): Maze {
  val lines  = rendered.lines().map { line -> line.filterIndexed { index, _ -> index % 2 == 0 }  }
  val width  = (lines.first().length - 1) / 2
  val height = (lines.size - 1) / 2
  val reachablePositions: Set<Pos> = lines.flatMapIndexed { y: Int, line: String ->
    line.mapIndexedNotNull { x, char ->
      if (char == ' ') Pos(x, y) else null
    }
  }.toSet()
  val deltas = listOf(Pos(0, -1), Pos(1, 0), Pos(0, 1), Pos(-1, 0))
  infix operator fun Pos.plus(other: Pos) = Pos(x + other.x, y + other.y)
  val cells = mutableMapOf<Pos, List<Pos>>()
  (0 until height).forEach { y ->
    (0 until width).forEach { x ->
      val pos = Pos(x * 2 + 1, y * 2 + 1)
      val reachableNeighbours = deltas.withIndex().filter { (_, delta) -> (pos + delta) in reachablePositions }.map { (index, _) -> Pos(x, y) + deltas[index] }
      cells[Pos(x, y)] = reachableNeighbours
    }
  }
  val targets = cells.values.flatten()
  val start = targets.firstOrNull { it.y == -1     } ?: targets.first { it.x == -1 }
  val end   = targets.firstOrNull { it.y == height } ?: targets.first { it.x == width }
  return Maze(cells + (start to listOf(cells.entries.first { (_, value) -> start in value }.key)) +
                      (end   to listOf(cells.entries.first { (_, value) -> end   in value }.key)), start, end)
}


fun main() {
  val sizes = listOf(10, 20, 100, 200)
  sizes.forEach { size ->
    println("--------- size = $size ---------")
    val generateStart = System.nanoTime()
    val maze = generateMaze(size, size)
    val generationDuration = Duration.ofNanos(System.nanoTime() - generateStart)
    println("Generated maze in $generationDuration")

    val renderBlocksStart = System.nanoTime()
    val blocks = maze.renderBlocks()
    val renderBlocksDuration = Duration.ofNanos(System.nanoTime() - renderBlocksStart)
    println("Renderer blocks in $renderBlocksDuration")

    val parseStart = System.nanoTime()
    val parsedMaze = parseMaze(blocks)
    val parseDuration = Duration.ofNanos(System.nanoTime() - parseStart)
    println("Parsed maze in $parseDuration")

    val renderStart = System.nanoTime()
    val rendered = parsedMaze.render()
    //println(rendered)
    val renderDuration = Duration.ofNanos(System.nanoTime() - renderStart)
    println("Rendered maze in $renderDuration")
    println()
  }

  /*
  for (y in 0..9) {
    for (x in 0..9) {
      print(parsedMaze.cells[Pos(x, y)]?.let { "$x/$y -> ${it.map { c -> "${c.x}/${c.y}" } } ".padEnd(24)})
    }
    println()
  }
  println("Start: ${parsedMaze.start} -> ${parsedMaze.cells[parsedMaze.start]}")
  println("End:   ${parsedMaze.end} -> ${parsedMaze.cells[parsedMaze.end]}")
  */
}

