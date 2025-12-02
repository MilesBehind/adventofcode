@file:Suppress("KotlinUnreachableCode")

package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.VerbalClockFormatter.Companion.words
import java.time.LocalTime

interface VerbalClockFormatter {
  companion object {
    val words = listOf(
      "fünf", "zehn", "viertel", "zwanzig", "halb",
      "vor", "nach",
      "eins", "zwei", "drei", "vier", "fünf", "sechs", "sieben", "acht", "neun", "zehn", "elf", "zwölf",
      "ein", "Uhr"
    )
  }

  /**
   * This formatter takes a time as a [java.time.LocalTime] and returns a
   * list of indexes into a [word list][words]. When the words at the returned
   * indexes are joined using a space as a separator, then a german verbal
   * representation of the time is created.
   * Seconds are not used for any calculations. Minutes are rounded to the
   * nearest multiple of 5 (with 3 minutes being rounded to 5 and 2 minutes
   * being rounded to 0).
   * This code is inspired by [clock-two](https://www.qlocktwo.com/de-de)
   * @param time the time to format
   * @return a list of indexes into the word list
   */
  fun format(time: LocalTime): List<Int>
}

object GermanVerbalClockFormatter : VerbalClockFormatter {
  private const val HOUR_OFFSET = 6
  private const val AFTER       = 6
  private const val BEFORE      = 5
  private const val FIVE        = 0
  private const val TEN         = 1
  private const val QUARTER     = 2
  private const val TWENTY      = 3
  private const val HALF        = 4
  private       val O_CLOCK     = words.lastIndex

  override fun format(time: LocalTime): List<Int> {
    val minutes    = ((time.minute + 2) / 5) * 5
    val hours      = if (time.hour % 12 == 0) HOUR_OFFSET + 12 else time.hour % 12 + HOUR_OFFSET
    val hour       = if (time.hour % 12 == 1) words.lastIndex - 1 else hours
    val nextHour   = if (time.hour % 12 == 11) HOUR_OFFSET + 12 else (time.hour + 1) % 12 + HOUR_OFFSET
    return when (minutes) {
      0    -> listOf(hour, O_CLOCK)
      5    -> listOf(FIVE, AFTER, hours)
      10   -> listOf(TEN, AFTER, hours)
      15   -> listOf(QUARTER, AFTER, hours)
      20   -> listOf(TWENTY, AFTER, hours)
      25   -> listOf(FIVE, BEFORE, HALF, hours)
      30   -> listOf(HALF, nextHour)
      35   -> listOf(FIVE, AFTER, HALF, nextHour)
      40   -> listOf(TWENTY, BEFORE, nextHour)
      45   -> listOf(QUARTER, BEFORE, nextHour)
      50   -> listOf(TEN, BEFORE, nextHour)
      55   -> listOf(FIVE, BEFORE, nextHour)
      60   -> listOf(nextHour, O_CLOCK)
      else -> throw IllegalArgumentException("unexpected minutes value: $minutes")
    }
  }
}
