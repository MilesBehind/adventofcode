package de.smartsteuer.frank.adventofcode2021.day02

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PlannedCourseKtTest {
  @Test
  internal fun `empty command list does not change position and depth`() {
    executeCommands(0, 0, emptyList()) shouldBe SubmarineState(0, 0)
  }

  @Test
  internal fun `forward command changes position`() {
    executeCommands(0, 0, listOf("forward 10").toCommands()) shouldBe SubmarineState(10, 0)
  }

  @Test
  internal fun `down command increases depth`() {
    executeCommands(0, 0, listOf("down 10").toCommands()) shouldBe SubmarineState(0, 10)
  }

  @Test
  internal fun `up command decreases depth`() {
    executeCommands(0, 10, listOf("up 8").toCommands()) shouldBe SubmarineState(0, 2)
  }

  @Test
  internal fun `given commands result in expected position and depth`() {
    executeCommands(0, 0, listOf("forward 5",
                                 "down 5",
                                 "forward 8",
                                 "up 3",
                                 "down 8",
                                 "forward 2").toCommands()) shouldBe SubmarineState(15, 10)
  }



  @Test
  internal fun `empty command list does not change position and depth and aim`() {
    executeCommands(0, 0, 0, emptyList()) shouldBe SubmarineState(0, 0, 0)
  }

  @Test
  internal fun `forward command changes position and does not change depth if aim is 0`() {
    executeCommands(0, 0, 0, listOf("forward 10").toCommands()) shouldBe SubmarineState(10, 0, 0)
  }

  @Test
  internal fun `forward command changes position and depth if aim is not 0`() {
    executeCommands(0, 0, 5, listOf("forward 10").toCommands()) shouldBe SubmarineState(10, 50, 5)
  }

  @Test
  internal fun `down command increases aim`() {
    executeCommands(0, 0, 0, listOf("down 10").toCommands()) shouldBe SubmarineState(0, 0, 10)
  }

  @Test
  internal fun `up command decreases aim`() {
    executeCommands(0, 0, 10, listOf("up 8").toCommands()) shouldBe SubmarineState(0, 0, 2)
  }

  @Test
  internal fun `given commands result in expected position and depth and aim`() {
    executeCommands(0, 0, 0, listOf("forward 5",
                                    "down 5",
                                    "forward 8",
                                    "up 3",
                                    "down 8",
                                    "forward 2").toCommands()) shouldBe SubmarineState(15, 60, 10)
  }
}