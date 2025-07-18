package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.ConnectFour.*
import de.smartsteuer.frank.katas.ConnectFour.Companion.COLUMN_COUNT
import de.smartsteuer.frank.katas.ConnectFour.Companion.CONNECT_COUNT
import de.smartsteuer.frank.katas.ConnectFour.Companion.ROW_COUNT
import de.smartsteuer.frank.katas.ConnectFourGame.Coordinate
import java.time.Duration
import kotlin.random.Random

@Suppress("unused")
interface ConnectFour {
  enum class Player { RED, YELLOW }
  data class Move(val player: Player, val column: Int)
  enum class Result { DRAW, RED_WON, YELLOW_WON }

  companion object {
    const val COLUMN_COUNT  = 7
    const val ROW_COUNT     = 6
    const val CONNECT_COUNT = 4
  }

  fun evaluate(moves: List<Move>): Result
}


class ConnectFourGame : ConnectFour {
  override fun evaluate(moves: List<Move>): Result =
    moves.fold(Board(emptyList())) { board, move -> board.execute(move) }.evaluate()

  data class Coordinate(val column: Int, val row: Int) {
    fun expandToGroup(deltaX: Int, deltaY: Int) =
      (0 until CONNECT_COUNT).map { Coordinate(column + deltaX * it, row + deltaY * it) }
  }

  data class Slot(val player: Player, val coordinate: Coordinate)

  data class Board(val slots: List<Slot>) {
    companion object {
      val allCoordinates = (1..COLUMN_COUNT).flatMap { column -> (1..ROW_COUNT).map { row -> Coordinate(column, row) } }.toSet()
      val groups         = expandGroups(1, 0) + expandGroups(0, 1) + expandGroups(1, 1) + expandGroups(-1, 1)

      fun expandGroups(deltaX: Int, deltaY: Int): List<List<Coordinate>> =
        allCoordinates.map { it.expandToGroup(deltaX, deltaY) }.filter { group -> group.all { it in allCoordinates } }
    }

    fun execute(move: Move): Board =
      Board(slots + Slot(move.player, Coordinate(move.column, nextRow(move.column))))

    fun nextRow(column: Int) =
      slots.filter { it.coordinate.column == column }.minOfOrNull { it.coordinate.row }?.minus(1) ?: ROW_COUNT

    fun playerSlots(player: Player): Set<Coordinate> =
      slots.filter { it.player == player }.map { it.coordinate }.toSet()

    fun evaluate(): Result {
      val start = System.nanoTime()
      val redPlayerSlots       = playerSlots(Player.RED)
      val yellowPlayerSlots    = playerSlots(Player.YELLOW)
      val redConnectedFours    = groups.filter { group -> group.all { it in redPlayerSlots    } }
      val yellowConnectedFours = groups.filter { group -> group.all { it in yellowPlayerSlots } }

      printEvaluationInfo(Duration.ofNanos(System.nanoTime() - start), redConnectedFours, yellowConnectedFours)

      return when {
        redConnectedFours.isNotEmpty() && yellowConnectedFours.isNotEmpty() -> Result.DRAW
        redConnectedFours.isNotEmpty()                                      -> Result.RED_WON
        yellowConnectedFours.isNotEmpty()                                   -> Result.YELLOW_WON
        else                                                                -> Result.DRAW
      }
    }

    fun printEvaluationInfo(duration: Duration, redConnectedFours: List<List<Coordinate>>, yellowConnectedFours: List<List<Coordinate>>) {
      val winningCoordinates = (redConnectedFours.flatten() + yellowConnectedFours.flatten()).toSet()
      println("evaluated board after $duration:")
      println(renderBoard(this, winningCoordinates))
      println("yellow wins: ${yellowConnectedFours.joinToString(separator = "\n             ") { it.joinToString { "${it.column}/${it.row}" } }}")
      println("red    wins: ${redConnectedFours.joinToString   (separator = "\n             ") { it.joinToString { "${it.column}/${it.row}" } }}")
    }
  }
}


fun renderBoard(board: ConnectFourGame.Board, winningCoordinates: Set<Coordinate>) = buildString {
  val ansiReset  = "\u001B[0m"
  val ansiRed    = "\u001B[31m"
  val ansiYellow = "\u001B[33m"

  (1 .. ROW_COUNT).forEach { row ->
    (1..COLUMN_COUNT).forEach { column ->
      val coordinate = Coordinate(column, row)
      val player = board.slots.find { it.coordinate == coordinate }?.player
      val symbol = if (coordinate in winningCoordinates) '\u25CF' else '\u25CE'
      val playerChar = when (player) {
        Player.RED    -> "$ansiRed$symbol $ansiReset"
        Player.YELLOW -> "$ansiYellow$symbol $ansiReset"
        null          -> "• "
      }
      append(playerChar)
    }
    appendLine()
  }
}


fun generateMoves(count: Int = COLUMN_COUNT * ROW_COUNT, starter: Player = Player.RED): List<Move> =
  (1..COLUMN_COUNT).flatMap { column ->
    (1..ROW_COUNT).map { row ->
      Coordinate(column, row)
    }
  }.shuffled()
   .take(count)
   .sortedByDescending { it.row }
   .mapIndexed { index, coordinate -> Move(Player.entries[(index + starter.ordinal) % 2], coordinate.column) }


fun main() {
  val results = mutableMapOf<Result, Int>()
  repeat(200_000) {
    val result = ConnectFourGame().evaluate(generateMoves(Random.nextInt(22, ROW_COUNT * COLUMN_COUNT), Player.RED))
    results[result] = results.getOrPut(result) { 0 } + 1
  }
  println(results)
}