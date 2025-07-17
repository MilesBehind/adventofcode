package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.ConnectFour.*
import de.smartsteuer.frank.katas.ConnectFour.Companion.COLUMN_COUNT
import de.smartsteuer.frank.katas.ConnectFour.Companion.ROW_COUNT
import java.time.Duration
import kotlin.math.abs
import kotlin.random.Random
import kotlin.time.measureTime

@Suppress("unused")
interface ConnectFour {
  enum class Player { RED, YELLOW }
  data class Move(val player: Player, val column: Int)
  enum class Result { DRAW, RED_WON, YELLOW_WON }

  companion object {
    const val COLUMN_COUNT = 7
    const val ROW_COUNT    = 6
  }

  fun evaluate(moves: List<Move>): Result
}


class ConnectFourGame : ConnectFour {
  override fun evaluate(moves: List<Move>): Result =
    moves.fold(Board(emptyList())) { board, move -> board.execute(move) }.evaluate()

  data class Slot(val player: Player, val coordinate: Coordinate)

  data class Board(val slots: List<Slot>) {
    fun execute(move: Move): Board = Board(slots + Slot(move.player, Coordinate(move.column, nextRow(move.column))))

    fun nextRow(column: Int) = slots.filter { it.coordinate.column == column }.minOfOrNull { it.coordinate.row }?.minus(1) ?: 6

    fun evaluate(): Result {
      val start = System.nanoTime()
      val connectedFours = candidates().mapValues { (_, groups) ->
        groups.filter { group -> group.connected() }
      }
      val duration = Duration.ofNanos(System.nanoTime() - start)
      val winningCoordinates = connectedFours.values.flatten().flatten().toSet()
      println("evaluated board after $duration:")
      println(this.toString(winningCoordinates))
      val redWins    = connectedFours[Player.RED]    ?: emptyList()
      val yellowWins = connectedFours[Player.YELLOW] ?: emptyList()
      println("red    wins: ${redWins.joinToString   (separator = "\n             ")}")
      println("yellow wins: ${yellowWins.joinToString(separator = "\n             ")}")
      return when {
        redWins.isNotEmpty() && yellowWins.isNotEmpty() -> Result.DRAW
        redWins.isNotEmpty()                            -> Result.RED_WON
        yellowWins.isNotEmpty()                         -> Result.YELLOW_WON
        else                                            -> Result.DRAW
      }
    }

    fun List<Coordinate>.connected(): Boolean =
      this.zipWithNext().all { (first, second) -> abs(first.row - second.row) == 1 || abs(first.column - second.column) == 1 }

    fun rows(): List<List<Slot>> =
      (1..COLUMN_COUNT).map { column -> slots.filter { it.coordinate.column == column } }

    fun columns(): List<List<Slot>> =
      (1..ROW_COUNT).map { row -> slots.filter { it.coordinate.row == row } }

    fun diagonalsRight(): List<List<Slot>> =
      (-COLUMN_COUNT..COLUMN_COUNT).map { diff -> slots.filter { it.coordinate.column - it.coordinate.row == diff } }

    fun diagonalsLeft(): List<List<Slot>> =
      (-COLUMN_COUNT..COLUMN_COUNT).map { diff -> slots.filter { COLUMN_COUNT - it.coordinate.column - it.coordinate.row == diff } }

    fun groups(): List<List<Slot>> =
      rows() + columns() + diagonalsRight() + diagonalsLeft()

    fun playersToGroups(): Map<Player, List<List<Coordinate>>> =
      Player.entries.associateWith { player ->
        groups().map { group -> group.filter { it.player == player }.map { it.coordinate } }
      }

    fun candidates(): Map<Player, List<List<Coordinate>>> =
      playersToGroups().mapValues { groups -> groups.value.flatMap { group -> group.windowed(4) } }

    fun toString(winningCoordinates: Set<Coordinate>) = buildString {
      (1 .. ROW_COUNT).forEach { row ->
        (1..COLUMN_COUNT).forEach { column ->
          val coordinate = Coordinate(column, row)
          val player = slots.find { it.coordinate == coordinate }?.player
          val symbol = if (coordinate in winningCoordinates) '\u25CF' else '\u25CE'
          val playerChar = when (player) {
            Player.RED    -> "$ANSI_RED$symbol $ANSI_RESET"
            Player.YELLOW -> "$ANSI_YELLOW$symbol $ANSI_RESET"
            null          -> "• "
          }
          append(playerChar)
        }
        appendLine()
      }
    }
  }

  data class Coordinate(val column: Int, val row: Int)

  companion object {
    const val ANSI_RESET  = "\u001B[0m"
    const val ANSI_RED    = "\u001B[31m"
    const val ANSI_YELLOW = "\u001B[33m"
  }
}


fun generateMoves(count: Int = COLUMN_COUNT * ROW_COUNT): List<Move> =
  (1..COLUMN_COUNT).flatMap { column ->
    (1..ROW_COUNT).map { row ->
      ConnectFourGame.Coordinate(column, row)
    }
  }.shuffled()
   .take(count)
   .sortedByDescending { it.row }
   .mapIndexed { index, coordinate -> Move(Player.entries[index % 2], coordinate.column) }


fun main() {
  println(ConnectFourGame().evaluate(generateMoves(Random.nextInt(20, ROW_COUNT * COLUMN_COUNT))))
}