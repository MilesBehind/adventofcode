package de.smartsteuer.frank.adventofcode2024.day15

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  WarehouseWoes.execute(lines("/adventofcode2024/day15/warehouse.txt"))
}

object WarehouseWoes : Day {

  override fun part1(input: List<String>): Long =
    input.parseWarehouse().moveRobot().computeGpsCoordinates().sum().toLong()

  override fun part2(input: List<String>): Long =
    input.parseWarehouse().double().moveRobot().computeGpsCoordinates().sum().toLong()

  data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    fun gpsCoordinate() = 100 * y + x
  }

  data class Robot(val pos: Pos, val movements: List<Pos>)

  data class Warehouse(val walls: Set<Pos>, val boxes: Set<Pos>, val width: Int, val height: Int, val doubled: Boolean, val robot: Robot) {
    fun moveRobot(): Warehouse {
      tailrec fun moveRobot(boxes: MutableSet<Pos> = this.boxes.toMutableSet(), pos: Pos = robot.pos, movements: List<Pos> = robot.movements): Warehouse {
        if (movements.isEmpty()) return Warehouse(walls, boxes, width, height, doubled, Robot(pos, movements))
        val movement = movements.first()
        val newPos = pos + movement
        if (newPos in walls) return moveRobot(boxes, pos, movements.drop(1))
        if (boxes.boxAt(newPos) != null) {
          val (newBoxes, movedPos) = moveBoxes(boxes, pos, movement)
          return moveRobot(newBoxes, movedPos, movements.drop(1))
        }
        return moveRobot(boxes, newPos, movements.drop(1))
      }
      return moveRobot()
    }

    internal fun findBoxesUntilEmptySpace(boxes: Set<Pos>, pos: Pos, direction: Pos): Set<Pos> {
      tailrec fun findBoxesUntilEmptySpace(positions: Set<Pos>, result: MutableSet<Pos>): Set<Pos> {
        if (positions.any { it in walls }) return emptySet()
        val affectedBoxes = positions.mapNotNull { boxes.boxAt(it) }.toSet()
        if (affectedBoxes.isEmpty()) return result
        val newPositions = if (direction.y != 0 && doubled) affectedBoxes.flatMap { box -> listOf(box, box.copy(x = box.x + 1)) }.map { it + direction }.toSet()
                           else positions.map { it + direction }.toSet()
        return findBoxesUntilEmptySpace(newPositions, result.apply { addAll(affectedBoxes) })
      }
      return findBoxesUntilEmptySpace(setOf(pos + direction), mutableSetOf())
    }

    private fun moveBoxes(boxes: MutableSet<Pos>, pos: Pos, direction: Pos): Pair<MutableSet<Pos>, Pos> {
      val boxesUntilEmptySpace = findBoxesUntilEmptySpace(boxes, pos, direction)
      if (boxesUntilEmptySpace.isEmpty()) return boxes to pos
      return boxes.apply {
        removeAll(boxesUntilEmptySpace.toSet())
        addAll(boxesUntilEmptySpace.map { it + direction })
      } to pos + direction
    }

    private fun Set<Pos>.boxAt(pos: Pos): Pos? =
      if (!doubled) {
        if (pos in this) pos else null
      } else {
        if (pos in this) pos else pos.copy(x = pos.x - 1).let { leftBoxPos -> if (leftBoxPos in this) leftBoxPos else null }
      }

    fun computeGpsCoordinates() =
      boxes.map { it.gpsCoordinate() }

    fun double(): Warehouse =
      Warehouse(walls   = walls.flatMap { pos -> listOf(Pos(2 * pos.x, pos.y), Pos(2 * pos.x + 1, pos.y)) }.toSet(),
                boxes   = boxes.map { pos -> Pos(2 * pos.x, pos.y) }.toSet(),
                width   = width * 2,
                height  = height,
                doubled = true,
                robot   = robot.copy(pos = robot.pos.copy(x = 2 * robot.pos.x)))

    override fun toString(): String = buildString {
      (0 until height).forEach { y ->
        (0 until width).forEach { x ->
          val pos = Pos(x, y)
          when {
            pos in walls                                -> append('#')
            pos in boxes && !doubled                    -> append('O')
            pos in boxes && doubled                     -> append('[')
            pos.copy(x = pos.x - 1) in boxes && doubled -> append(']')
            pos == robot.pos                            -> append('@')
            else                                        -> append('.')
          }
        }
        appendLine()
      }
    }
  }

  fun List<String>.parseWarehouse(): Warehouse {
    val warehouseDefinition = this.takeWhile { it.isNotBlank() }
    val posToChar           = warehouseDefinition.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Pos(x, y) to char } }.toMap()
    val charsToMovement     = mapOf('^' to Pos(0, -1), 'v' to Pos(0, 1), '<' to Pos(-1, 0), '>' to Pos(1, 0))
    val movements           = this.dropWhile { it.isNotBlank() }.drop(1).joinToString("").mapNotNull { char -> charsToMovement[char] }
    return Warehouse(walls   = posToChar.filterValues { it == '#' }.keys,
                     boxes   = posToChar.filterValues { it == 'O' }.keys,
                     width   = warehouseDefinition.first().length,
                     height  = warehouseDefinition.size,
                     doubled = false,
                     robot   = Robot(posToChar.filterValues { it == '@' }.keys.first(), movements))
  }
}
