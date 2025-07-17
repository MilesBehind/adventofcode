package de.smartsteuer.frank.katas

typealias CsvLine = String
typealias CsvCell = String

fun toTable(csvLines: Iterable<CsvLine>): Iterable<String> {
  if (!csvLines.iterator().hasNext()) return emptyList()
  val csvCells: List<List<CsvCell>> = csvLines.map { it.parse() }
  val columnWidths: List<Int> = csvCells.first().indices.map { columnIndex ->
    csvCells.maxOf { it[columnIndex].length }
  }
  return listOf(formatLine(csvCells.first(), columnWidths)) +
         formatDivider(columnWidths) +
         csvCells.drop(1).map { formatLine(it, columnWidths) }
}

internal fun formatLine(cells: List<String>, widths: List<Int>): String =
  cells.zip(widths).joinToString(separator = "|", postfix = "|") { (cell, width) ->
    cell.padEnd(width)
  }

internal fun formatDivider(widths: List<Int>): String =
  widths.joinToString("+") { "-".repeat(it) } + "+"

internal fun CsvLine.parse(): List<CsvCell> =
  this.split(';').map { it.trim() }

fun main() {
  val csv = listOf(
    "Name;Street;City;Age",
    "Peter Pan;Am Hang 5;12345 Einsam;42",
    "Maria Schmitz;Kölner Straße 45;50123 Köln;43",
    "Paul Meier;Münchener Weg 1;87654 München;65",
  )
  val asciiTableLines = toTable(csv)
  println(asciiTableLines.joinToString("\n"))
}