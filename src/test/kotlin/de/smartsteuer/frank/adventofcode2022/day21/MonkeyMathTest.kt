package de.smartsteuer.frank.adventofcode2022.day21

import de.smartsteuer.frank.adventofcode2022.day21.Day21.parseExpressions
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.*
import de.smartsteuer.frank.adventofcode2022.day21.Day21.part1
import de.smartsteuer.frank.adventofcode2022.day21.Day21.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MonkeyMathTest {
  @Suppress("SpellCheckingInspection")
  private val input = listOf(
    "root: pppw + sjmn",
    "dbpl: 5",
    "cczh: sllz + lgvd",
    "zczc: 2",
    "ptdq: humn - dvpt",
    "dvpt: 3",
    "lfqf: 4",
    "humn: 5",
    "ljgn: 2",
    "sjmn: drzm * dbpl",
    "sllz: 4",
    "pppw: cczh / lfqf",
    "lgvd: ljgn * ptdq",
    "drzm: hmdt - zczc",
    "hmdt: 32",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 152
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 0
  }

  @Suppress("SpellCheckingInspection")
  @Test
  fun `expressions can be parsed`() {
    parseExpressions(input) shouldBe listOf(
      Assigment("root", Sum           (UnresolvedVariable("pppw"), UnresolvedVariable("sjmn"))),
      Assigment("dbpl", Literal(5)),
      Assigment("cczh", Sum           (UnresolvedVariable("sllz"), UnresolvedVariable("lgvd"))),
      Assigment("zczc", Literal(2)),
      Assigment("ptdq", Subtraction   (UnresolvedVariable("humn"), UnresolvedVariable("dvpt"))),
      Assigment("dvpt", Literal(3)),
      Assigment("lfqf", Literal(4)),
      Assigment("humn", Literal(5)),
      Assigment("ljgn", Literal(2)),
      Assigment("sjmn", Multiplication(UnresolvedVariable("drzm"), UnresolvedVariable("dbpl"))),
      Assigment("sllz", Literal(4)),
      Assigment("pppw", Division      (UnresolvedVariable("cczh"), UnresolvedVariable("lfqf"))),
      Assigment("lgvd", Multiplication(UnresolvedVariable("ljgn"), UnresolvedVariable("ptdq"))),
      Assigment("drzm", Subtraction   (UnresolvedVariable("hmdt"), UnresolvedVariable("zczc"))),
      Assigment("hmdt", Literal(32)),
    )
  }
}
