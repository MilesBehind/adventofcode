package de.smartsteuer.frank.adventofcode2022.day02

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day02/rock-paper-scissors.txt")
  // simple solution:
  println("part 1: score = ${part1Lookup(input)}")
  println("part 2: score = ${part2Lookup(input)}")
  // model based solution (more code, mire complicated, better abstraction):
  println("part 1: score = ${part1(input)}")
  println("part 2: score = ${part2(input)}")
}

//region code variant

enum class Outcome(val score: Int) {
  LOSE(0), DRAW(3), WIN(6);

  companion object {
    fun parse(code: Char): Outcome = when (code) {
      'X' -> LOSE
      'Y' -> DRAW
      'Z' -> WIN
      else -> throw IllegalArgumentException("invalid code: '$code'")
    }
  }
}

enum class Item(val score: Int) {
  ROCK(1), PAPER(2), SCISSORS(3);

  companion object {
    fun parse(code: Char): Item = when (code) {
      'A', 'X' -> ROCK
      'B', 'Y' -> PAPER
      'C', 'Z' -> SCISSORS
      else -> throw IllegalArgumentException("invalid code: '$code'")
    }
  }

  fun outcome(other: Item): Outcome = when {
    (this == ROCK && other == SCISSORS) || (this == SCISSORS && other == PAPER) || (this == PAPER && other == ROCK) -> Outcome.WIN
    this == other                                                                                                   -> Outcome.DRAW
    else                                                                                                            -> Outcome.LOSE
  }

  fun chooseItemForOutcome(outcome: Outcome): Item = when {
    (outcome == Outcome.WIN && this == SCISSORS) || (outcome == Outcome.DRAW && this == ROCK)     || (outcome == Outcome.LOSE && this == PAPER)    -> ROCK
    (outcome == Outcome.WIN && this == ROCK)     || (outcome == Outcome.DRAW && this == PAPER)    || (outcome == Outcome.LOSE && this == SCISSORS) -> PAPER
    (outcome == Outcome.WIN && this == PAPER)    || (outcome == Outcome.DRAW && this == SCISSORS) || (outcome == Outcome.LOSE && this == ROCK)     -> SCISSORS
    else -> throw IllegalArgumentException("invalid case, item = $this and outcome should be $outcome")
  }
}

fun part1(input: List<String>): Int = input.sumOf { line ->
  val (other, me) = line.split(" ").map { Item.parse(it.first()) }
  me.score + me.outcome(other).score
}

fun part2(input: List<String>): Int = input.sumOf { line ->
  val (otherCode, outcomeCode) = line.split(" ").map { it.first() }
  val other   = Item.parse(otherCode)
  val outcome = Outcome.parse(outcomeCode)
  val me      = other.chooseItemForOutcome(outcome)
  me.score + outcome.score
}

//endregion

//region lookup

const val ROCK_SCORE     = 1
const val PAPER_SCORE    = 2
const val SCISSORS_SCORE = 3

const val LOSE_SCORE = 0
const val DRAW_SCORE = 3
const val WIN_SCORE  = 6

// A = Rock, B = Paper, C = Scissors
// X = Rock, Y = Paper, Z = Scissors
// Rock > Scissors, Scissors > Paper, Paper > Rock

val part1InputToScore = mapOf(
  "A X" to ROCK_SCORE     + DRAW_SCORE,  // opponent: Rock     = me: Rock     => Draw
  "A Y" to PAPER_SCORE    + WIN_SCORE,   // opponent: Rock     < me: Paper    => Win
  "A Z" to SCISSORS_SCORE + LOSE_SCORE,  // opponent: Rock     > me: Scissors => Lose
  "B X" to ROCK_SCORE     + LOSE_SCORE,  // opponent: Paper    > me: Rock     => Lose
  "B Y" to PAPER_SCORE    + DRAW_SCORE,  // opponent: Paper    = me: Paper    => Draw
  "B Z" to SCISSORS_SCORE + WIN_SCORE,   // opponent: Paper    < me: Scissors => Win
  "C X" to ROCK_SCORE     + WIN_SCORE,   // opponent: Scissors < me: Rock     => Win
  "C Y" to PAPER_SCORE    + LOSE_SCORE,  // opponent: Scissors > me: Paper    => Lose
  "C Z" to SCISSORS_SCORE + DRAW_SCORE,  // opponent: Scissors = me: Scissors => Draw
)

val part2InputToScore = mapOf(
  "A X" to SCISSORS_SCORE + LOSE_SCORE,  // opponent: Rock     > me: Scissors => Lose
  "A Y" to ROCK_SCORE     + DRAW_SCORE,  // opponent: Rock     = me: Rock     => Draw
  "A Z" to PAPER_SCORE    + WIN_SCORE,   // opponent: Rock     < me: paper    => Win
  "B X" to ROCK_SCORE     + LOSE_SCORE,  // opponent: Paper    > me: Rock     => Lose
  "B Y" to PAPER_SCORE    + DRAW_SCORE,  // opponent: Paper    = me: Paper    => Draw
  "B Z" to SCISSORS_SCORE + WIN_SCORE,   // opponent: Paper    < me: Scissors => Win
  "C X" to PAPER_SCORE    + LOSE_SCORE,  // opponent: Scissors > me: paper    => Lose
  "C Y" to SCISSORS_SCORE + DRAW_SCORE,  // opponent: Scissors = me: Scissor  => Draw
  "C Z" to ROCK_SCORE     + WIN_SCORE,   // opponent: Scissors < me: Rock     => Win
)

fun part1Lookup(input: List<String>): Int = input.sumOf { part1InputToScore[it] ?: 0 }

fun part2Lookup(input: List<String>): Int = input.sumOf { part2InputToScore[it] ?: 0 }

//endregion