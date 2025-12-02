package de.smartsteuer.frank.katas

fun nextBiggerNumberWithSameDigits(n: Long): Long {
  val digits = n.toString().toCharArray().map { it.digitToInt() }
  val largestOrderedEnd = digits.largestOrderedEnd()
  if (largestOrderedEnd.size == digits.size) return -1
  val prefix = digits.dropLast(largestOrderedEnd.size)
  val nextDigit = prefix.last()
  val unchangedPrefix = prefix.dropLast(1)
  val smallestDigitBiggerThanNextDigit = largestOrderedEnd.filter { it > nextDigit }.min()
  val postfixDigits = largestOrderedEnd - smallestDigitBiggerThanNextDigit + nextDigit
  val resultDigits = unchangedPrefix + smallestDigitBiggerThanNextDigit + postfixDigits.sorted()
  return resultDigits.joinToString(separator = "").toLong()
}

internal fun <T: Comparable<T>> List<T>.largestOrderedEnd(): List<T> =
  if (size <= 1) this
  else (lastIndex - 1 downTo 0)
    .firstOrNull { index -> this[index] < this[index + 1] }
    ?.let { index -> takeLast(size - index - 1) }
    ?: this
