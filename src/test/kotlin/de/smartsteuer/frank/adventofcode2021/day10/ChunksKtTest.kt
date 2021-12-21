package de.smartsteuer.frank.adventofcode2021.day10

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.ArrayDeque

internal class ChunksKtTest {
  private val expectedScores = listOf(
    Score(0, 288_957),
    Score(0, 5_566),
    Score(1_197, 0),
    Score(0, 1_480_781),
    Score(3, 0),
    Score(57, 0),
    Score(0, 995_444),
    Score(3, 0),
    Score(25_137, 0),
    Score(0, 294)
  )

  @Test
  internal fun `syntax check should return expected scores`() {
    val chunks = listOf("[({(<(())[]>[[{[]{<()<>>",
                        "[(()[<>])]({[<{<<[]>>(",
                        "{([(<{}[<>[]}>{[]{[(<()>",
                        "(((({<>}<{<{<>}{[]{[]{}",
                        "[[<[([]))<([[{}[[()]]]",
                        "[{[{({}]{}}([{[{{{}}([]",
                        "{<[[]]>}<{[{[{[]{()[[[]",
                        "[<(<(<(<{}))><([]([]()",
                        "<{([([[(<>()){}]>(<<{{",
                        "<{([{{}}[<[[[<>{}]]]>[]]")
    score(chunks) shouldContainExactly expectedScores
  }

  @Test
  internal fun `completion score is computed as expected`() {
    val stack = ArrayDeque<Char>()
    stack.push('>')
    stack.push('}')
    stack.push(')')
    stack.push(']')
    completionScore(stack) shouldBe 294
  }

  @Test
  internal fun `middle score is correct`() {
    middleScore(expectedScores) shouldBe 288957
  }
}