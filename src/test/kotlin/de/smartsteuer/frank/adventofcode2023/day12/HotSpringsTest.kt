package de.smartsteuer.frank.adventofcode2023.day12

import de.smartsteuer.frank.adventofcode2023.day12.SpringMap.Row
import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class HotSpringsTest {
  private val input = listOf(
    "???.### 1,1,3",
    ".??..??...?##. 1,1,3",
    "?#?#?#?#?#?#?#? 1,3,1,6",
    "????.#...#... 4,1,1",
    "????.######..#####. 1,6,5",
    "?###???????? 3,2,1",
  )

  @Test
  fun `part 1`() {
    part1(parseSpringMap(input)) shouldBe 21
  }

  @Test
  fun `part 2`() {
    part2(parseSpringMap(input)) shouldBe 525_152
  }

  @Test
  fun `spring map can be parsed`() {
    parseSpringMap(input) shouldBe SpringMap(listOf(
      Row("???.###",             listOf(1, 1, 3)),
      Row(".??..??...?##.",      listOf(1, 1, 3)),
      Row("?#?#?#?#?#?#?#?",     listOf(1, 3, 1, 6)),
      Row("????.#...#...",       listOf(4, 1, 1)),
      Row("????.######..#####.", listOf(1, 6, 5)),
      Row("?###????????",        listOf(3, 2, 1)),
    ))
  }

  @Test
  fun `solutions can be counted efficiently`() {
    Row("???.###",        listOf(1, 1, 3)).countSolutions() shouldBe  1
    Row(".??..??...?##.", listOf(1, 1, 3)).countSolutions() shouldBe  4
    Row("?###????????",   listOf(3, 2, 1)).countSolutions() shouldBe 10
  }

  @Test
  fun `solutions can be counted efficiently after unfolding`() {
    Row("???.###",             listOf(1, 1, 3)).unfold().countSolutions()    shouldBe       1
    Row(".??..??...?##.",      listOf(1, 1, 3)).unfold().countSolutions()    shouldBe  16_384
    Row("?#?#?#?#?#?#?#?",     listOf(1, 3, 1, 6)).unfold().countSolutions() shouldBe       1
    Row("????.#...#...",       listOf(4, 1, 1)).unfold().countSolutions()    shouldBe      16
    Row("????.######..#####.", listOf(1, 6, 5)).unfold().countSolutions()    shouldBe   2_500
    Row("?###????????",        listOf(3, 2, 1)).unfold().countSolutions()    shouldBe 506_250
  }
}