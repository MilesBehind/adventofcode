package de.smartsteuer.frank.adventofcode2021.day18

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val numbers = lines("/adventofcode2021/day18/homework.txt").map { SnailFishNumber(it) }
  val sum = numbers.reduce { sum, number -> sum + number }
  val magnitude = sum.magnitude()
  println("magnitude = $magnitude")
  val maximumMagnitude = computeMaximumMagnitude(numbers)
  println("maximum magnitude = $maximumMagnitude")
}

fun computeMaximumMagnitude(numbers: List<SnailFishNumber>): Long =
  numbers.lazyCartesianProduct(false).maxOf { (first, second) -> (first + second).magnitude() }

data class SnailFishNumber(private val number: String, private val range: IntRange = number.indices) {

  operator fun plus(other: SnailFishNumber): SnailFishNumber = SnailFishNumber("[$this,$other]").reduce()

  fun reduce(): SnailFishNumber {
    val pairNestedInsideFourPairs = findFirstPairNestedInsideFourPairs()
    if (pairNestedInsideFourPairs != null) {
      return pairNestedInsideFourPairs.explode().reduce()
    }
    val pairContainingRegularNumberGreater9 = findFirstRegularNumberGreater9()
    if (pairContainingRegularNumberGreater9 != null) {
      return pairContainingRegularNumberGreater9.split().reduce()
    }
    return this
  }

  fun explode(): SnailFishNumber {
    // input:           [[1,[2,3]],4]
    // toExplode:       ____[2,3]____
    // left neighbour:  __1__________
    // left:            _____2_______
    // added left:      __3__________
    // complete:        [[3,[2,3]],4]
    // toExplode:       ____[2,3]____
    // right neighbour: ___________4_
    // right:           _______3_____
    // added right:     ___________7_
    // complete:        [[3,[2,3]],7]
    // replace:         [[3,    0],7]
    // output:          [[3,0],7]
    val afterAddedLeft  = (this.findLeftNeighbour()?.let { leftNeighbour -> leftNeighbour add this.left() } ?: this).complete()
    val toExplode       = afterAddedLeft.findFirstPairNestedInsideFourPairs() ?: error("could not found pair nested in 4 pairs")
    val afterAddedRight = (toExplode.findRightNeighbour()?.let { rightNeighbour -> rightNeighbour add toExplode.right() } ?: toExplode).complete()
    val toReplace       = afterAddedRight.findFirstPairNestedInsideFourPairs() ?: error("could not found pair nested in 4 pairs")
    return toReplace.replaceByZero().complete()
  }

  fun split(): SnailFishNumber {
    val toSplit = number.substring(range).toInt()
    val left    = toSplit / 2
    val right   = toSplit - left
    val split   = "[$left,$right]"
    return SnailFishNumber(number.substring(0 until range.first) + split + number.substring(range.last + 1),
                           (range.first) until range.first + split.length).complete()
  }

  fun findFirstPairNestedInsideFourPairs(): SnailFishNumber? {
    val pairsWith2RegularNumbers: Sequence<MatchResult> = """\[\d+,\d+]""".toRegex().findAll(number.substring(range))
    return pairsWith2RegularNumbers.find { matchResult ->
      number.take(matchResult.range.first - 1).countOpeningBrackets() >= 4 || number.drop(matchResult.range.last + 1).countClosingBrackets() >= 4
    }?.let { matchResult -> SnailFishNumber(number, (matchResult.range.first + range.first)..(matchResult.range.last + range.first)) }
  }

  fun left(): SnailFishNumber {
    val left = """\[(\d+),(\d+)]""".toRegex().matchEntire(number.substring(range))?.groups?.get(1) ?: error("no left number in $this")
    return SnailFishNumber(number, (left.range.first + range.first)..(left.range.last + range.first))
  }

  fun right(): SnailFishNumber {
    val left = """\[(\d+),(\d+)]""".toRegex().matchEntire(number.substring(range))?.groups?.get(2) ?: error("no right number in $this")
    return SnailFishNumber(number, (left.range.first + range.first)..(left.range.last + range.first))
  }

  infix fun add(other: SnailFishNumber): SnailFishNumber {
    val sum = (number.substring(range).toInt() + other.number.substring(other.range).toInt()).toString()
    return SnailFishNumber(number.substring(0 until range.first) + sum + number.substring(range.last + 1), (range.first) until range.first + sum.length)
  }

  fun findFirstRegularNumberGreater9(): SnailFishNumber? {
    val regularNumbers: Sequence<MatchResult> = """\d+""".toRegex().findAll(number)
    return regularNumbers.find { matchResult ->
      matchResult.value.toInt() > 9
    }?.run { SnailFishNumber(number, this.range) }
  }

  fun findLeftNeighbour(): SnailFishNumber? {
    val lastOrNull: MatchResult? = """\d+""".toRegex().findAll(number.take(range.first)).lastOrNull()
    return lastOrNull?.let { SnailFishNumber(number, lastOrNull.range) }
  }

  fun findRightNeighbour(): SnailFishNumber? {
    val firstOrNull: MatchResult? = """\d+""".toRegex().find(number.drop(range.last + 1))
    return firstOrNull?.let { SnailFishNumber(number, (firstOrNull.range.first + range.last + 1)..(firstOrNull.range.last + range.last + 1)) }
  }

  fun replaceByZero(): SnailFishNumber {
    val before = number.take(range.first)
    val after  = number.drop(range.last + 1)
    return SnailFishNumber(before + "0" + after, range.first..range.first)
  }

  fun magnitude(): Long = parseNumber(Input(number)).magnitude()

  fun complete(): SnailFishNumber = SnailFishNumber(number)

  override fun toString() = number.substring(range)
  private fun String.countBrackets() = this.count { it == '[' } - this.count { it == ']' }
  private fun String.countOpeningBrackets() = this.countBrackets()
  private fun String.countClosingBrackets() = -this.countBrackets()
}


sealed interface HierarchicalSnailFishNumber {
  fun magnitude(): Long
}

data class RegularNumber(val number: Int): HierarchicalSnailFishNumber {
  override fun magnitude(): Long = number.toLong()
}

data class PairNumber(val first: HierarchicalSnailFishNumber, val second: HierarchicalSnailFishNumber): HierarchicalSnailFishNumber {
  override fun magnitude(): Long = 3 * first.magnitude() + 2 * second.magnitude()
}

class Input(private val input: String, var index: Int = 0) {
  fun lookAhead(): Char = input[index]

  fun consume(): Char {
    val result = input[index]
    index++
    return result
  }
}

fun parseNumber(input: Input): HierarchicalSnailFishNumber =
  if (input.lookAhead() == '[') {
    require(input.consume() == '[')
    val first = parseNumber(input)
    require(input.consume() == ',')
    val second = parseNumber(input)
    require(input.consume() == ']')
    PairNumber(first, second)
  } else {
    require(input.lookAhead().isDigit())
    RegularNumber(input.consume().digitToInt())
  }

fun <T> Iterable<T>.lazyCartesianProduct(includeItself: Boolean = true): Sequence<Pair<T, T>> =
  sequence {
    forEach { a ->
      filter { b -> includeItself || b !== a }.forEach { b ->
        yield(a to b)
      }
    }
  }
