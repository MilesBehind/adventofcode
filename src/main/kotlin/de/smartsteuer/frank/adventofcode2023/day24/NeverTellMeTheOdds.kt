package de.smartsteuer.frank.adventofcode2023.day24

import de.smartsteuer.frank.adventofcode2023.lines
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.time.measureTime

fun main() {
  val hailstones = parseHailstones(lines("/adventofcode2023/day24/hailstones.txt"))
  measureTime { println("part 1: ${part1(hailstones, 200_000_000_000_000..400_000_000_000_000)}") }.also { println("part 1 took $it") }
}

internal fun part1(hailstones: List<Hailstone>, bounds: ClosedRange<Long> = 7L..24L): Int =
  hailstones
    .combinations()
    .associateWith { (hailstone1, hailstone2) -> computePathIntersectionPointXY(hailstone1, hailstone2) }
    .filterValues { it != null && it.x.toLong() in bounds && it.y.toLong() in bounds }
    .filter { (hailstones, xy) -> xy != null && computeTime(hailstones.first, hailstones.second, xy) >= BigDecimal.ZERO }
    .count()

internal data class Vec(val x: BigInteger, val y: BigInteger, val z: BigInteger)

internal data class Vec2d(val x: BigDecimal, val y: BigDecimal)

internal data class Hailstone(val pos: Vec, val velocity: Vec)

internal fun computePathIntersectionPointXY(hailstone1: Hailstone, hailstone2: Hailstone): Vec2d? {
  val (px1, py1) = hailstone1.pos
  val (vx1, vy1) = hailstone1.velocity
  val (px2, py2) = hailstone2.pos
  val (vx2, vy2) = hailstone2.velocity
  val c1 = vy1 * px1 - vx1 * py1
  val c2 = vy2 * px2 - vx2 * py2
  val d  = vy1 * -vx2 - vy2 * -vx1
  if (d == BigInteger.ZERO) return null
  val x = (c1 * -vx2 - c2 * -vx1).toBigDecimal(scale = 10) / d.toBigDecimal(scale = 10)
  val y = (c2 *  vy1 - c1 *  vy2).toBigDecimal(scale = 10) / d.toBigDecimal(scale = 10)
  return Vec2d(x, y)
}

internal fun computeTime(hailstone1: Hailstone, hailstone2: Hailstone, intersectionPoint: Vec2d): BigDecimal =
  listOf((intersectionPoint.x - hailstone1.pos.x.toBigDecimal()) * hailstone1.velocity.x.toBigDecimal(),
         (intersectionPoint.y - hailstone1.pos.y.toBigDecimal()) * hailstone1.velocity.y.toBigDecimal(),
         (intersectionPoint.x - hailstone2.pos.x.toBigDecimal()) * hailstone2.velocity.x.toBigDecimal(),
         (intersectionPoint.y - hailstone2.pos.y.toBigDecimal()) * hailstone2.velocity.y.toBigDecimal()).min()

internal fun <T> List<T>.combinations(): List<Pair<T, T>> =
  flatMapIndexed { index: Int, element1: T ->
    this.drop(index + 1).map { element2 -> element1 to element2 }
  }

internal fun parseHailstones(lines: List<String>): List<Hailstone> =
  lines.map { line ->
    val (x, y, z, dx, dy, dz) = line.split(',', '@').map { it.trim().toBigInteger() }
    Hailstone(Vec(x, y, z), Vec(dx, dy, dz))
  }

internal operator fun <T> List<T>.component6() = this[5]
