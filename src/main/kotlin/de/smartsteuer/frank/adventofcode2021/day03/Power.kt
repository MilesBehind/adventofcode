package de.smartsteuer.frank.adventofcode2021.day03

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val report = lines("/adventofcode2021/day03/report.txt")
  val (gammaRate: Int, epsilonRate: Int) = computeGammaAndEpsilonRates(report)
  println("gammaRate = $gammaRate, epsilonRate = $epsilonRate, product = ${gammaRate * epsilonRate}")
  val (oxygenRate: Int, co2rate: Int) = computeOxygenAndCo2Rates(report)
  println("oxygenRate = $oxygenRate, co2rate = $co2rate, product = ${oxygenRate * co2rate}")
}

internal data class GammaAndEpsilonRates(val gamma: Int, val epsilon: Int)

internal fun computeGammaAndEpsilonRates(report: List<String>): GammaAndEpsilonRates {
  if (report.isEmpty()) {
    return GammaAndEpsilonRates(0, 0)
  }
  val oneDigitCounts = countOneDigits(report)
  val gammaRateString: String = oneDigitCounts.map { counter -> if (counter > report.size / 2) '1' else '0' }
                                              .joinToString(separator = "")
  val allOnes = (1 shl report.first().length) - 1
  val gamma   = gammaRateString.toInt(radix = 2)
  val epsilon = allOnes xor gamma
  return GammaAndEpsilonRates(gamma, epsilon)
}

internal fun countOneDigits(report: List<String>): List<Int> =
  List(report.first().length) { countOneDigits(report, it) }

internal fun countOneDigits(report: List<String>, index: Int): Int =
  report.count { it[index] == '1' }



internal data class OxygenAndCo2Rates(val oxygen: Int, val co2: Int)

internal fun computeOxygenAndCo2Rates(report: List<String>): OxygenAndCo2Rates {
  if (report.isEmpty()) {
    return OxygenAndCo2Rates(0, 0)
  }
  tailrec fun computeRate(remainingReport: List<String>, index: Int, moreCommonDigit: Char, lessCommonDigit: Char): Int {
    if (remainingReport.size == 1 || index >= remainingReport.first().length) {
      return remainingReport.first().toInt(radix = 2)
    }
    val oneDigitCount = countOneDigits(remainingReport, index)
    val mostCommonDigit = if (oneDigitCount >= remainingReport.size / 2.0) moreCommonDigit else lessCommonDigit
    return computeRate(remainingReport.filter { it[index] == mostCommonDigit }, index + 1, moreCommonDigit, lessCommonDigit)
  }
  return OxygenAndCo2Rates(computeRate(report, 0, '1', '0'),
                           computeRate(report, 0, '0', '1'))
}


/*
internal fun ratesOldSchool(report: List<String>): GammaAndEpsilonRates {
  val digitCount = report.first().length
  val counters   = MutableList(digitCount) {0}
  for (i in report.indices) {
    val line = report[i].toCharArray()
    for (j in line.indices) {
      val digit = line[j]
      if (digit == '1') {
        counters[j]++
      }
    }
  }
  var gamma = 0
  for (i in (counters.size - 1) downTo 0) {
    val counter = counters[i]
    if (counter > report.size / 2) {
      gamma = gamma or 1
    }
    gamma = gamma shl  1
  }
  val allOnes = (1 shl report.first().length) - 1
  val epsilon = allOnes xor gamma
  return GammaAndEpsilonRates(gamma, epsilon)
}
*/