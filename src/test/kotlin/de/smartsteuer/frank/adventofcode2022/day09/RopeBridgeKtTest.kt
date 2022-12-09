package de.smartsteuer.frank.adventofcode2022.day09

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RopeBridgeKtTest {

  private val input = listOf(
    "R 4",
    "U 4",
    "L 3",
    "D 1",
    "R 4",
    "D 1",
    "L 5",
    "R 2",
  )

  @Test
  fun `part 1 is correct`() {
    Day09.part1(input) shouldBe 13
  }

  @Test
  fun `tail follows head as expected`() {
    fun c(x: Int, y: Int) = Day09.Coordinate(x, y)
    c(0, 0) follow c( 0,  0) shouldBe c( 0,  0)
    c(0, 0) follow c( 1,  0) shouldBe c( 0,  0)
    c(0, 0) follow c( 2,  0) shouldBe c( 1,  0)
    c(0, 0) follow c(-1,  0) shouldBe c( 0,  0)
    c(0, 0) follow c(-2,  0) shouldBe c(-1,  0)
    c(0, 0) follow c( 0,  1) shouldBe c( 0,  0)
    c(0, 0) follow c( 0,  2) shouldBe c( 0,  1)
    c(0, 0) follow c( 0, -1) shouldBe c( 0,  0)
    c(0, 0) follow c( 0, -2) shouldBe c( 0, -1)
    c(0, 0) follow c( 1,  1) shouldBe c( 0,  0)
    c(0, 0) follow c( 2,  1) shouldBe c( 1,  1)
    c(0, 0) follow c( 1,  2) shouldBe c( 1,  1)
    c(0, 0) follow c(-1,  2) shouldBe c(-1,  1)
    c(0, 0) follow c(-2,  1) shouldBe c(-1,  1)
    c(0, 0) follow c( 2, -1) shouldBe c( 1, -1)
    c(0, 0) follow c( 1, -2) shouldBe c( 1, -1)
    c(0, 0) follow c(-2, -1) shouldBe c(-1, -1)
    c(0, 0) follow c(-1, -2) shouldBe c(-1, -1)
  }
}