package de.smartsteuer.frank.katas

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CsvTableizerTest {

  private val csv = listOf(
    "Name;Street;City;Age",
    "Peter Pan;Am Hang 5;12345 Einsam;42",
    "Maria Schmitz;Kölner Straße 45;50123 Köln;43",
    "Paul Meier;Münchener Weg 1;87654 München;65",
  )

  @Test
  fun `csv lines can be parsed`() {
    csv[0].parse() shouldBe listOf("Name",          "Street",           "City",          "Age")
    csv[1].parse() shouldBe listOf("Peter Pan",     "Am Hang 5",        "12345 Einsam",  "42")
    csv[2].parse() shouldBe listOf("Maria Schmitz", "Kölner Straße 45", "50123 Köln",    "43")
    csv[3].parse() shouldBe listOf("Paul Meier",    "Münchener Weg 1",  "87654 München", "65")
  }

  @Test
  fun `csv input can be converted to ascii table`() {
    toTable(csv) shouldBe listOf(
      "Name         |Street          |City         |Age|",
      "-------------+----------------+-------------+---+",
      "Peter Pan    |Am Hang 5       |12345 Einsam |42 |",
      "Maria Schmitz|Kölner Straße 45|50123 Köln   |43 |",
      "Paul Meier   |Münchener Weg 1 |87654 München|65 |"
    )
  }
}