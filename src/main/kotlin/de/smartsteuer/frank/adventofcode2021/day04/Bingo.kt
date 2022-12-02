package de.smartsteuer.frank.adventofcode2021.day04

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val boardsAndNumbers = lines("/adventofcode2021/day04/boards-and-numbers.txt")
  val numbers = boardsAndNumbers[0].splitToSequence(',')
                                   .map { it.toInt() }
                                   .toList()
  val boards  = boardsAndNumbers.drop(1)
                                .chunked(6)
                                .map { it.drop(1) }
                                .map { parseBoard(it) }
  val winningBoardsAndFinalNumbers           = findAllWinningBoards(boards, emptyList(), numbers, 0)
  val firstWinningBoardAndFinalNumber        = winningBoardsAndFinalNumbers.first()
  val sumOfUnmarkedCellsForFirstWinningBoard = firstWinningBoardAndFinalNumber.board.computeSumOfUnmarkedCells()
  val scoreOfFirstWinningBoard               = sumOfUnmarkedCellsForFirstWinningBoard * firstWinningBoardAndFinalNumber.number
  val lastWinningBoardAndFinalNumber         = winningBoardsAndFinalNumbers.last()
  val sumOfUnmarkedCellsForLastWinningBoard  = lastWinningBoardAndFinalNumber.board.computeSumOfUnmarkedCells()
  val scoreOfLastWinningBoard                = sumOfUnmarkedCellsForLastWinningBoard * lastWinningBoardAndFinalNumber.number
  println("first winning board:")
  println(firstWinningBoardAndFinalNumber.board)
  println("final number       = ${firstWinningBoardAndFinalNumber.number}")
  println("sumOfUnmarkedCells = $sumOfUnmarkedCellsForFirstWinningBoard")
  println("score              = $scoreOfFirstWinningBoard")

  println("last winning board:")
  println(lastWinningBoardAndFinalNumber.board)
  println("final number       = ${lastWinningBoardAndFinalNumber.number}")
  println("sumOfUnmarkedCells = $sumOfUnmarkedCellsForLastWinningBoard")
  println("score              = $scoreOfLastWinningBoard")
}


internal data class BoardAndFinalNumber(val board: Board, val number: Int)

internal tailrec fun findAllWinningBoards(boards:        List<Board>,
                                          winningBoards: List<BoardAndFinalNumber>,
                                          numbers:       List<Int>,
                                          numberIndex:   Int): List<BoardAndFinalNumber> {
  if (numberIndex >= numbers.size) {
    return winningBoards
  }
  val newWinningBoards = (boards.filter { it.isBingo() })
  val remainingBoards  = boards.filter { it !in newWinningBoards }
  val winningBoardsAndFinalNumbers = winningBoards + newWinningBoards.map { BoardAndFinalNumber(it, numbers[numberIndex - 1]) }
  return findAllWinningBoards(remainingBoards.map { it.mark(numbers[numberIndex]) }, winningBoardsAndFinalNumbers, numbers, numberIndex + 1)
}


internal data class Cell(val number: Int, val marked: Boolean)

internal class Board private constructor(private val cells:        List<List<Cell>>,
                                         private val columnCounts: List<Int>) {
  constructor(numbers: List<List<Int>>): this(numbers.map { row -> row.map { Cell(it, false) }},
                                              List(numbers.first().size) { 0 })
  fun mark(number: Int): Board {
    cells.forEachIndexed { rowIndex, rows ->
      rows.forEachIndexed { columnIndex, cell ->
        if (cell.number == number) {
          return Board(cells.mark(columnIndex, rowIndex), columnCounts.increment(columnIndex))
        }
      }
    }
    return this
  }

  fun isBingo(): Boolean {
    val anyRowMarkedAll    = cells.any { row -> row.all { it.marked } }
    val anyColumnMarkedAll = columnCounts.any { it == 5 }
    return anyRowMarkedAll || anyColumnMarkedAll
  }

  fun computeSumOfUnmarkedCells() = cells.sumOf { row -> row.filter { !it.marked }.sumOf { it.number } }

  override fun toString(): String = buildString {
    cells.forEach { row ->
      appendLine(row.joinToString(" ") { it.number.toString().padStart(2) })
    }
  }

  private fun List<List<Cell>>.mark(columnIndex: Int, rowIndex: Int): List<List<Cell>> =
    this.mapIndexed { r, row -> if (r == rowIndex) row.mark(columnIndex)  else row }

  private fun List<Cell>.mark(columnIndex: Int): List<Cell> =
    this.mapIndexed { c, cell -> if (c == columnIndex) Cell(cell.number, true) else cell }

  private fun List<Int>.increment(columnIndex: Int): List<Int> = mapIndexed { c, counter -> if (c == columnIndex) counter + 1 else counter }
}


internal fun parseBoard(boardText: List<String>): Board =
  Board(boardText.map { it.trim()
                          .splitToSequence("""\s+""".toRegex())
                          .map { number -> number.toInt() }
                          .toList() })
