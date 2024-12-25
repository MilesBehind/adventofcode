package de.smartsteuer.frank.adventofcode2024.day24

import de.smartsteuer.frank.adventofcode2024.day24.CrossedWires.parseDevice
import de.smartsteuer.frank.adventofcode2024.day24.CrossedWires.part1
import de.smartsteuer.frank.adventofcode2024.day24.CrossedWires.part2
import de.smartsteuer.frank.adventofcode2024.lines
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CrossedWiresTest {
  private val input1 = listOf(
    "x00: 1",
    "x01: 1",
    "x02: 1",
    "y00: 0",
    "y01: 1",
    "y02: 0",
    "",
    "x00 AND y00 -> z00",
    "x01 XOR y01 -> z01",
    "x02 OR y02 -> z02",
  )

  private val input2 = listOf(
    "x00: 1",
    "x01: 0",
    "x02: 1",
    "x03: 1",
    "x04: 0",
    "y00: 1",
    "y01: 1",
    "y02: 1",
    "y03: 1",
    "y04: 1",
    "",
    "ntg XOR fgs -> mjb",
    "y02 OR x01 -> tnw",
    "kwq OR kpj -> z05",
    "x00 OR x03 -> fst",
    "tgd XOR rvg -> z01",
    "vdt OR tnw -> bfw",
    "bfw AND frj -> z10",
    "ffh OR nrd -> bqk",
    "y00 AND y03 -> djm",
    "y03 OR y00 -> psh",
    "bqk OR frj -> z08",
    "tnw OR fst -> frj",
    "gnj AND tgd -> z11",
    "bfw XOR mjb -> z00",
    "x03 OR x00 -> vdt",
    "gnj AND wpb -> z02",
    "x04 AND y00 -> kjc",
    "djm OR pbm -> qhw",
    "nrd AND vdt -> hwm",
    "kjc AND fst -> rvg",
    "y04 OR y02 -> fgs",
    "y01 AND x02 -> pbm",
    "ntg OR kjc -> kwq",
    "psh XOR fgs -> tgd",
    "qhw XOR tgd -> z09",
    "pbm OR djm -> kpj",
    "x03 XOR y03 -> ffh",
    "x00 XOR y04 -> ntg",
    "bfw OR bqk -> z06",
    "nrd XOR fgs -> wpb",
    "frj XOR qhw -> z04",
    "bqk OR frj -> z07",
    "y03 OR x01 -> nrd",
    "hwm AND bqk -> z03",
    "tgd XOR rvg -> z12",
    "tnw OR pbm -> gnj",
  )

  private val input3 = listOf(
    "x00: 0",
    "x01: 1",
    "x02: 0",
    "x03: 1",
    "x04: 0",
    "x05: 1",
    "y00: 0",
    "y01: 0",
    "y02: 1",
    "y03: 1",
    "y04: 0",
    "y05: 1",
    "",
    "x00 AND y00 -> z05",
    "x01 AND y01 -> z02",
    "x02 AND y02 -> z01",
    "x03 AND y03 -> z03",
    "x04 AND y04 -> z04",
    "x05 AND y05 -> z00",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input1) shouldBe 4
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input1) shouldBe 42
  }

  @Test
  fun `result names can be found`() {
    input1.parseDevice().findVariableNames('z') shouldContainExactlyInAnyOrder listOf("z00", "z01", "z02")
    input2.parseDevice().findVariableNames('z') shouldContainExactlyInAnyOrder listOf(
      "z00", "z01", "z02", "z03", "z04", "z05", "z06", "z07", "z08", "z09", "z10", "z11", "z12"
    )
  }

  @Test
  fun `expressions can be found`() {
    input1.parseDevice().expressions shouldBe mapOf(
      "z00" to CrossedWires.And("x00", "y00"),
      "z01" to CrossedWires.Xor("x01", "y01"),
      "z02" to CrossedWires.Or ("x02", "y02")
    )
  }

  @Test
  fun `expressions can be evaluated`() {
    val device = input1.parseDevice()
    device.compute("z00") shouldBe 0
    device.compute("z01") shouldBe 0
    device.compute("z02") shouldBe 1
  }

  @Test
  fun `result can be computed`() {
    input1.parseDevice().computeResult() shouldBe    4
    input2.parseDevice().computeResult() shouldBe 2024
  }

  @Test
  fun `expected result-bits can be computed`() {
    val x = "101010".toInt(2) // 42
    val y = "101100".toInt(2) // 44
    val z = x + y             // 86
    z.toString(2) shouldBe "1010110"
    val device = input3.parseDevice()
    device.computeBits(device.computeExpectedResult(), 'z') shouldBe mapOf("z00" to 0, "z01" to 1, "z02" to 1, "z03" to 0, "z04" to 1, "z05" to 0, "z06" to 1)
  }

  @Test
  fun `flipped z-bits can be computed`() {
    val device = lines("/adventofcode2024/day24/input-and-gates.txt").parseDevice()
    device.computeFlippedBits() shouldBe mapOf("z12" to 1, "z13" to 1, "z14" to 1, "z15" to 0, "z29" to 0, "z30" to 1, "z33" to 0, "z34" to 0, "z35" to 0, "z36" to 1)
  }
}