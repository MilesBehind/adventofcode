package de.smartsteuer.frank.adventofcode2025.day01

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  SecretEntrance.execute(lines("/adventofcode2025/day01/rotations.txt"))
}

object SecretEntrance: Day<Int> {
  override fun part1(input: List<String>): Int =
    input.parseRotations().fold(listOf(Position(50))) { positions, rotation -> positions + (positions.last() + rotation) }.count { it.position == 0 }

  override fun part2(input: List<String>): Int =
    input.parseRotations().fold(Position(50) to 0) { (position, zeroCount), rotation -> (position + rotation) to (zeroCount + position.countZeros(rotation)) }.second
}



enum class Direction(val representation: String) {
  LEFT("L"), RIGHT("R");
  companion object {
    fun from(representation: String) = entries.first { it.representation == representation }
  }
  val delta get () = if (this == LEFT) -1 else 1
}

data class Rotation(val direction: Direction, val distance: Int)

data class Position(val position: Int) {
  companion object {
    private const val MODULUS = 100
  }
  fun normalize() = Position((position + 100 * MODULUS) % MODULUS)

  operator fun plus(rotation: Rotation) =
    Position(position + rotation.direction.delta * rotation.distance).normalize()

  fun countZeros(rotation: Rotation): Int =
    Position(0).let { zero ->
      (1..rotation.distance).count { this + rotation.copy(distance = it) == zero }
    }
}

fun List<String>.parseRotations(): List<Rotation> =
  """([LR])(\d+)""".toRegex().let { regex ->
    map { line ->
      val (direction, distance) = regex.matchEntire(line)?.destructured ?: error("could not parse line '$line'")
      Rotation(Direction.from(direction), distance.toInt())
    }
  }
