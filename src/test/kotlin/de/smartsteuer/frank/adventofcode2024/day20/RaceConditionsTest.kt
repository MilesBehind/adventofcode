package de.smartsteuer.frank.adventofcode2024.day20

import de.smartsteuer.frank.adventofcode2024.day20.RaceCondition.Pos
import de.smartsteuer.frank.adventofcode2024.day20.RaceCondition.parseRaceTrack
import de.smartsteuer.frank.adventofcode2024.day20.RaceCondition.part1
import de.smartsteuer.frank.adventofcode2024.day20.RaceCondition.part2
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RaceConditionTest {
  private val input = listOf(
    "###############",
    "#...#...#.....#",
    "#.#.#.#.#.###.#",
    "#S#...#.#.#...#",
    "#######.#.#.###",
    "#######.#.#...#",
    "#######.#.###.#",
    "###..E#...#...#",
    "###.#######.###",
    "#...###...#...#",
    "#.#####.#.###.#",
    "#.#...#.#.#...#",
    "#.#.#.#.#.#.###",
    "#...#...#...###",
    "###############",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 0
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 0
  }

  @Test
  fun `race track positions without cheating are found`() {
    val positions = input.parseRaceTrack().trackPositionsWithoutCheating.keys.toList()
    positions shouldHaveSize 84 + 1
    positions[ 0] shouldBe Pos( 1,  3)
    positions[12] shouldBe Pos( 7,  1)
    positions[18] shouldBe Pos( 7,  7)
    positions[30] shouldBe Pos(13,  1)
    positions[48] shouldBe Pos(13, 11)
    positions[60] shouldBe Pos( 7,  9)
    positions[84] shouldBe Pos( 5,  7)
  }

  @Test
  fun `time for some track position can be found`() {
    val raceTrack = input.parseRaceTrack()
    raceTrack.time(Pos( 1,  3)) shouldBe  0
    raceTrack.time(Pos( 7,  1)) shouldBe 12
    raceTrack.time(Pos( 7,  7)) shouldBe 18
    raceTrack.time(Pos(13,  1)) shouldBe 30
    raceTrack.time(Pos(13, 11)) shouldBe 48
    raceTrack.time(Pos( 7,  9)) shouldBe 60
    raceTrack.time(Pos( 5,  7)) shouldBe 84
  }

  @Test
  fun `cheats with maximum length of 2 can be found`() {
    val raceTrack = input.parseRaceTrack()
    val cheats = raceTrack.findCheats(minimumTimeSaved = 1, maximumCheatLength = 2)
    cheats[ 2] shouldBe 14
    cheats[ 4] shouldBe 14
    cheats[ 6] shouldBe  2
    cheats[ 8] shouldBe  4
    cheats[10] shouldBe  2
    cheats[12] shouldBe  3
    cheats[20] shouldBe  1
    cheats[36] shouldBe  1
    cheats[38] shouldBe  1
    cheats[40] shouldBe  1
    cheats[64] shouldBe  1
    cheats shouldHaveSize 11
  }

  @Test
  fun `cheats with maximum length of 20 can be found`() {
    val raceTrack = input.parseRaceTrack()
    val cheats = raceTrack.findCheats(minimumTimeSaved = 50, maximumCheatLength = 20)
    cheats[50] shouldBe 32
    cheats[52] shouldBe 31
    cheats[54] shouldBe 29
    cheats[56] shouldBe 39
    cheats[58] shouldBe 25
    cheats[60] shouldBe 23
    cheats[62] shouldBe 20
    cheats[64] shouldBe 19
    cheats[66] shouldBe 12
    cheats[68] shouldBe 14
    cheats[70] shouldBe 12
    cheats[72] shouldBe 22
    cheats[74] shouldBe  4
    cheats[76] shouldBe  3
    cheats shouldHaveSize 14
  }
}