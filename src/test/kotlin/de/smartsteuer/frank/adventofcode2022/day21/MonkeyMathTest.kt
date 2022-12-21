package de.smartsteuer.frank.adventofcode2022.day21

import de.smartsteuer.frank.adventofcode2022.day21.Day21.parseExpressions
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.*
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Solver
import de.smartsteuer.frank.adventofcode2022.day21.Day21.part1
import de.smartsteuer.frank.adventofcode2022.day21.Day21.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class MonkeyMathTest {
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
    part1(input) shouldBe 152  // non-example result: 268597611536314
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 301  // non-example result: 3451534022348
  }

  @Test
  fun `solving works`() {
    val solver = Solver(parseExpressions(input))
    val patchedSolver = Solver(solver.expressions + ("humn" to Assigment("humn", Literal(301))))
    patchedSolver.solveRoot()
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

  @Test
  fun `expression can be simplified`() {
    simplify(Sum(Subtraction(Literal(5), Literal(3)), Multiplication(Literal(7), UnresolvedVariable("x")))) shouldBe
             Sum(Literal(2),                          Multiplication(Literal(7), UnresolvedVariable("x")))

    simplify(Sum(Subtraction(Literal(5), Literal(3)), Multiplication(UnresolvedVariable("x"), Literal(7)))) shouldBe
             Sum(Literal(2),                          Multiplication(UnresolvedVariable("x"), Literal(7)))

    simplify(Sum(Subtraction(Literal(5), UnresolvedVariable("x")), Multiplication(Literal(7), Literal(8)))) shouldBe
             Sum(Subtraction(Literal(5), UnresolvedVariable("x")), Literal(56))

    simplify(Sum(Subtraction(UnresolvedVariable("x"), Literal(5)), Multiplication(Literal(7), Literal(8)))) shouldBe
             Sum(Subtraction(UnresolvedVariable("x"), Literal(5)), Literal(56))

    simplify(Sum(Subtraction(Literal(5), Division(Literal(9), Literal(3))), Multiplication(Literal(7), UnresolvedVariable("x")))) shouldBe
             Sum(Literal(2),                                                Multiplication(Literal(7), UnresolvedVariable("x")))
  }

  private fun simplify(expression: Expression): Expression = Solver(listOf(Assigment("y", expression))).simplify(expression)
}
