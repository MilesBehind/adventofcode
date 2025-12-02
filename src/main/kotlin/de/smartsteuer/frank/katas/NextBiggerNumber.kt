package de.smartsteuer.frank.katas

fun nextBiggerNumber(input: Long): Long {
  val digits: List<Int> = input.toString().toCharArray().map { it.digitToInt() }
  if (digits.windowed(size = 2, step = 1).all { (first, second) -> second <= first }) return -1
  // find the first position/digit that is smaller than the following digit
  val indexOfFirstWrongDigit = digits.size - 2 - digits.reversed()
                                                       .windowed(size = 2, step = 1)
                                                       .indexOfFirst { (first, second) -> second < first }
  val digitsToChange = digits.drop(indexOfFirstWrongDigit + 1)
  val sortedTail = digitsToChange.sorted()
  val digitToReplace = digits[indexOfFirstWrongDigit]
  val newDigit = sortedTail.filter { it > digitToReplace }.min()
  val resultDigits: List<Int> = digits.take(indexOfFirstWrongDigit) + newDigit + (sortedTail - newDigit + digitToReplace).sorted()
  return resultDigits.joinToString(separator = "").toLong()
}

fun main() {
  nextBiggerNumber(3421)
}