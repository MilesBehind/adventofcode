package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.BlockSequence.blocks
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BlockSequenceTest {

  @Test
  fun `buffering of sequence should allow going back to a mark`() {
    "abcdefg".asSequence().buffered().let { sequence ->
      sequence.next() shouldBe 'a'
      sequence.mark()
      sequence.next() shouldBe 'b'
      sequence.next() shouldBe 'c'
      sequence.reset()
      sequence.next() shouldBe 'b'
      sequence.next() shouldBe 'c'
      sequence.next() shouldBe 'd'
      sequence.mark()
      sequence.next() shouldBe 'e'
      sequence.next() shouldBe 'f'
      sequence.reset()
      sequence.next() shouldBe 'e'
      sequence.next() shouldBe 'f'
      sequence.next() shouldBe 'g'
    }
  }

  @Test
  fun `sequences can be transformed to block sequences`() {
    "".             asSequence().blocks().toList() shouldBe listOf()
    "0".            asSequence().blocks().toList() shouldBe listOf('0' to 1)
    "00".           asSequence().blocks().toList() shouldBe listOf('0' to 2)
    "000".          asSequence().blocks().toList() shouldBe listOf('0' to 3)
    "01".           asSequence().blocks().toList() shouldBe listOf('0' to 1, '1' to 1)
    "abc".          asSequence().blocks().toList() shouldBe listOf('a' to 1, 'b' to 1, 'c' to 1)
    "011222".       asSequence().blocks().toList() shouldBe listOf('0' to 1, '1' to 2, '2' to 3)
    "0001000111100".asSequence().blocks().toList() shouldBe listOf('0' to 3, '1' to 1, '0' to 3, '1' to 4, '0' to 2)
  }
}