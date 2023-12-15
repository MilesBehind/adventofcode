package de.smartsteuer.frank.adventofcode2023.day15

import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LensLibraryTest {
  val input = listOf(
    "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
  )

  @Test
  fun `part 1`() {
    part1(parseInitializationSequence(input)) shouldBe 1320
  }

  @Test
  fun `part 2`() {
    part2(parseInitializationSequence(input)) shouldBe 145
  }

  @Test
  fun `hash is correct`() {
    "rn=1".hash() shouldBe 30
    "cm-".hash()  shouldBe 253
    "qp=3".hash() shouldBe 97
    "cm=2".hash() shouldBe 47
    "qp-".hash()  shouldBe 14
    "pc=4".hash() shouldBe 180
    "ot=9".hash() shouldBe 9
    "ab=5".hash() shouldBe 197
    "pc-".hash()  shouldBe 48
    "pc=6".hash() shouldBe 214
    "ot=7".hash() shouldBe 231

    "rn".hash() shouldBe 0
    "cm".hash() shouldBe 0
    "qp".hash() shouldBe 1
    "ot".hash() shouldBe 3
    "ab".hash() shouldBe 3
    "pc".hash() shouldBe 3
  }

  @Test
  fun `steps can be parsed`() {
    val sequence = parseInitializationSequence(input)
    sequence.parseSteps() shouldBe listOf(
      Put("rn", 1, 0), Remove("cm", 0), Put("qp", 3, 1), Put("cm", 2, 0), Remove("qp", 1), Put("pc", 4, 3),
      Put("ot", 9, 3), Put("ab", 5, 3), Remove("pc", 3), Put("pc", 6, 3), Put("ot", 7, 3)
    )
  }

  @Test
  fun `steps can be applied to boxes`() {
    val boxes = parseInitializationSequence(input).applySequence()
    boxes.size shouldBe 256
    boxes[0] shouldBe Box(mutableMapOf("rn" to 1, "cm" to 2))
    boxes[3] shouldBe Box(mutableMapOf("ot" to 7, "ab" to 5, "pc" to 6))
    boxes.filterIndexed { index, _ -> index !in setOf(0, 3) }.forEach { it.lenses.shouldBeEmpty() }
  }

  @Test
  fun `sequence can be parsed`() {
    parseInitializationSequence(input) shouldBe InitializationSequence(listOf(
      "rn=1", "cm-", "qp=3", "cm=2", "qp-", "pc=4", "ot=9", "ab=5", "pc-", "pc=6", "ot=7"
    ))
  }
}