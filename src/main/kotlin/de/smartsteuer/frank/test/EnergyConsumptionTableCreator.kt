package de.smartsteuer.frank.test

import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter

fun main() {
  val year = Year.of(2020)
  val file = createEnergyConsumptionCsv(year)
  println("created file ${file.name} for year $year")
  val wattsPerModule      =    455 // watts
  val modulesCountEast    =      9
  val elevationEast       =     10 // degrees
  val wattsEast           = modulesCountEast * wattsPerModule
  val modulesCountWest    =      9
  val elevationWest       =     10 // degrees
  val wattsWest           = modulesCountWest * wattsPerModule
  val modulesCountSouth   =     10
  val elevationSouth      =     25 // degrees
  val wattsSouth          = modulesCountSouth * wattsPerModule
  val costWithoutDiscount = 18_500 // euros
  val discount            =     10 // percent
  val batteryCapacity     = 10_200 // watt-hours
  val discountedPrice     = (costWithoutDiscount * (1 - discount / 100.0)).toInt()
  println("modulesCountEast:    $modulesCountEast")
  println("elevationEast:       $elevationEast")
  println("wattsEast:           $wattsEast")
  println("modulesCountWest:    $modulesCountWest")
  println("elevationWest:       $elevationWest")
  println("wattsWest:           $wattsWest")
  println("modulesCountSouth:   $modulesCountSouth")
  println("elevationSouth:      $elevationSouth")
  println("wattsSouth:          $wattsSouth")
  println("costWithoutDiscount: $costWithoutDiscount")
  println("discount:            $discount")
  println("batteryCapacity:     $batteryCapacity")
  println("discountedPrice:     $discountedPrice")
}

private fun createEnergyConsumptionCsv(year: Year): File {
  val formatter = DateTimeFormatter.ofPattern("yyyyMMdd:HH")
  return File("verbrauch_$year.csv").also {
    it.bufferedWriter(Charsets.UTF_8).use { writer ->
      writer.write(""""Datetime";"Power"""")
      writer.newLine()
      energyConsumption().forEach { (dateTime, energyConsumption) ->
        writer.write(""""${dateTime.format(formatter)}";$energyConsumption""")
        writer.newLine()
      }
    }
  }
}

private fun energyConsumption(): Sequence<Pair<LocalDateTime, Int>> =
  dateTimeSequence().map { dateTime ->
    val hour = dateTime.hour
    val day  = dateTime.dayOfYear
    dateTime to (energyPerHourOther[hour] + energyPerDayHeatPump[day - 1] / 24).toInt()
  }

private val averageTemperaturesPerMonth = listOf(2.3, 2.8, 5.4,   9.3, 13.2, 16.7,   18.5, 18.1, 14.9,   10.8, 6.4, 3.5)
private val averageTemperaturePerDay    = dateSequence().map { day ->
  val currentMonthOrdinal                     = day.month.ordinal
  val nextMonthOrdinal                        = (day.month + 1).ordinal
  val distanceToFirstOfCurrentMonth           = day.dayOfMonth - 1
  val averageTemperatureCurrentMonth          = averageTemperaturesPerMonth[currentMonthOrdinal]
  val averageTemperatureNextMonth             = averageTemperaturesPerMonth[nextMonthOrdinal]
  val normalizedDistanceToFirstOfCurrentMonth = distanceToFirstOfCurrentMonth.toDouble() / day.month.length(true)
  averageTemperatureCurrentMonth * (1.0 - normalizedDistanceToFirstOfCurrentMonth) + averageTemperatureNextMonth * normalizedDistanceToFirstOfCurrentMonth
}
private const val HEAT_PUMP_MAX_TEMPERATURE = 15.0
private val percentEnergyPerDayHeatPump = averageTemperaturePerDay.map { temperature -> (HEAT_PUMP_MAX_TEMPERATURE - temperature).coerceAtLeast(0.0) }
                                                                  .let { temperatures -> temperatures.map { it / temperatures.sum() } }
private val percentEnergyPerHourOther = listOf(2, 2, 2, 2, 2, 2,    2, 2, 3, 3, 3, 3,    10, 4, 3, 3, 3, 3,    8, 6, 6, 6, 6, 6).let { list ->
  list.map { it.toDouble() / list.sum() }
}

private const val WATT_HOURS_HEAT_PUMP = 3_000_000.0
private const val WATT_HOURS_OTHER     = 3_200_000.0

private val energyPerDayHeatPump = percentEnergyPerDayHeatPump.map { percent -> percent * WATT_HOURS_HEAT_PUMP }.toList()
private val energyPerHourOther   = percentEnergyPerHourOther.map   { percent -> percent * (WATT_HOURS_OTHER / 365) }

private const val YEAR = 2020

private fun dateSequence(): Sequence<LocalDate> {
  val start = LocalDate.of(YEAR, Month.JANUARY,   1)
  val end   = LocalDate.of(YEAR, Month.DECEMBER, 31)
  return generateSequence(start) { dateTime -> if (dateTime >= end) null else dateTime.plusDays(1) }
}

private fun dateTimeSequence(): Sequence<LocalDateTime> {
  val start = LocalDateTime.of(YEAR, Month.JANUARY,   1,  0, 0, 0)
  val end   = LocalDateTime.of(YEAR, Month.DECEMBER, 31, 23, 0, 0)
  return generateSequence(start) { dateTime -> if (dateTime >= end) null else dateTime.plusHours(1) }
}
