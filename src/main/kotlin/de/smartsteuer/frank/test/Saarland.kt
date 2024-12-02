@file:Suppress("SameParameterValue")

package de.smartsteuer.frank.test

import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat

private const val SAARLAND_AREA             = 2_569.69  // km^2
private const val DIN_A4_WIDTH              = 210L      // mm
private const val DIN_A4_HEIGHT             = 297L      // mm
private const val DIN_A4_AREA               = DIN_A4_WIDTH * DIN_A4_HEIGHT // mm^2
private const val LINES_PER_DIN_A4_PAGE     = 80L
private const val SQUARE_KM_TO_SQUARE_METER = 1_000L * 1_000L
private const val SQUARE_METER_TO_SQUARE_CM = 100L * 100L
private const val SQUARE_KM_TO_SQUARE_MM    = SQUARE_KM_TO_SQUARE_METER * SQUARE_METER_TO_SQUARE_CM * 100L
private const val SQUARE_M_TO_SQUARE_MM     = SQUARE_METER_TO_SQUARE_CM * 100L
private       val SQUARE_MM_TO_SQUARE_KM    = BigDecimal.ONE.divide(SQUARE_KM_TO_SQUARE_MM.toBigDecimal())
private       val SQUARE_MM_TO_SQUARE_M     = BigDecimal.ONE.divide(SQUARE_M_TO_SQUARE_MM.toBigDecimal())

private const val SOCCER_FIELD_WIDTH  =  68L // m
private const val SOCCER_FIELD_LENGTH = 105L // m
private const val SOCCER_FIELD_AREA   = SOCCER_FIELD_LENGTH * SOCCER_FIELD_WIDTH // m^2

private fun linesToPages(lines: Int): Long = lines / LINES_PER_DIN_A4_PAGE

private fun pagesToSquareKilometer(pages: Long): BigDecimal = (pages * DIN_A4_AREA).toBigDecimal() * SQUARE_MM_TO_SQUARE_KM

private fun pagesToSquareMeter(pages: Long): BigDecimal = (pages * DIN_A4_AREA).toBigDecimal() * SQUARE_MM_TO_SQUARE_M

private fun squareKilometerToSaarland(squareKilometer: BigDecimal): BigDecimal = squareKilometer.divide(SAARLAND_AREA.toBigDecimal(), MathContext.DECIMAL128)

private fun squareMeterToSoccerFields(squareMeter: BigDecimal): BigDecimal = squareMeter.divide(SOCCER_FIELD_AREA.toBigDecimal(), MathContext.DECIMAL128)

private fun printComparisons(label: String, lines: Int) {
  val formatter = DecimalFormat("#,###,##0.000000000000")
  val pages           = linesToPages(lines)
  val squareKilometer = pagesToSquareKilometer(pages)
  val squareMeter     = pagesToSquareMeter(pages)
  val saarlands1      = squareKilometerToSaarland(squareKilometer)
  val soccerFields    = squareMeterToSoccerFields(squareMeter)
  println("$label:")
  println("  lines:            ${formatter.format(lines)}")
  println("  pages:            ${formatter.format(pages)}")
  println("  square meter:     ${formatter.format(squareMeter)}")
  println("  square kilometer: ${formatter.format(squareKilometer)}")
  println("  saarlands:        ${formatter.format(saarlands1)}")
  println("  saarlands %:      ${formatter.format(saarlands1 * 100.toBigDecimal())}")
  println("  soccer fields:    ${formatter.format(soccerFields)}")
  println("  soccer fields %:  ${formatter.format(soccerFields * 100.toBigDecimal())}")
}

fun main() {
  val onseLines            = 144_659
  val onseChangesLines     =   6_590
  val shopLines            =  49_284
  val userServiceOldLines  =  12_269
  val userServiceNewLines  =   3_773
  val oscaLines            =   1_879
  val checkoutServiceLines =  13_308
  val backofficeLines      =  14_889
  val formulaBundleLines   = 286_029
  val newCodeLines         = userServiceOldLines + checkoutServiceLines + oscaLines + backofficeLines
  printComparisons("onse",             onseLines)
  printComparisons("onse changes",     onseChangesLines)
  printComparisons("shop",             shopLines)
  printComparisons("user-service old", userServiceOldLines)
  printComparisons("user-service new", userServiceNewLines)
  printComparisons("checkout service", checkoutServiceLines)
  printComparisons("backoffice",       backofficeLines)
  printComparisons("formula bundle",   formulaBundleLines)
  printComparisons("new code",         newCodeLines)
  println("")
}