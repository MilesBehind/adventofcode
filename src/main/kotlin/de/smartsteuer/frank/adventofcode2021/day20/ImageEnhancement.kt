package de.smartsteuer.frank.adventofcode2021.day20

import de.smartsteuer.frank.adventofcode2021.day20.Image.Companion.OFF
import de.smartsteuer.frank.adventofcode2021.day20.Image.Companion.ON
import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val input            = lines("/adventofcode2021/day20/image-data.txt")
  val pixelMap         = PixelMap(input.first())
  val image            = Image(input.drop(2))
  val imageAfter2Steps = image.apply(pixelMap, OFF).apply(pixelMap, ON)
  println(imageAfter2Steps.render().joinToString("\n"))
  println("white pixels = ${imageAfter2Steps.countWhitePixels()}")
  val imageAfter50Steps = (1..50).fold(image to OFF) { (result, defaultValue), _ ->
    result.apply(pixelMap, defaultValue) to if (defaultValue == OFF) pixelMap.atAllZero else pixelMap.atAllOne
  }.first
  println(imageAfter50Steps.render().joinToString("\n"))
  println("white pixels = ${imageAfter50Steps.countWhitePixels()}")
}

data class Pixel(val x: Int, val y: Int) {
  fun neighbours() = listOf(Pixel(x - 1, y - 1), Pixel(x, y - 1), Pixel(x + 1, y - 1),
                            Pixel(x - 1, y    ), this,            Pixel(x + 1, y    ),
                            Pixel(x - 1, y + 1), Pixel(x, y + 1), Pixel(x + 1, y + 1))
}

class Image(private val imageData: List<String>) {
  companion object {
    const val ON  = '#'
    const val OFF = '.'
    fun String.toPixelMapIndex(): Int = fold(0) { result, char -> result shl 1 or if (char == ON) 1 else 0 }
    operator fun List<String>.contains(pixel: Pixel) = pixel.y in indices && pixel.x in first().indices
  }

  fun apply(pixelMap: PixelMap, defaultValue: Char): Image {
    val newImageData: List<String> = (-1..imageData.size).map { y ->
      (-1..imageData.first().length).map { x ->
        val index = Pixel(x, y).neighbours().map { pixel -> pixel.value(defaultValue) }.joinToString("").toPixelMapIndex()
        pixelMap.lookup(index)
      }.joinToString("")
    }
    return Image(newImageData)
  }

  private fun Pixel.value(defaultValue: Char): Char = if (this in imageData) imageData[y][x] else defaultValue

  fun render(): List<String> = imageData

  fun countWhitePixels() = imageData.joinToString("").count { it == ON }
}

class PixelMap(val map: String) {
  val atAllZero: Char = map.first()
  val atAllOne:  Char = map.last()

  fun lookup(value: Int): Char = map[value]
}
