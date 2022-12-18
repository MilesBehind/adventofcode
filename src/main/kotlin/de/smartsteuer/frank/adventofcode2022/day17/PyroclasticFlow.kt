package de.smartsteuer.frank.adventofcode2022.day17

import de.smartsteuer.frank.adventofcode2022.cycle
import de.smartsteuer.frank.adventofcode2022.day17.Day17.part1
import de.smartsteuer.frank.adventofcode2022.day17.Day17.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day17/jet-pattern.txt").first()
  measureTimeMillis {
    println("day 17, part 1: ${part1(input)}")
  }.also { println(it) }
  measureTimeMillis {
    println("day 17, part 2: ${part2(input)}")
  }.also { println(it) }
}

object Day17 {
  fun part1(input: String): Long {
    return simulate(input, 2022)
  }

  fun part2(input: String): Long {
    return simulate(input, 1_000_000_000_000L)
  }

  private fun simulate(input: String, rocksToStop: Long): Long {
    val chamber           = Chamber()
    val rockShapeSequence = rockShapes.cycle().iterator()
    val rockShape         = rockShapeSequence.next()
    val rock              = chamber.start(rockShape)
    return simulate(chamber, rock, rockShapeSequence, generateJetPushs(input).iterator(), rocksToStop, emptyList())
  }

  private tailrec fun simulate(chamber:                 Chamber,
                               rock:                    Rock,
                               rockShapeSequence:       Iterator<RockShape>,
                               actions:                 Iterator<(Chamber, Rock) -> Rock>,
                               rocksToStop:             Long,
                               growthAfterStoppedRocks: List<Int>): Long {
    if (rocksToStop == 0L) return chamber.height().toLong()
    val pushedRock = actions.next()(chamber, rock)
    val fallenRock = chamber.fall(pushedRock)
    return if (fallenRock == pushedRock) {
      val chamberWithStoppedRock = chamber.stopRock(fallenRock)
      val nextRock               = chamberWithStoppedRock.start(rockShapeSequence.next())
      //println(chamberWithStoppedRock.render(nextRock))
      val newGrowthAfterStoppedRocks = growthAfterStoppedRocks + (chamberWithStoppedRock.height() - chamber.height())
      val (start, size) = newGrowthAfterStoppedRocks.findRepeatingPattern()
      if (size > 0) {
        println("found repeating pattern starting at $start with size $size")
        return computeHeight(growthAfterStoppedRocks, rocksToStop, start, size)
      }
      simulate(chamberWithStoppedRock, nextRock, rockShapeSequence, actions, rocksToStop - 1, newGrowthAfterStoppedRocks)
    } else {
      simulate(chamber, fallenRock, rockShapeSequence, actions, rocksToStop, growthAfterStoppedRocks)
    }
  }

  private fun computeHeight(growthAfterStoppedRocks: List<Int>, rocksToStop: Long, repeatStart: Int, repeatSize: Int): Long {
    val heightUntilRepeatedGrowth     = growthAfterStoppedRocks.take(repeatStart).sum().toLong()
    val repeatedGrowth                = growthAfterStoppedRocks.drop(repeatStart).take(repeatSize).sum().toLong()
    val targetRocksToStop             = rocksToStop + growthAfterStoppedRocks.size
    val targetRepeatCount             = (targetRocksToStop - repeatStart) / repeatSize
    val remainingRocksAfterRepeating  = ((targetRocksToStop - repeatStart) % repeatSize).toInt()
    val remainingGrowthAfterRepeating = growthAfterStoppedRocks.drop(repeatStart).take(remainingRocksAfterRepeating).sum()
    return heightUntilRepeatedGrowth + targetRepeatCount * repeatedGrowth + remainingGrowthAfterRepeating
  }

  data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)
  }

  @Suppress("MemberVisibilityCanBePrivate")
  data class RockShape(val cells: Set<Pos>) {
    val minX = cells.minOf { it.x }
    val maxX = cells.maxOf { it.x }
    val minY = cells.minOf { it.y }
    val maxY = cells.maxOf { it.y }
  }

  private val horizontalLineRock = RockShape(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0)))
  private val plusRock           = RockShape(setOf(Pos(1, 0), Pos(0, 1), Pos(1, 1), Pos(2, 1), Pos(1, 2)))
  private val lShapedRock        = RockShape(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(2, 1), Pos(2, 2)))
  private val verticalLineRock   = RockShape(setOf(Pos(0, 0), Pos(0, 1), Pos(0, 2), Pos(0, 3)))
  private val squareRock         = RockShape(setOf(Pos(0, 0), Pos(1, 0), Pos(0, 1), Pos(1, 1)))

  private val rockShapes = listOf(horizontalLineRock, plusRock, lShapedRock, verticalLineRock, squareRock)

  private const val CHAMBER_WIDTH        = 7
  private const val ROCK_DISTANCE_LEFT   = 2
  private const val ROCK_DISTANCE_BOTTOM = 3

  data class Rock(val shape: RockShape, val position: Pos = Pos(0, 0)) {
    fun minX()  = shape.minX + position.x
    fun maxX()  = shape.maxX + position.x
    fun minY()  = shape.minY + position.y
    fun maxY()  = shape.maxY + position.y
    fun cells() = shape.cells.map { cell -> (cell + position) }
    fun isCollision(occupiedCells: Set<Pos>, deltaX: Int, deltaY: Int) = shape.cells.any { cell -> (cell + position + Pos(deltaX, deltaY)) in occupiedCells }
  }

  data class Chamber(val stoppedRockCells: Set<Pos> = emptySet(), val maxYCell: Int = -1) {
    fun pushLeft(rock: Rock): Rock = when {
      rock.minX() == 0                          -> rock
      rock.isCollision(stoppedRockCells, -1, 0) -> rock
      else                                      -> rock.copy(position = rock.position.copy(x = rock.position.x - 1))
    }

    fun pushRight(rock: Rock): Rock = when {
      rock.maxX() >= CHAMBER_WIDTH - 1         -> rock
      rock.isCollision(stoppedRockCells, 1, 0) -> rock
      else                                     -> rock.copy(position = rock.position.copy(x = rock.position.x + 1))
    }

    fun fall(rock: Rock): Rock = when {
      rock.minY() == 0                          -> rock
      rock.isCollision(stoppedRockCells, 0, -1) -> rock
      else                                      -> rock.copy(position = rock.position.copy(y = rock.position.y - 1))
    }

    fun start(rockShape: RockShape): Rock = Rock(rockShape, Pos(rockShape.minX + ROCK_DISTANCE_LEFT, rockShape.minY + maxYCell + ROCK_DISTANCE_BOTTOM + 1))

    fun stopRock(rock: Rock): Chamber = Chamber(stoppedRockCells + rock.cells(), max(rock.maxY(), maxYCell))

    fun height() = maxYCell + 1

    fun render(rock: Rock) = buildString {
      (rock.maxY() downTo 0).forEach { y ->
        val movingRockRow  = rock.cells().filter { it.y == y }
        val stoppedRockRow = stoppedRockCells.filter { it.y == y }
        append('|')
        (0..6).forEach { x ->
          when (Pos(x, y)) {
            in movingRockRow  -> append("@")
            in stoppedRockRow -> append('#')
            else              -> append('.')
          }
        }
        appendLine('|')
      }
      appendLine("+-------+")
    }
  }

  private fun generateJetPushs(input: String): Sequence<(Chamber, Rock) -> Rock> {
    return input.toList().cycle().map { char ->
      when (char) {
        '<'  -> { chamber, rock -> chamber.pushLeft(rock) }
        '>'  -> { chamber, rock -> chamber.pushRight(rock) }
        else -> throw IllegalArgumentException("invalid jet symbol: '$char'")
      }
    }
  }

  fun List<Int>.findRepeatingPattern(): Pair<Int, Int> {
    (20..size / 2).forEach { patternSize ->
      if (subList(size - patternSize, size) == subList(size - 2 * patternSize, size - patternSize)) {
        val start = size - 2 * patternSize
        println("list of size $patternSize is repeated starting at $start")
        return start to patternSize
      }
    }
    return 0 to 0
  }
}
