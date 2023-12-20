package de.smartsteuer.frank.adventofcode2023.day20

import de.smartsteuer.frank.adventofcode2023.lines
import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class PulsePropagationTest {
  private val input1 = listOf(
    "broadcaster -> a, b, c",
    "%a -> b",
    "%b -> c",
    "%c -> inv",
    "&inv -> a",
  )

  private val input2 = listOf(
    "broadcaster -> a",
    "%a -> inv, con",
    "&inv -> b",
    "%b -> con",
    "&con -> output",
  )

  @Test
  fun `part 1`() {
    part1(parseModules(input1)) shouldBe 32_000_000    // =>  8 * 1000 low,  4 * 1000 high
    part1(parseModules(input2)) shouldBe 11_687_500    // => 17 *  250 low, 11 *  250 high
  }

  @Test
  fun `part 2`() {
    val lines = lines("/adventofcode2023/day20/modules.txt")
    part2(parseModules(lines)) shouldBe 247_702_167_614_647 // > 247 Billionen
  }

  @Test
  fun `modules can be parsed for input1`() {
    parseModules(input1) shouldBe Communication(listOf(
      Broadcaster("broadcaster", listOf("a", "b", "c")),
      FlipFlop   ("a",           listOf("b")),
      FlipFlop   ("b",           listOf("c")),
      FlipFlop   ("c",           listOf("inv")),
      Conjunction("inv",         listOf("a"), listOf("c")),
    ))
  }

  @Test
  fun `modules can be parsed for input2`() {
    parseModules(input2) shouldBe Communication(listOf(
      Broadcaster("broadcaster", listOf("a")),
      FlipFlop   ("a",           listOf("inv", "con")),
      Conjunction("inv",         listOf("b"), listOf("a")),
      FlipFlop   ("b",           listOf("con")),
      Conjunction("con",         listOf("output"), listOf("a", "b")),
    ))
  }

  @Test
  fun `pulse can be sent for input1`() {
    val communication = parseModules(input1)
    println("--------------   input 1   ------------")
    println("-------------- push button --------------")
    communication.pushButton()
  }

  @Test
  fun `pulse can be sent for input2`() {
    val communication = parseModules(input2)
    println("--------------   input 2   ------------")
    println("-------------- push button --------------")
    communication.pushButton()
    println("-------------- push button --------------")
    communication.pushButton()
    println("-------------- push button --------------")
    communication.pushButton()
    println("-------------- push button --------------")
    communication.pushButton()
  }

  @Test
  fun `indirect sender for rx can be found`() {
    val lines = lines("/adventofcode2023/day20/modules.txt")
    val communication = parseModules(lines)
    communication.findIndirectSendersForRx() shouldBe listOf("mp", "qt", "qb", "ng")
  }
}