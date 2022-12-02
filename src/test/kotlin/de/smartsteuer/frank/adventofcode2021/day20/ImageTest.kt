package de.smartsteuer.frank.adventofcode2021.day20

import de.smartsteuer.frank.adventofcode2021.day20.Image.Companion.OFF
import de.smartsteuer.frank.adventofcode2021.day20.Image.Companion.toPixelMapIndex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ImageTest {
  private val pixelMapData = "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#"
  private val imageData = listOf(
    "#..#.",
    "#....",
    "##..#",
    "..#..",
    "..###"
  )

  @Test
  internal fun `toBinary converts list of boolean to binary value`() {
    val input    = "...#...#."
    input.toPixelMapIndex() shouldBe 34
  }

  @Test
  internal fun `image enhancement produces expected output`() {
    val image0 = Image(imageData)
    image0.render() shouldBe listOf(
      "#..#.",
      "#....",
      "##..#",
      "..#..",
      "..###",
    )
    image0.countWhitePixels() shouldBe 10

    val pixelMap = PixelMap(pixelMapData)
    val image1 = image0.apply(pixelMap, OFF)
    image1.render() shouldBe listOf(
      ".##.##.",
      "#..#.#.",
      "##.#..#",
      "####..#",
      ".#..##.",
      "..##..#",
      "...#.#.",
    )
    image1.countWhitePixels() shouldBe 24

    val image2 = image1.apply(pixelMap, OFF)
    image2.render() shouldBe listOf(
      ".......#.",
      ".#..#.#..",
      "#.#...###",
      "#...##.#.",
      "#.....#.#",
      ".#.#####.",
      "..#.#####",
      "...##.##.",
      "....###..",
    )
    image2.countWhitePixels() shouldBe 35

    val imageAfter50Rounds = (1..50).fold(image0) { result, _ -> result.apply(pixelMap, OFF) }
    println(imageAfter50Rounds.render().joinToString("\n"))
    imageAfter50Rounds.countWhitePixels() shouldBe 3351
  }
}