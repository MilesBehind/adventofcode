package de.smartsteuer.frank.adventofcode2025.day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ReactorTest {
  private val input1 = listOf(
    "aaa: you hhh",
    "you: bbb ccc",
    "bbb: ddd eee",
    "ccc: ddd eee fff",
    "ddd: ggg",
    "eee: out",
    "fff: out",
    "ggg: out",
    "hhh: ccc fff iii",
    "iii: out",
  )

  private val input2 = listOf(
    "svr: aaa bbb",
    "aaa: fft",
    "fft: ccc",
    "bbb: tty",
    "tty: ccc",
    "ccc: ddd eee",
    "ddd: hub",
    "hub: fff",
    "eee: dac",
    "dac: fff",
    "fff: ggg hhh",
    "ggg: out",
    "hhh: out",
  )

  @Test
  fun `part 1 returns expected result`() {
    Reactor.part1(input1) shouldBe 5
  }

  @Test
  fun `part 2 returns expected result`() {
    Reactor.part2(input2) shouldBe 2
  }
}