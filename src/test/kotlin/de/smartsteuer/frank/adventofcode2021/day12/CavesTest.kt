package de.smartsteuer.frank.adventofcode2021.day12

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

internal class CavesTest {
  private val definition1 = listOf("start-A",
                                   "start-b",
                                   "A-c",
                                   "A-b",
                                   "b-d",
                                   "A-end",
                                   "b-end")
  private val definition2 = listOf("dc-end",
                                   "HN-start",
                                   "start-kj",
                                   "dc-start",
                                   "dc-HN",
                                   "LN-dc",
                                   "HN-end",
                                   "kj-sa",
                                   "kj-HN",
                                   "kj-dc")

  @Test
  internal fun `cave definition is parsed correctly`() {
    val caves1 = Caves(definition1)
    caves1.caves.size shouldBe 6
    caves1.caves["start"] shouldContainExactlyInAnyOrder listOf("A", "b")
    caves1.caves["c"    ] shouldContainExactlyInAnyOrder listOf("A")
    caves1.caves["A"    ] shouldContainExactlyInAnyOrder listOf("start", "b", "end", "c")
    caves1.caves["b"    ] shouldContainExactlyInAnyOrder listOf("start", "d", "end", "A")
    caves1.caves["d"    ] shouldContainExactlyInAnyOrder listOf("b")
    caves1.caves["end"  ] shouldContainExactlyInAnyOrder listOf("A", "b")

    val caves2 = Caves(definition2)
    caves2.caves.size shouldBe 7
    caves2.caves["start"] shouldContainExactlyInAnyOrder listOf("HN", "kj", "dc")
    caves2.caves["dc"   ] shouldContainExactlyInAnyOrder listOf("end", "start", "HN", "LN", "kj")
    caves2.caves["HN"   ] shouldContainExactlyInAnyOrder listOf("start", "dc", "end", "kj")
    caves2.caves["kj"   ] shouldContainExactlyInAnyOrder listOf("start", "sa", "HN", "dc")
    caves2.caves["LN"   ] shouldContainExactlyInAnyOrder listOf("dc")
    caves2.caves["sa"   ] shouldContainExactlyInAnyOrder listOf("kj")
    caves2.caves["end"  ] shouldContainExactlyInAnyOrder listOf("dc", "HN")
  }

  @Test
  internal fun `number of found paths are correct`() {
    val caves1 = Caves(definition1)
    caves1.countPaths() shouldBe 10

    val caves2 = Caves(definition2)
    caves2.countPaths() shouldBe 19

    caves1.countPaths2() shouldBe 36

    caves2.countPaths2() shouldBe 103
  }
}
