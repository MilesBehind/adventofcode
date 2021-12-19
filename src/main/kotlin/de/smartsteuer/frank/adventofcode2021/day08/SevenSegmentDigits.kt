package de.smartsteuer.frank.adventofcode2021.day08

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val notes = lines("/day08/seven-segment-notes.txt").map { Note.fromString(it) }
  val digitLengths = digits.map { it.size }
  val digit1478Count = notes.map { it.digits }
                            .flatten()
                            .count { it.size in setOf(digitLengths[1],
                                                      digitLengths[4],
                                                      digitLengths[7],
                                                      digitLengths[8]) }
  println("number of digits 1, 4, 7, 8 in output: $digit1478Count")
  println ("sum of all notes: ${notes.sumOf { it.decodeNumber() }}")
}

internal typealias Segment = Char
internal typealias Digit   = Int

internal class Note(notes: List<Set<Segment>>, val digits: List<Set<Segment>>) {
  companion object {
    fun fromString(string: String): Note {
      val (notes, digits) = string.split(" | ").map { it.split(" ") }
      return Note(notes.map  { it.toSortedSet() },
                  digits.map { it.toSortedSet() })
    }

  }
  // create lists of digits together with possible candidates by finding notes with required number of enabled segments
  private val digitsAndCandidates: List<DigitAndCandidates> = listOf(
    notes.filter { it.size == 6 },
    notes.filter { it.size == 2 },
    notes.filter { it.size == 5 },
    notes.filter { it.size == 5 },
    notes.filter { it.size == 4 },
    notes.filter { it.size == 5 },
    notes.filter { it.size == 6 },
    notes.filter { it.size == 3 },
    notes.filter { it.size == 7 },
    notes.filter { it.size == 6 },
  ).map { candidates: List<Set<Segment>> -> candidates.map { SevenSegmentDigit(it) }.toSet() }
    .mapIndexed { digit, candidates -> DigitAndCandidates(digit, candidates) }

  fun decodeNumber(): Int = decodeDigits().joinToString("").toInt()

  fun decodeDigits(): List<Int> {
    val decodedNotes:     List<DigitAndSegments> = decodeNotes()
    val segmentsToDigits: Map<String, Digit>     = decodedNotes.associate { it.segments.segments.joinToString("") to it.digit }
    return digits.mapNotNull { segmentsToDigits[it.joinToString("")] }
  }

  fun decodeNotes(): List<DigitAndSegments> {
    val (ambiguous, unambiguous) = digitsAndCandidates.partition { it.isAmbiguous() }
    return decode(ambiguous, unambiguous.map { it.toSegments() })
  }

  private tailrec fun decode(ambiguousDigits: List<DigitAndCandidates>, unambiguousDigits: List<DigitAndSegments>): List<DigitAndSegments> {
    //println("decode(${ambiguousDigits.sortedBy { it.digit }.joinToString("\n       ")}, \n       --\n       ${unambiguousDigits.sortedBy { it.digit }.joinToString("\n       ")})...")
    if (ambiguousDigits.isEmpty()) {
      return unambiguousDigits
    }
    ambiguousDigits.find { !it.isAmbiguous() }?.let { digitAndCandidates ->
      // move candidate from ambiguous to unambiguous and remove segments from all ambiguous candidates
      val ambiguousDigitsWithoutCandidate = (ambiguousDigits - digitAndCandidates).map { it.without(digitAndCandidates.candidates.first()) }
      //println("digit ${digitAndCandidates.digit} is no longer ambiguous, so recurse with it moved from ambiguous to unambiguous and segments removed from ambiguous candidates")
      return decode(ambiguousDigitsWithoutCandidate, unambiguousDigits + digitAndCandidates.toSegments())
    }
    ambiguousDigits.forEach { digitAndCandidates: DigitAndCandidates ->
      unambiguousDigits.forEach { digitAndSegments: DigitAndSegments ->
        digitAndCandidates.candidates.forEach { candidate: SevenSegmentDigit ->
          val commonSegmentCount = (candidate.segments intersect digitAndSegments.segments.segments).size
          val expectedCommonSegmentsCount = commonSegments[digitAndCandidates.digit][digitAndSegments.digit]
          if (commonSegmentCount != expectedCommonSegmentsCount) {
            // remove candidate
            //println("candidate ${candidate} is not valid for digit ${digitAndCandidates.digit}, so recurse with this candidate being removed from this digit")
            return decode(ambiguousDigits - digitAndCandidates + digitAndCandidates.without(candidate), unambiguousDigits)
          }
        }
      }
    }
    error("could not decode notes, remaining ambiguous digits: $ambiguousDigits")
  }
}

@JvmInline
internal value class SevenSegmentDigit(val segments: Set<Segment>) {
  override fun toString(): String = segments.joinToString("")
}

internal data class DigitAndCandidates(val digit: Digit, val candidates: Set<SevenSegmentDigit>) {
  fun isAmbiguous(): Boolean = candidates.size != 1

  fun toSegments(): DigitAndSegments = DigitAndSegments(digit, candidates.first())

  fun without(candidate: SevenSegmentDigit): DigitAndCandidates = DigitAndCandidates(digit, candidates - candidate)

  override fun toString(): String = "$digit -> $candidates"
}

internal data class DigitAndSegments(val digit: Digit, val segments: SevenSegmentDigit) {
  override fun toString(): String = "$digit -> $segments"
}

// map pairs of digits to number of common enabled segments
private val commonSegments = listOf(
  listOf(6, 2, 4, 4, 3, 4, 5, 3, 6, 5),
  listOf(2, 2, 1, 2, 2, 1, 1, 2, 2, 2),
  listOf(4, 1, 5, 4, 2, 3, 4, 2, 5, 4),
  listOf(5, 2, 4, 5, 3, 4, 4, 3, 5, 5),
  listOf(3, 2, 3, 3, 4, 3, 3, 2, 4, 3),
  listOf(4, 1, 3, 4, 3, 5, 5, 2, 5, 5),
  listOf(5, 1, 4, 4, 3, 5, 6, 2, 6, 5),
  listOf(3, 2, 2, 3, 2, 2, 2, 3, 3, 3),
  listOf(6, 2, 5, 5, 4, 5, 6, 3, 7, 6),
  listOf(5, 2, 4, 5, 3, 5, 5, 3, 6, 6),
)

internal val digits: List<Set<Char>> = listOf(
  ( "abcefg".toSet()),  // 0, length = 6 (3 times)
  (     "cf".toSet()),  // 1, length = 2 (unique length)
  (  "acdeg".toSet()),  // 2, length = 5 (3 times)
  (  "acdfg".toSet()),  // 3, length = 5 (3 times)
  (   "bcdf".toSet()),  // 4, length = 4 (unique length)
  (  "abdfg".toSet()),  // 5, length = 5 (3 times)
  ( "abdefg".toSet()),  // 6, length = 6 (3 times)
  (    "acf".toSet()),  // 7, length = 3 (unique length)
  ("abcdefg".toSet()),  // 8, length = 7 (unique length)
  ( "abcdfg".toSet())   // 9, length = 6 (3 times)
)

// acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab
// candidates: (all wire-patterns with mathcing length to correct pattern)
// 0:  abcefg => any of (cefabd cdfgeb cagedb)
// 1:      cf => any of (ab)
// 2:   acdeg => any of (cdfbe gcdfa fbcad)
// 3:   acdfg => any of (cdfbe gcdfa fbcad)
// 4:    bcdf => any of (eafb)
// 5:   abdfg => any of (cdfbe gcdfa fbcad)
// 6:  abdefg => any of (cefabd cdfgeb cagedb)
// 7:     acf => any of (dab)
// 8: abcdefg => any of (acedgfb)
// 9:  abcdfg => any of (cefabd cdfgeb cagedb)

//  which digits contains which segment:
//  0-23-56789
//  0---456-89
//  01234--789
//  --23456-89
//  0-2---6-8-
//  01-3456789
//  0-23-56-89

//    common segment count
//    0   1   2   3   4   5   6   7   8   9
// 0: 6   2   4   4   3   4   5   3   6   5
// 1: 2   2   1   2   2   1   1   2   2   2
// 2: 4   1   5   4   2   3   4   2   5   4
// 3: 5   2   4   5   3   4   4   3   5   5
// 4: 3   2   3   3   4   3   3   2   4   3
// 5: 4   1   3   4   3   5   5   2   5   5
// 6: 5   1   4   4   3   5   6   2   6   5
// 7: 3   2   2   3   2   2   2   3   3   3
// 8: 6   2   5   5   4   5   6   3   7   6
// 9: 5   2   4   5   3   5   5   3   6   6

// algorithm:
// if there is an unsolved digit with only one candidate left => choose candidate, continue
// otherwise: choose first unsolved digit, for each candidate
//   for each other digit with only one candidate
//      compute number of common wires
//   if number of common wires matches expected common wires only for one candidate: choose candidate, remove candidate from all other candidate lists, continue
//   otherwise: remove all unmatched candidates from list
//     if number of common wires matches expected common wires only for one candidate: choose candidate, remove candidate from all other candidate lists, continue
// if any unsolved digits remain: error

// 1: one candidate, next
// 4: one candidate, next
// 7: one candidate, next
// 8: one candidate, next
// 0: common candidates with 1:2-2/1/2, 4:3-4/3/3, remove 2 and 1, choose 3, remove 3 from other candidate lists, next
// 2: common candidates with 1:1-1/1/2, 4:2-3/2/3, remove 3 and 1, choose 2, remove 2 from other candidate lists, next
// 3: common candidates with 1:2-1/2,              remove 1,       choose 2, remove 2 from other candidate lists, next
// 5: one candidate, next
// 6: common candidates with 1:1-2/1,              remove 1,       choose 2, remove 2 from other candidate lists, next
// 9: one candidate, next
// finished

//    candidates                  common count               selected
// 0: cefabd cdfgeb cagedb     => 1:2-212 4:3-433         => cagedb  => remove from 6 and 9
// 1: ab                       =>                            ab
// 2: cdfbe gcdfa fbcad        => 1:1-112 4:2-323         => gcdfa   => remove from 3 and 5
// 3: cdfbe (gcdfa) fbcad      => 1:2-12                  => fbcad   => remove and 5
// 4: eafb                     =>                            eafb
// 5: cdfbe (gcdfa) (fbcad)    =>                         => cdfbe
// 6: cefabd cdfgeb (cagedb)   => 1:1-21                  => cdfgeb  => remove from 9
// 7: dab                      =>                            dab
// 8: acedgfb                  =>                            acedgfb
// 9: cefabd (cdfgeb) (cagedb) =>                            cefabd

// sorted:
// 0: abcdeg
// 1: ab
// 2: acdfg
// 3: abcdf
// 4: abef
// 5: bcdef
// 6: bcdefg
// 7: abd
// 8: abcdefg
// 9: abcdef


// mapping from correct to wrong wires:
// a: d
// b: e
// c: a
// d: f
// e: g
// f: b
// g: c

