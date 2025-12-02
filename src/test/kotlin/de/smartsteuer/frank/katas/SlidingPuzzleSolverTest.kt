package de.smartsteuer.frank.katas

import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

class SlidingPuzzleSolverTest {
  private val puzzlesToSolutions = mapOf(
    listOf(
      listOf( 1, 2, 3, 4),
      listOf( 5, 0, 6, 8),
      listOf( 9,10, 7,11),
      listOf(13,14,15,12),
    ) to listOf(6, 7, 11, 12),

    listOf(
      listOf(4, 1, 3),
      listOf(2, 8, 0),
      listOf(7, 6, 5)
    ) to listOf(5, 6, 8, 2, 4, 1, 2, 5, 6),

    listOf(
      listOf(10,  3,  6,  4),
      listOf( 1,  5,  8,  0),
      listOf( 2, 13,  7, 15),
      listOf(14,  9, 12, 11)
    ) to listOf(8, 6, 3, 10, 1, 5, 10, 1, 5, 2, 13, 9, 14, 13, 9, 10, 2, 5, 1, 2, 6, 7, 12, 11, 15, 12, 11, 15),

    listOf(
      listOf( 3,  7, 14, 15, 10),
      listOf( 1,  0,  5,  9,  4),
      listOf(16,  2, 11, 12,  8),
      listOf(17,  6, 13, 18, 20),
      listOf(21, 22, 23, 19,  2)
    ) to listOf()
  )

  @Test
  fun `test solutions are correct`() {
    puzzlesToSolutions.forEach { (puzzle, solution) -> validateSlidingPuzzleSolution(puzzle, solution) }
  }

  @Test
  fun `solver returns expected solution`() {
    puzzlesToSolutions.keys.forEach { puzzle ->
      val solution = solve(puzzle)
      validateSlidingPuzzleSolution(puzzle, solution)
    }
  }
}

fun validateSlidingPuzzleSolution(start: List<List<Int>>, steps: List<Int>) {
  println("solution: $steps")
  val finalPuzzle = steps.fold(start.also { println("start:\n${it.render()}") }) { puzzle, tileToMove ->
    puzzle.applyMove(tileToMove).also { println("after moving $tileToMove:\n${it.render()}") }
  }
  finalPuzzle.isSolved().shouldBeTrue()
}