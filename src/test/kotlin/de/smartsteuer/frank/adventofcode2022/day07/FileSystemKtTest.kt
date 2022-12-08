package de.smartsteuer.frank.adventofcode2022.day07

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FileSystemKtTest {

  private val fileSystemOutput = listOf(
      "$ cd /",
      "$ ls",
      "dir a",
      "14848514 b.txt",
      "8504156 c.dat",
      "dir d",
      "$ cd a",
      "$ ls",
      "dir e",
      "29116 f",
      "2557 g",
      "62596 h.lst",
      "$ cd e",
      "$ ls",
      "584 i",
      "$ cd ..",
      "$ cd ..",
      "$ cd d",
      "$ ls",
      "4060174 j",
      "8033020 d.log",
      "5626152 d.ext",
      "7214296 k",
  )

  @Test
  fun `part 1 is correct`() {
    part1(fileSystemOutput) shouldBe 95_437
  }

  @Test
  fun `part 2 is correct`() {
    part2(fileSystemOutput) shouldBe 24_933_642
  }

  @Test
  fun `parseLine() is correct`() {
    assertSoftly {
      parseLine("dir x")     shouldBe Directory("x")
      parseLine("123 a.txt") shouldBe File("a.txt", 123)
    }
  }

  @Test
  fun `parseFileSystemOutput is correct`() {
    parseFileSystemOutput(fileSystemOutput).size() shouldBe 48381165
  }

  @Test
  fun `simple part 1 is correct`() {
    part1Simple(fileSystemOutput) shouldBe 95_437
  }

  @Test
  fun `simple part 2 is correct`() {
    part2Simple(fileSystemOutput) shouldBe 24_933_642
  }
}

