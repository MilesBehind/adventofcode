package de.smartsteuer.frank.katas

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class NextBiggerNumberWithSameDigitsTest {

  @Test
  fun `return -1 if no bigger number with same digits can be found`() {
    assertSoftly {
      listOf(9, 111, 531, 2000).forEach { number ->
        nextBiggerNumber(number.toLong()) shouldBe -1
      }
    }
  }

  @Test
  fun `returns next bigger number with same digits`() {
    assertSoftly {
      mapOf(          12 to             21,
                     144 to            414,
                     414 to            441,
                     513 to            531,
                    2017 to           2071,
                    2071 to           2107,
                    2073 to           2307,
                    2003 to           2030,
                    3421 to           4123,
               123456789 to      123456798,
               123456798 to      123456879,
               123456879 to      123456897,
               123456897 to      123456978,
               123456978 to      123456987,
               123456987 to      123457689,
          59884848459853 to 59884848483559).forEach { (number, expected) ->
        nextBiggerNumber(number.toLong()) shouldBe expected
      }
    }
  }

  private val bigNumbersAndSolutions = mapOf(
    4487348059342281064 to 4487348059342281406,
    5264697331079988794 to 5264697331079988947,
    4836962602801608670 to 4836962602801608706,
    5575527624477082768 to 5575527624477082786,
    1731101987120282856 to 1731101987120282865,
    5790612044543982962 to 5790612044543986229,
    8453220170391599176 to 8453220170391599617,
    2301358371142031640 to 2301358371142034016,
    1202941720011261465 to 1202941720011261546,
     163310593849708539 to  163310593849708593,
     891013708566263941 to  891013708566264139,
    4547902452352571276 to 4547902452352571627,
     653790959877352981 to  653790959877358129,
     629423724638785043 to  629423724638785304,
      20688402822707886 to   20688402822708678,
    8429641995494743965 to 8429641995494745369,
    3879625994516637245 to 3879625994516637254,
    2404661289926919024 to 2404661289926919042,
     521485678660412109 to  521485678660412190,
    1025157650176485676 to 1025157650176485766,
     435086183735322672 to  435086183735322726,
    1728929950065163255 to 1728929950065163525,
    1887897378061786247 to 1887897378061786274,
    8546517087692774768 to 8546517087692774786,
    7238487438438763468 to 7238487438438763486,
      32533751047131507 to   32533751047131570,
    1341057610507885110 to 1341057610508011578,
    6084335180587349497 to 6084335180587349749,
    1594719769385881090 to 1594719769385881900,
    4734848162852056557 to 4734848162852056575,
    5077476167881244817 to 5077476167881244871,
    2393365401523882915 to 2393365401523882951,
    5339467079049273782 to 5339467079049273827,
    8235918170510001096 to 8235918170510001609,
    1971929105470298841 to 1971929105470412889,
    2873436414564608226 to 2873436414564608262,
    6523565115981928135 to 6523565115981928153,
    4439990732075323078 to 4439990732075323087,
    5061707983962674239 to 5061707983962674293,
    6574050033057591325 to 6574050033057591352,
    5285608030142760932 to 5285608030142762039,
    6790316473740646270 to 6790316473740646702,
    8144158585754626921 to 8144158585754629126,
    7177851497476546125 to 7177851497476546152,
    8253190713297851964 to 8253190713297854169,
    6952877770701128110 to 6952877770701180112,
    4544320692444419953 to 4544320692444431599,
    1727207795209091520 to 1727207795209092015,
    4694278817724882488 to 4694278817724882848,
    1937349602780477387 to 1937349602780477738
  )

  @Test
  fun `many big numbers`() {
    assertSoftly {
      bigNumbersAndSolutions.forEach { (number, expected) ->
        nextBiggerNumber(number) shouldBe expected
      }
    }
  }

  @Test
  fun performance() {
    val warmupRounds = 1000
    val benchmarkRounds = 100_000
    // warmup
    repeat(warmupRounds) {
      bigNumbersAndSolutions.keys.forEach { number -> nextBiggerNumberWithSameDigits(number) }
    }
    // benchmark
    val start = System.currentTimeMillis()
    repeat(benchmarkRounds) {
      bigNumbersAndSolutions.keys.forEach { number -> nextBiggerNumberWithSameDigits(number) }
    }
    val duration = (System.currentTimeMillis() - start).toDuration(DurationUnit.MILLISECONDS)
    println("duration for computing ${benchmarkRounds * bigNumbersAndSolutions.size} solutions: $duration")
    println("which is ${(benchmarkRounds * bigNumbersAndSolutions.size) / duration.inWholeMilliseconds * 1_000} solutions per second")
  }

  @Test
  fun findLargestOrderedEnd() {
    mapOf(12 to    2,
         513 to    3,
        2017 to    7,
        2071 to   71,
        2871 to  871,
        6531 to 6531,
        3421 to  421)
      .forEach { (number, expected) ->
        val digits = number.toString().toCharArray().toList()
        val result = digits.largestOrderedEnd()
        result.joinToString(separator = "").toInt() shouldBe expected
      }
  }
}