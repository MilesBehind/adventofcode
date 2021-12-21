package de.smartsteuer.frank.adventofcode2021.day18

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ArgumentConverter
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource


internal class SnailFishNumberTest {
  private val snailFishNumbers = listOf(
    "[1,2]",
    "[[1,2],3]",
    "[9,[8,7]]",
    "[[1,9],[8,5]]",
    "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
    "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
    "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"
  )

  @Test
  internal fun `output string representation matches input string representation`() {
    snailFishNumbers.forEach { input ->
      val parsedNumber = SnailFishNumber(input)
      parsedNumber.toString() shouldBe input
    }
  }

  @Test
  internal fun `adding two numbers returns correct result`() {
    SnailFishNumber("[1,2]") + SnailFishNumber("[[3,4],5]") shouldBe SnailFishNumber("[[1,2],[[3,4],5]]")
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                  4..8",
    "[7,[6,[5,[4,[3,2]]]]];                 12..16",
    "[[6,[5,[4,[3,2]]]],1];                 10..14",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; 10..14",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]];     24..28",
    "[[3,[2,8]],[9,[5,3]]];                       ",
  ])
  internal fun `find first pair nested inside four pairs`(number: String, @ConvertWith(IntRangeConverter::class) expectedRange: IntRange?) {
    SnailFishNumber(number).findFirstPairNestedInsideFourPairs() shouldBe if (expectedRange != null) SnailFishNumber(number, expectedRange) else null
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[0,7],4],[15,[0,13]]],[1,1]]; 13..14",
    "[[3,[2,8]],[9,[5,3]]];                 ",
  ])
  internal fun `find first regular number greater than 9`(number: String, @ConvertWith(IntRangeConverter::class) expectedRange: IntRange?) {
    SnailFishNumber(number).findFirstRegularNumberGreater9() shouldBe if (expectedRange != null) SnailFishNumber(number, expectedRange) else null
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                  4..8;  ",
    "[7,[6,[5,[4,[3,2]]]]];                 12..16; 10..10",
    "[[6,[5,[4,[3,2]]]],1];                 10..14;  8..8",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; 10..14;  8..8",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]];     24..28; 22..22",
  ])
  internal fun `left neighbour is found`(input: String, @ConvertWith(IntRangeConverter::class) range: IntRange,
                                         @ConvertWith(IntRangeConverter::class) expectedRange: IntRange?) {
    SnailFishNumber(input, range).findLeftNeighbour() shouldBe if (expectedRange != null) SnailFishNumber(input, expectedRange) else null
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                  4..8;  10..10",
    "[7,[6,[5,[4,[3,2]]]]];                 12..16; ",
    "[[6,[5,[4,[3,2]]]],1];                 10..14; 19..19",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; 10..14; 20..20",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]];     24..28; ",
  ])
  internal fun `right neighbour is found`(input: String, @ConvertWith(IntRangeConverter::class) range: IntRange,
                                          @ConvertWith(IntRangeConverter::class) expectedRange: IntRange?) {
    SnailFishNumber(input, range).findRightNeighbour() shouldBe if (expectedRange != null) SnailFishNumber(input, expectedRange) else null
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                  4..8;   5..5",
    "[7,[6,[5,[4,[3,2]]]]];                 12..16; 13..13",
    "[[6,[5,[4,[3,2]]]],1];                 10..14; 11..11",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; 10..14; 11..11",
    "[[3,[2,[8,0]]],[9,[5,[4,[13,2]]]]];    24..29; 25..26",
  ])
  internal fun `left number is found`(input: String, @ConvertWith(IntRangeConverter::class) range: IntRange,
                                      @ConvertWith(IntRangeConverter::class) expectedRange: IntRange) {
    SnailFishNumber(input, range).left() shouldBe SnailFishNumber(input, expectedRange)
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                  4..8;   7..7",
    "[7,[6,[5,[4,[3,2]]]]];                 12..16; 15..15",
    "[[6,[5,[4,[3,2]]]],1];                 10..14; 13..13",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; 10..14; 13..13",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,12]]]]];    24..29; 27..28",
  ])
  internal fun `right number is found`(input: String, @ConvertWith(IntRangeConverter::class) range: IntRange,
                                       @ConvertWith(IntRangeConverter::class) expectedRange: IntRange) {
    SnailFishNumber(input, range).right() shouldBe SnailFishNumber(input, expectedRange)
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[1,2];     1..1;  [9,[8,7]];     4..4;  [9,2];      1..1",
    "[[1,2],3]; 7..7;  [[1,9],[8,5]]; 4..4;  [[1,2],12]; 7..8",
  ])
  internal fun `number is added`(number1:        String, @ConvertWith(IntRangeConverter::class) range1:        IntRange,
                                 number2:        String, @ConvertWith(IntRangeConverter::class) range2:        IntRange,
                                 expectedNumber: String, @ConvertWith(IntRangeConverter::class) expectedRange: IntRange) {
    (SnailFishNumber(number1, range1) add SnailFishNumber(number2, range2)) shouldBe SnailFishNumber(expectedNumber, expectedRange)
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[13,2];     1..2;  [[6,7],2];     0..8",
    "[[1,16],3]; 4..5;  [[1,[8,8]],3]; 0..12",
  ])
  internal fun `number is split`(number:         String, @ConvertWith(IntRangeConverter::class) range:         IntRange,
                                 expectedNumber: String, @ConvertWith(IntRangeConverter::class) expectedRange: IntRange) {
    SnailFishNumber(number, range).split() shouldBe SnailFishNumber(expectedNumber, expectedRange)
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[1,16],3];                         1..6;    [0,3];                          1..1",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,12]]]]]; 7..11;   [[3,[2,0]],[9,[5,[4,[3,12]]]]]; 7..7",
  ])
  internal fun `replaces pair by 0`(number:         String, @ConvertWith(IntRangeConverter::class) range:         IntRange,
                                    expectedNumber: String, @ConvertWith(IntRangeConverter::class) expectedRange: IntRange) {
    SnailFishNumber(number, range).replaceByZero() shouldBe SnailFishNumber(expectedNumber, expectedRange)
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                  4..8",
    "[7,[6,[5,[4,[3,2]]]]];                 12..16",
    "[[6,[5,[4,[3,2]]]],1];                 10..14",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; 10..14",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,12]]]]];    24..29",
  ])
  internal fun completes(input: String, @ConvertWith(IntRangeConverter::class) range: IntRange) {
    SnailFishNumber(input, range).complete() shouldBe SnailFishNumber(input)
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[[[[9,8],1],2],3],4];                   4..8;   [[[[0,9],2],3],4];                  0..16",
    "[7,[6,[5,[4,[3,2]]]]];                  12..16;  [7,[6,[5,[7,0]]]];                  0..16",
    "[[6,[5,[4,[3,2]]]],1];                  10..14;  [[6,[5,[7,0]]],3];                  0..16",
    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]];  10..14;  [[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]];  0..32",
    "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]];      24..28;  [[3,[2,[8,0]]],[9,[5,[7,0]]]];      0..28",
    "[[[[0,7],4],[7,[[8,4],9]]],[1,1]];      16..20;  [[[[0,7],4],[15,[0,13]]],[1,1]];    0..30",
  ])
  internal fun `number is exploded`(number:         String, @ConvertWith(IntRangeConverter::class) range:         IntRange,
                                    expectedNumber: String, @ConvertWith(IntRangeConverter::class) expectedRange: IntRange) {
    SnailFishNumber(number, range).explode() shouldBe SnailFishNumber(expectedNumber, expectedRange)
  }

  @Test
  internal fun `number is reduced`() {
    val number         = SnailFishNumber("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")
    val expectedResult = SnailFishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
    number.reduce() shouldBe expectedResult
  }

  @Test
  internal fun `list 1 of numbers are added correctly`() {
    val input = listOf(
      "[1,1]",
      "[2,2]",
      "[3,3]",
      "[4,4]"
    ).map { SnailFishNumber(it) }
    val result = input.drop(1).fold(input.first()) { sum, number -> sum + number }
    result shouldBe SnailFishNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]")
  }

  @Test
  internal fun `list 2 of numbers are added correctly`() {
    val input = listOf(
      "[1,1]",
      "[2,2]",
      "[3,3]",
      "[4,4]",
      "[5,5]"
    ).map { SnailFishNumber(it) }
    val result = input.drop(1).fold(input.first()) { sum, number -> sum + number }
    result shouldBe SnailFishNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]")
  }

  @Test
  internal fun `list 3 of numbers are added correctly`() {
    val input = listOf(
      "[1,1]",
      "[2,2]",
      "[3,3]",
      "[4,4]",
      "[5,5]",
      "[6,6]"
    ).map { SnailFishNumber(it) }
    val result = input.drop(1).fold(input.first()) { sum, number -> sum + number }
    result shouldBe SnailFishNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]")
  }

  @Test
  internal fun `list 4 of numbers are added correctly`() {
    val input = listOf(
      "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
      "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
      "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
      "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
      "[7,[5,[[3,8],[1,4]]]]",
      "[[2,[2,2]],[8,[8,1]]]",
      "[2,9]",
      "[1,[[[9,3],9],[[9,0],[0,7]]]]",
      "[[[5,[7,4]],7],1]",
      "[[[[4,2],2],6],[8,7]]",
    ).map { SnailFishNumber(it) }
    val result = input.reduce { sum, number -> sum + number }
    result shouldBe SnailFishNumber("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
  }

  @ParameterizedTest
  @CsvSource(delimiter = ';', value = [
    "[[1,2],[[3,4],5]];                                     143",
    "[[[[0,7],4],[[7,8],[6,0]]],[8,1]];                     1384",
    "[[[[1,1],[2,2]],[3,3]],[4,4]];                         445",
    "[[[[3,0],[5,3]],[4,4]],[5,5]];                         791",
    "[[[[5,0],[7,4]],[5,5]],[6,6]];                         1137",
    "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]; 3488"
  ])
  internal fun `magnitude is computed`(number: String, expectedMagnitude: Long) {
    SnailFishNumber(number).magnitude() shouldBe expectedMagnitude
  }

  @Test
  internal fun `cartesian product with self is produced as expected`() {
    listOf(1, 2, 3).lazyCartesianProduct().toList() shouldBe listOf(1 to 1, 1 to 2, 1 to 3,
                                                                    2 to 1, 2 to 2, 2 to 3,
                                                                    3 to 1, 3 to 2, 3 to 3)
  }

  @Test
  internal fun `cartesian product without self is produced as expected`() {
    listOf(1, 2, 3).lazyCartesianProduct(false).toList() shouldBe listOf(1 to 2, 1 to 3,
                                                                         2 to 1, 2 to 3,
                                                                         3 to 1, 3 to 2)
  }

  internal class IntRangeConverter: ArgumentConverter {
    override fun convert(source: Any?, context: ParameterContext): Any? {
      require(source is String?) { "The argument should be a string: $source" }
      if (source == null) {
        return null
      }
      val (from, to) = """(\d+)\.\.(\d+)""".toRegex().matchEntire(source)?.destructured ?: error("invalid range: $source")
      return from.toInt()..to.toInt()
    }
  }
}