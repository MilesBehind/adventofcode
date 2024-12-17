package de.smartsteuer.frank.adventofcode2024

import java.time.Duration

interface Day<T> {
  fun part1(input: List<String>): T
  fun part2(input: List<String>): T

  fun execute(input: List<String>) {
    val startPart1 = System.currentTimeMillis()
    val part1Result = part1(input)
    println("part 1: $part1Result (took ${Duration.ofMillis(System.currentTimeMillis() - startPart1)})")
    val startPart2 = System.currentTimeMillis()
    val part2Result = part2(input)
    println("part 2: $part2Result (took ${Duration.ofMillis(System.currentTimeMillis() - startPart2)})")
  }
}