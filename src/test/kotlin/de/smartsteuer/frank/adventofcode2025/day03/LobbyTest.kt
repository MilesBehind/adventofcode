package de.smartsteuer.frank.adventofcode2025.day03

import de.smartsteuer.frank.adventofcode2025.day03.Lobby.part1
import de.smartsteuer.frank.adventofcode2025.day03.Lobby.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LobbyTest {
  private val input = listOf(
    "987654321111111",
    "811111111111119",
    "234234234234278",
    "818181911112111",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 357
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 3121910778619
  }

  @Test
  fun `maximum joltage can be found`() {
    val batteryBank = input.take(1).parseBatteryBanks().first()
    batteryBank.maximumJoltage( 2) shouldBe 98L
    batteryBank.maximumJoltage( 3) shouldBe 987L
    batteryBank.maximumJoltage( 4) shouldBe 9876L
    batteryBank.maximumJoltage( 5) shouldBe 98765L
    batteryBank.maximumJoltage( 6) shouldBe 987654L
    batteryBank.maximumJoltage( 7) shouldBe 9876543L
    batteryBank.maximumJoltage( 8) shouldBe 98765432L
    batteryBank.maximumJoltage( 9) shouldBe 987654321L
    batteryBank.maximumJoltage(10) shouldBe 9876543211L

    BatteryBank("987654321111111".toList().map { it.digitToInt() }).maximumJoltage(2) shouldBe 98
    BatteryBank("811111111111119".toList().map { it.digitToInt() }).maximumJoltage(2) shouldBe 89
    BatteryBank("234234234234278".toList().map { it.digitToInt() }).maximumJoltage(2) shouldBe 78
    BatteryBank("818181911112111".toList().map { it.digitToInt() }).maximumJoltage(2) shouldBe 92
  }
}