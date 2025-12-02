package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.PuzzleState.Position
import kotlin.math.abs

data class PuzzleState(val board:       List<List<Int>>,
                       val zeroPos:     Position,
                       val targetBoard: List<List<Int>>): Node<PuzzleState> {

  data class Position(val row: Int, val col: Int)

  override val heuristic: Double get() {
    var distance = 0
    for (row in board.indices) {
      for (col in board[row].indices) {
        val value = board[row][col]
        if (value != 0) {
          val targetRow = (value - 1) / board[row].size
          val targetCol = (value - 1) % board[row].size
          distance += abs(row - targetRow) + abs(col - targetCol)
        }
      }
    }
    return distance.toDouble()
  }

  override val isGoal: Boolean get() = board == targetBoard

  override val neighbors: List<Pair<PuzzleState, Double>> get() {
    val neighbors = mutableListOf<Pair<PuzzleState, Double>>()
    val (row, col) = zeroPos
    val directions = listOf(Position(-1, 0), Position(1, 0), Position(0, -1), Position(0, 1))

    for ((dRow, dCol) in directions) {
      val newRow = row + dRow
      val newCol = col + dCol

      if (newRow in board.indices && newCol in board[0].indices) {
        val newBoard = board.map { it.toMutableList() }
        newBoard[row][col] = newBoard[newRow][newCol]
        newBoard[newRow][newCol] = 0

        neighbors += PuzzleState(newBoard, Position(newRow, newCol), targetBoard) to 1.0
      }
    }
    return neighbors
  }

  override val key: Any get() = board
}

fun List<List<Int>>.findTile(tile: Int): Position? {
  forEachIndexed { row, rowTiles ->
    rowTiles.forEachIndexed { col, value ->
      if (value == tile) return Position(row, col)
    }
  }
  return null
}


fun List<List<Int>>.applyMove(tileToMove: Int): List<List<Int>> =
  map { row ->
    row.map { tile ->
      when (tile) {
        0          -> tileToMove
        tileToMove -> 0
        else       -> tile
      }
    }
  }

fun List<List<Int>>.toTarget(): List<List<Int>> {
  val height = size
  val width  = first().size
  return List(height) { y ->
    List(width) { x ->
      if (x == width - 1 && y == height - 1) 0 else y * width + x + 1
    }
  }
}

fun List<List<Int>>.isSolved() =
  this == toTarget()

fun solve(puzzle: List<List<Int>>): List<Int> {
  fun findMovedTile(from: List<List<Int>>, to: List<List<Int>>): Int {
    from.forEachIndexed { row, rowTiles ->
      rowTiles.forEachIndexed { col, value ->
        if (value != 0 && to[row][col] == 0) return value
      }
    }
    return -1
  }

  val solver = AStar<PuzzleState>()
  val zeroPos = puzzle.findTile(0) ?: return emptyList()
  val target = puzzle.toTarget()
  val result = solver.search(PuzzleState(puzzle, zeroPos, target)) ?: return emptyList()
  return result.path.windowed(2, 1).map { (from, to) -> findMovedTile(from.board, to.board) }
}

fun List<List<Int>>.render(): String =
  buildString {
    (0 until size).forEach { y ->
      (0 until this@render.first().size).forEach { x ->
        val tile = this@render[y][x]
        append((if (tile == 0) "" else tile.toString()).padStart(2))
        append(" ")
      }
      appendLine()
    }
  }


// Verwendung
fun main() {
  val start = listOf(
    listOf( 1,  2,  3,  4),
    listOf( 5,  0,  6,  8),
    listOf( 9, 10,  7, 11),
    listOf(13, 14, 15, 12)
  )

  val solution = solve(start)
  println("Schritte: $solution")
}

//---------------
//---------------
//---------------

data class Puzzle2(val tiles: List<List<Int>>) {
  data class Position(val x: Int, val y: Int) {
    private val relativeNeighbours = listOf(Position(-1, 0), Position(1, 0), Position(0, -1), Position(0, 1))
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    fun neighbours() = relativeNeighbours.map { this + it }
  }

  fun solve(): List<Int> {
    tailrec fun solve(tiles: List<List<Int>>, position: Position, solved: Set<Position>, solution: List<Int>): List<Int> {
      val (newTiles, movesForPosition) = solvePosition(tiles, position, solved)
      val nextPosition = findNextPosition(position) ?: return solution
      return solve(newTiles, nextPosition, solved + position, solution + movesForPosition)
    }
    return solve(tiles, Position(0, 0), emptySet(), emptyList())
  }

  val width:  Int get() = tiles.first().size
  val height: Int get() = tiles.size

  fun findNextPosition(position: Position): Position? {
    // 0,0, 1,0, 2,0, 3,0, 4,0    y <= x    0   1  2  3  4
    // 0,1, 0,2, 0,3, 0,4         x < y     5   9 10 11 12
    // 1,1, 2,1, 3,1, 4,1         y <= x    6  13 16 17 18
    // 1,2, 1,3, 1,4              x < y     7  14 19 21 22
    // 2,2, 3,2, 4,2              y <= x    8  15 20 23 24
    // 2,4, 2,4                   x < y
    // 3,3, 4,3                   y <= x
    // 3,4                        x < y
    // 4,4                        y <= x
    return if (position.y <= position.x) {
      if (position.x == width - 1) Position(position.y, position.y + 1) else Position(position.x + 1, position.y)
    } else {
      if (position.y == height - 1) Position(position.x + 1, position.x + 1) else Position(position.x, position.y + 1)
    }

  }

  fun solvePosition(tiles: List<List<Int>>, position: Position, solved: Set<Position>): Pair<List<List<Int>>, List<Int>> {
    val tileToMove = position.y * width + position.x + 1
    val positionToMove = findPosition(tileToMove)
    // swap position and positionToMove without moving solved positions
    val currentTiles = tiles.map { it.toMutableList() }.toMutableList()
    TODO()
  }

  fun findPosition(tile: Int): Position {
    tiles.forEachIndexed { y, row ->
      row.forEachIndexed { x, t ->
        if (t == tile) return Position(x, y)
      }
    }
    throw IllegalArgumentException("tile $tile not found")
  }

  fun applyMove(tileToMove: Int): Puzzle2 =
    copy(tiles = tiles.map { row ->
      row.map { tile ->
        when (tile) {
          0          -> tileToMove
          tileToMove -> 0
          else       -> tile
        }
      }
    })

  fun render(): String =
    buildString {
      val width = tiles.first().size
      val height = tiles.size
      (0 until height).forEach { y ->
        (0 until width).forEach { x ->
          val tile = tiles[y][x]
          append((if (tile == 0) "" else tile.toString()).padStart(2))
          append(" ")
        }
        appendLine()
      }
    }
}


/*
import de.smartsteuer.frank.katas.SlidingPuzzle.Companion.Position

typealias Tile = Int

data class SlidingPuzzle(val tiles: Map<Position, Tile>, val width: Int, val height: Int, val emptyPosition: Position) {
  companion object {
    const val EMPTY_POSITION = 0

    data class Position(val x: Int, val y: Int) {
      companion object {
        val relativeNeighbours = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
      }
      operator fun plus(other: Pair<Int, Int>) = Position(x + other.first, y + other.second)
      val neighbours: List<Position> get() = relativeNeighbours.map { this + it }
    }

    data class Move(val from: Position, val to: Position)

    fun findPosition(tiles: List<List<Tile>>, tile: Tile): Position {
      tiles.forEachIndexed { y, row ->
        row.forEachIndexed { x, t ->
          if (t == tile) return Position(x, y)
        }
      }
      throw IllegalArgumentException("tile $tile not found")
    }
  }

  constructor(tiles: List<List<Tile>>):
          this(tiles         = tiles.flatMapIndexed { y, row ->
                                 row.mapIndexed { x, tile ->
                                   Position(x, y) to tile
                                 }
                               }.toMap(),
               width         = tiles.first().size,
               height        = tiles.size,
               emptyPosition = findPosition(tiles, EMPTY_POSITION))

  fun applyMove(from: Position): SlidingPuzzle =
    applyMove(tiles.getValue(from))

  fun applyMove(tileToMove: Tile): SlidingPuzzle {
    var newEmptyPosition = emptyPosition
    val newTiles = tiles.mapValues { (position, tile) ->
      when (tile) {
        EMPTY_POSITION -> tileToMove
        tileToMove     -> EMPTY_POSITION.also { newEmptyPosition = position }
        else           -> tile
      }
    }
    return copy(tiles = newTiles, emptyPosition = newEmptyPosition)
  }

  fun isSolved(): Boolean {
    (0 until height).forEach { y ->
      (0 until width).forEach { x ->
        val tile = tiles[Position(x, y)]
        if (tile != EMPTY_POSITION && tile != y * width + x + 1) return false
      }
    }
    return true
  }
}



fun solve(puzzle: SlidingPuzzle): List<Int> {
  tailrec fun solve(puzzles: MutableList<Pair<SlidingPuzzle, List<Tile>>>, visited: MutableSet<SlidingPuzzle>): List<Tile> {
    val (puzzle, solution) = puzzles.last()
    println("${puzzles.size} puzzles to process, ${visited.size} puzzles visited:\n${puzzle.render()}")
    if (puzzle.isSolved()) return solution
    if (solution.size > 2 * puzzle.width * puzzle.height) return solve(puzzles.also { it.removeLast() }, visited)
    val nextFromPositions = puzzle.emptyPosition.neighbours.filter { position -> position in puzzle.tiles }
    val nextPuzzles = nextFromPositions.mapNotNull { from ->
      val nextPuzzle = puzzle.applyMove(from)
      if (nextPuzzle in visited) return@mapNotNull null else nextPuzzle to (solution + puzzle.tiles.getValue(from))
    }.filter { it.first !in visited }
    //nextPuzzles.forEach { (puzzle, solution) -> println("depth: ${solution.size}\n${puzzle.render()}solution: ${solution.joinToString(" ")}\n") }
    val newPuzzles = puzzles.also { it.removeLast(); it.addAll(nextPuzzles) }
    val newVisited = visited.also { it.add(puzzle) }
    return solve(newPuzzles, newVisited)
  }
  return solve(mutableListOf(puzzle to emptyList()), mutableSetOf())
}

fun SlidingPuzzle.render(): String =
  buildString {
    (0 until height).forEach { y ->
      (0 until width).forEach { x ->
        append((tiles[Position(x, y)]?.toString() ?: "?").padStart(2))
        append(" ")
      }
      appendLine()
    }
  }
*/