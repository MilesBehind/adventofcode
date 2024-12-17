package de.smartsteuer.frank.adventofcode2024.day17

import de.smartsteuer.frank.adventofcode2024.day17.ChronospatialComputer.Computer
import de.smartsteuer.frank.adventofcode2024.day17.ChronospatialComputer.parseProgram
import de.smartsteuer.frank.adventofcode2024.day17.ChronospatialComputer.part1
import de.smartsteuer.frank.adventofcode2024.day17.ChronospatialComputer.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ChronospatialComputerTest {
  private fun computer(registerA: Long = 0, registerB: Long = 0, registerC: Long = 0, program: List<Int>) =
    Computer(registerA, registerB, registerC, program)

   private val input1 = listOf(
     "Register A: 729",
     "Register B: 0",
     "Register C: 0",
     "",
     "Program: 0,1,5,4,3,0",
   )

   private val input2 = listOf(
     "Register A: 2024",
     "Register B: 0",
     "Register C: 0",
     "",
     "Program: 0,3,5,4,3,0",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input1) shouldBe "4,6,3,5,6,3,5,2,1,0"
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input2) shouldBe "117440"
  }

  @Suppress("ComplexRedundantLet")
  @Test
  fun `program produces expected effects`() {
    computer(registerC =    9,                    program = listOf(2, 6)).let             { computer -> computer.run(); computer.registerB shouldBe 1 }
    computer(registerA =   10,                    program = listOf(5, 0, 5, 1, 5, 4)).let { computer -> computer.run() shouldBe listOf(0L, 1L, 2L) }
    computer(registerA = 2024,                    program = listOf(0, 1, 5, 4, 3, 0)).let { computer -> computer.run() shouldBe listOf(4,2,5,6,7,7,7,7,3,1,0).map { it.toLong() }; computer.registerA shouldBe 0 }
    computer(registerB =   29,                    program = listOf(1, 7)).let             { computer -> computer.run(); computer.registerB shouldBe 26 }
    computer(registerB = 2024, registerC = 43690, program = listOf(4, 0)).let             { computer -> computer.run(); computer.registerB shouldBe 44_354 }
  }

  @Test
  fun `program produces expected output`() {
    input1.parseProgram().run().joinToString(",") shouldBe "4,6,3,5,6,3,5,2,1,0"
  }
}