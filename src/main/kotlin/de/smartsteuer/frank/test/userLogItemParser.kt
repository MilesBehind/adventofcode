@file:Suppress("SameParameterValue")

package de.smartsteuer.frank.test

import java.net.URL
import java.time.Year

fun main() {
  val filings = findDistinctFilings(linesSequence("/exported/tables/user2_user_log_item_tags.csv"))
  println(filings.joinToString(separator = "\n"))
}

private fun findDistinctFilings(lines: Sequence<String>): Set<Filing> =
  lines.fold(emptySet()) { filings, line ->
    filings + parseFiling(line)
  }

private fun parseFiling(line: String): Filing =
  if (" -tt " in line) parseNewFilingFormat(line) else parseOldFilingFormat(line)

private fun parseOldFilingFormat(line: String): Filing {
  val parts = line.split(" ")
  val (_, environment, year, filingType, caseType) = parts
  val assessmentType = if (parts[5].startsWith("et")) if (parts.size > 6) parts[6] else "???" else parts[5]
  val transferTicket = if (parts[5].startsWith("et")) parts[5] else "???"
  if (transferTicket == "???") println(line)
  //if (assessmentType !in listOf("zv", "gv", "gv50")) println(line)
  return Filing(
    environment    = Environment.byName(environment),
    year           = Year.of(year.toInt()),
    filingType     = FilingType.byName(filingType),
    caseType       = CaseType.byName(caseType),
    assessmentType = AssessmentType.byName(assessmentType)
  )
}

private fun parseNewFilingFormat(line: String): Filing {
  // abgabe2 prod 2018 ELSTER-I EST -tt et3516ef8z526p5hw2x5tqg9vtic5ws9 -va zv -idm 76513954204 -idf 75023671499 -nm Daniel -nf Jana
  val parts = line.split(" ")
  val (_, environment, year, filingType, caseType) = parts
  val assessmentType = parts[parts.indexOf("-va") + 1]
  val transferTicket = parts[parts.indexOf("-tt") + 1]
  if (!transferTicket.startsWith("et")) println(line)
  //if (assessmentType !in listOf("zv", "gv", "gv50")) println(line)
  return Filing(
    environment    = Environment.byName(environment),
    year           = Year.of(year.toInt()),
    filingType     = FilingType.byName(filingType),
    caseType       = CaseType.byName(caseType),
    assessmentType = AssessmentType.byName(assessmentType)
  )
}

private data class Filing(val environment:    Environment,
                          val year:           Year,
                          val filingType:     FilingType,
                          val caseType:       CaseType,
                          val assessmentType: AssessmentType)

private enum class FilingType {
  ELSTER_I, ELSTER_II;

  companion object {
    fun byName(name: String) = when (name) {
      "ELSTER-I"             -> ELSTER_I
      "ELSTER-II", "elster2" -> ELSTER_II
      else                   -> throw IllegalArgumentException("unknown filing type '$name'")
    }
  }
}

private enum class Environment {
  PROD;

  companion object {
    fun byName(name: String) = when (name) {
      "prod", "online" -> PROD
      else             -> throw IllegalArgumentException("unknown environment '$name'")
    }
  }
}

enum class CaseType {
  EST, EUER, EUER_PV, GEWST, UST, GES, GES_E, GES_V, UST_GES_AND_GES_E;

  companion object {
    @JvmStatic
    fun byName(name: String) = when (name) {
      "EST", "ESt"        -> EST
      "EÜR"               -> EUER
      "EÜR_PV"            -> EUER_PV
      "GEWST"             -> GEWST
      "UST"               -> UST
      "GES"               -> GES
      "GES_E"             -> GES_E
      "GES_V"             -> GES_V
      "UST_GES_AND_GES_E" -> UST_GES_AND_GES_E
      else                -> throw IllegalArgumentException("unknown case type '$name'")
    }
  }
}

enum class AssessmentType {
  ZV, GV, GV50, UNKNOWN;

  companion object {
    fun byName(name: String) = when (name) {
      "zv"   -> ZV
      "gv"   -> GV
      "gv50" -> GV50
      else   -> UNKNOWN
    }
  }
}

private fun resource(path: String): URL? = object {}.javaClass.getResource(path)

private fun linesSequence(path: String): Sequence<String> =
  resource(path)?.openStream()?.reader()?.buffered(20_000)?.let { reader ->
    generateSequence { reader.readLine() }
  } ?: emptySequence()
