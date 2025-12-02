package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.VerbalClockFormatter.Companion.words
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalTime
import kotlin.collections.joinToString

class VerbalTest {

  @Test
  fun `time can be converted to verbal time`() {
    mapOf(
      "00:00:00" to "zwölf Uhr",
      "01:00:00" to "ein Uhr",
      "02:00:00" to "zwei Uhr",
      "03:00:00" to "drei Uhr",
      "04:00:00" to "vier Uhr",
      "05:00:00" to "fünf Uhr",
      "06:00:00" to "sechs Uhr",
      "07:00:00" to "sieben Uhr",
      "08:00:00" to "acht Uhr",
      "09:00:00" to "neun Uhr",
      "10:00:00" to "zehn Uhr",
      "11:00:00" to "elf Uhr",
      "12:00:00" to "zwölf Uhr",
      "13:00:00" to "ein Uhr",
      "14:00:00" to "zwei Uhr",
      "15:00:00" to "drei Uhr",
      "16:00:00" to "vier Uhr",
      "17:00:00" to "fünf Uhr",
      "18:00:00" to "sechs Uhr",
      "19:00:00" to "sieben Uhr",
      "20:00:00" to "acht Uhr",
      "21:00:00" to "neun Uhr",
      "22:00:00" to "zehn Uhr",
      "23:00:00" to "elf Uhr",

      "01:01:00" to "ein Uhr",
      "01:02:00" to "ein Uhr",
      "01:03:00" to "fünf nach eins",
      "01:04:00" to "fünf nach eins",
      "01:05:00" to "fünf nach eins",
      "01:06:00" to "fünf nach eins",
      "01:07:00" to "fünf nach eins",
      "01:08:00" to "zehn nach eins",
      "01:09:00" to "zehn nach eins",
      "01:10:00" to "zehn nach eins",
      "01:11:00" to "zehn nach eins",
      "01:12:00" to "zehn nach eins",
      "01:13:00" to "viertel nach eins",
      "01:14:00" to "viertel nach eins",
      "01:15:00" to "viertel nach eins",
      "01:16:00" to "viertel nach eins",
      "01:17:00" to "viertel nach eins",
      "01:18:00" to "zwanzig nach eins",
      "01:19:00" to "zwanzig nach eins",
      "01:20:00" to "zwanzig nach eins",
      "01:21:00" to "zwanzig nach eins",
      "01:22:00" to "zwanzig nach eins",
      "01:23:00" to "fünf vor halb eins",
      "01:24:00" to "fünf vor halb eins",
      "01:25:00" to "fünf vor halb eins",
      "01:26:00" to "fünf vor halb eins",
      "01:27:00" to "fünf vor halb eins",
      "01:28:00" to "halb zwei",
      "01:29:00" to "halb zwei",
      "01:30:00" to "halb zwei",
      "01:31:00" to "halb zwei",
      "01:32:00" to "halb zwei",
      "01:33:00" to "fünf nach halb zwei",
      "01:34:00" to "fünf nach halb zwei",
      "01:35:00" to "fünf nach halb zwei",
      "01:36:00" to "fünf nach halb zwei",
      "01:37:00" to "fünf nach halb zwei",
      "01:38:00" to "zwanzig vor zwei",
      "01:39:00" to "zwanzig vor zwei",
      "01:40:00" to "zwanzig vor zwei",
      "01:41:00" to "zwanzig vor zwei",
      "01:42:00" to "zwanzig vor zwei",
      "01:43:00" to "viertel vor zwei",
      "01:44:00" to "viertel vor zwei",
      "01:45:00" to "viertel vor zwei",
      "01:46:00" to "viertel vor zwei",
      "01:47:00" to "viertel vor zwei",
      "01:48:00" to "zehn vor zwei",
      "01:49:00" to "zehn vor zwei",
      "01:50:00" to "zehn vor zwei",
      "01:51:00" to "zehn vor zwei",
      "01:52:00" to "zehn vor zwei",
      "01:53:00" to "fünf vor zwei",
      "01:54:00" to "fünf vor zwei",
      "01:55:00" to "fünf vor zwei",
      "01:56:00" to "fünf vor zwei",
      "01:57:00" to "fünf vor zwei",
      "01:58:00" to "zwei Uhr",
      "01:59:00" to "zwei Uhr",

      "00:01:00" to "zwölf Uhr",
      "00:02:00" to "zwölf Uhr",
      "00:03:00" to "fünf nach zwölf",
      "00:05:00" to "fünf nach zwölf",

      "11:50:00" to "zehn vor zwölf",
      "11:58:00" to "zwölf Uhr",
      "23:50:00" to "zehn vor zwölf",
      "23:58:00" to "zwölf Uhr",
    ).forEach { (time, verbalTime) ->
      val verbalIndexes = GermanVerbalClockFormatter.format(LocalTime.parse(time))
      val verbal = verbalIndexes.joinToString(separator = " ") { words[it] }
      verbal shouldBe verbalTime
    }
  }
}