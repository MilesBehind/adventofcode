package de.smartsteuer.frank.adventofcode2021.day17

import de.smartsteuer.frank.adventofcode2021.day17.TargetArea.Companion.toTargetArea
import de.smartsteuer.frank.adventofcode2021.lines
import kotlin.math.sqrt

fun main() {
  val input = lines("/adventofcode2021/day17/target-area.txt").first()
  val targetArea = input.toTargetArea()
  println("targetArea = $targetArea")

  val maximumSpeedAfterPassingZero = (-targetArea.y1 -1)
  val heightWhenUsingThisSpeed = maximumSpeedAfterPassingZero * (maximumSpeedAfterPassingZero + 1) / 2
  println("maximum height = $heightWhenUsingThisSpeed")

  val velocitiesHittingTarget = findAllHittingVelocities(targetArea)
  println("velocitiesHittingTarget = $velocitiesHittingTarget")
  println("velocitiesHittingTarget = ${velocitiesHittingTarget.size}")
}

data class TargetArea(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
  companion object {
    fun String.toTargetArea(): TargetArea {
      val ranges = ("""target area: x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)"""
        .toRegex().matchEntire(this)?.destructured?.toList() ?: emptyList())
        .map { it.toInt() }
      return TargetArea(x1 = ranges[0], x2 = ranges[1], y1 = ranges[2], y2 = ranges[3])
    }
  }

  operator fun contains(position: Pair<Int, Int>): Boolean = position.first in (x1..x2) && position.second in (y1..y2)

  fun targetHitPosition(velocityX: Int, velocityY: Int): Pair<Int, Int>? {
    val seqX = generateSequence(0 to velocityX) { (posX, velX) -> posX + velX to maxOf(0, velX - 1) }
      .takeWhile { (posX, _) -> posX <= x2 }
      .map { it.first }
    val seqY = generateSequence(0 to velocityY) { (posY, velY) -> posY + velY to velY - 1 }
      .takeWhile { (posY, _) -> posY >= y1 }
      .map { it.first }
    return (seqX zip seqY).find { it in this }?.let { velocityX to velocityY }
  }
}

fun x(step: Int, vx: Int): Int = ((step * (step + 1)) / 2 + step * (vx - step)).coerceAtMost((vx * (vx + 1)) / 2)

fun y(step: Int, vy: Int): Int = ((step - 2 * vy - 1) * step) / -2

fun findAllHittingVelocities(targetArea: TargetArea): List<Pair<Int, Int>> {
  val x = computeHorizontalVelocityRange(targetArea).flatMap { velocityX ->
    computeVerticalVelocityRange(targetArea).map { velocityY ->
      targetArea.targetHitPosition(velocityX, velocityY)
    }
  }
  println("tried ${x.size} different velocity pairs")
  return x.filterNotNull().distinct()
}

fun computeHorizontalVelocityRange(targetArea: TargetArea): IntRange = IntRange(((sqrt(8.0 * targetArea.x1 + 1) - 1) / 2).toInt(), targetArea.x2)


fun computeVerticalVelocityRange(targetArea: TargetArea): IntRange = IntRange(targetArea.y1, -targetArea.y1 - 1)



// data class ProbeState(val velocityX: Int, val velocityY: Int, val x: Int = 0, val y: Int = 0) {
//   fun nextStep() = ProbeState(velocityX - velocityX.sign, velocityY - 1, x + velocityX, y + velocityY)
// }
//
// data class MaximumHeightAndHitState(val maximumHeight: Int, val hitState: Boolean)
//
// fun shoot(targetArea: TargetArea, vx: Int, vy: Int): MaximumHeightAndHitState {
//   with(targetArea) {
//     tailrec fun shoot(probeState: ProbeState = ProbeState(vx, vy), maximumHeight: Int = 0): MaximumHeightAndHitState = when {
//       probeState.y < min(y1, y2)        -> MaximumHeightAndHitState(maximumHeight, false)
//       isHit(probeState.x, probeState.y) -> MaximumHeightAndHitState(maximumHeight, true)
//       else                              -> shoot(probeState.nextStep(), max(maximumHeight, probeState.nextStep().y))
//     }
//     return shoot()
//   }
// }

// /**
//  * x must reach at least target.x1 and must not be greater than target.x2 during shooting.
//  * vx decreases by 1 after each step, so x can at most reach vx_start + (vx_start + 1) / 2,
//  * thus vx must at least have value floor((sqrt(8 * target.x1 + 1) - 1) / 2)
//  * and  vx must at most  have value target.x2 (otherwise the first step would put probe after target area.
//  */
// fun computeHorizontalVelocityRange(targetArea: TargetArea): IntRange =
//   IntRange(((sqrt(8.0 * targetArea.x1 + 1) - 1) / 2).toInt(), targetArea.x2)

// x after step:
// example: vx = 5 =>
// step = 1 => 5                         =  5
// step = 2 => 5 + 4                     =  9
// step = 3 => 5 + 4 + 3                 = 12
// step = 4 => 5 + 4 + 3 + 2             = 14
// step = 5 => 5 + 4 + 3 + 2 + 1         = 15
// step = 6 => 5 + 4 + 3 + 2 + 1 + 0     = 15
// step = 7 => 5 + 4 + 3 + 2 + 1 + 0 + 0 = 15
// general computation:
// step >= vx => (vx * (vx + 1)) / 2
// else       => (step * (step + 1)) / 2 + step * (vx - step)
// example: vx = 5 =>
// step >= vx => 5 * 6 / 2                                          = 15
// else       => step = 1 => (1 * 2) / 2 + 1 * (5 - 1) =  1 + 1 * 4 =  5
// else       => step = 2 => (2 * 3) / 2 + 2 * (5 - 2) =  3 + 2 * 3 =  9
// else       => step = 3 => (3 * 4) / 2 + 3 * (5 - 3) =  6 + 3 * 2 = 12
// else       => step = 4 => (4 * 5) / 2 + 4 * (5 - 4) = 10 + 4 * 1 = 14

// vy_start = 5 => vy(step) = 5 - step
//              =>  y(step) = summe(5 - s, s = 1..step) = ((step - 11) * step) / -2
// y at step =
// example: vy = 5 =>
// step  0: vy =  5 = 5 -  0,    y =   0,    vy = 5 - step = 5 -  0 =  5,    y = (( 0 - 11) *  0) / -2 =   0
// step  1: vy =  4 = 5 -  1,    y =   5,    vy = 5 - step = 5 -  1 =  4,    y = (( 1 - 11) *  1) / -2 =   5
// step  2: vy =  3 = 5 -  2,    y =   9,    vy = 5 - step = 5 -  2 =  3,    y = (( 2 - 11) *  2) / -2 =   9
// step  3: vy =  2 = 5 -  3,    y =  12,    vy = 5 - step = 5 -  3 =  2,    y = (( 3 - 11) *  3) / -2 =  12
// step  4: vy =  1 = 5 -  4,    y =  14,    vy = 5 - step = 5 -  4 =  1,    y = (( 4 - 11) *  4) / -2 =  14
// step  5: vy =  0 = 5 -  5,    y =  15,    vy = 5 - step = 5 -  5 =  0,    y = (( 5 - 11) *  5) / -2 =  15
// step  6: vy = -1 = 5 -  6,    y =  15,    vy = 5 - step = 5 -  6 = -1,    y = (( 6 - 11) *  6) / -2 =  15
// step  7: vy = -2 = 5 -  7,    y =  14,    vy = 5 - step = 5 -  7 = -2,    y = (( 7 - 11) *  7) / -2 =  14
// step  8: vy = -3 = 5 -  8,    y =  12,    vy = 5 - step = 5 -  8 = -3,    y = (( 8 - 11) *  8) / -2 =  12
// step  9: vy = -4 = 5 -  9,    y =   9,    vy = 5 - step = 5 -  9 = -4,    y = (( 9 - 11) *  9) / -2 =   9
// step 10: vy = -5 = 5 - 10,    y =   5,    vy = 5 - step = 5 - 10 = -5,    y = ((10 - 11) * 10) / -2 =   5
// step 11: vy = -6 = 5 - 11,    y =   0,    vy = 5 - step = 5 - 11 = -6,    y = ((11 - 11) * 11) / -2 =   0
// step 12: vy = -7 = 5 - 12,    y =  -6,    vy = 5 - step = 5 - 12 = -7,    y = ((12 - 11) * 12) / -2 =  -6
// step 13: vy = -8 = 5 - 13,    y = -13,    vy = 5 - step = 5 - 13 = -8,    y = ((13 - 11) * 13) / -2 = -13
// step 14: vy = -9 = 5 - 14,    y = -21,    vy = 5 - step = 5 - 14 = -9,    y = ((14 - 11) * 14) / -2 = -21

// fun vy(y: Int, step: Int): Double = (2.0 * y / step + step - 1) / 2