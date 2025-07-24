package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.MorseCode.Companion.CHARACTER_PAUSE_LENGTH
import de.smartsteuer.frank.katas.MorseCode.Companion.DASH
import de.smartsteuer.frank.katas.MorseCode.Companion.DOT
import de.smartsteuer.frank.katas.MorseCode.Companion.LONG_LENGTH
import de.smartsteuer.frank.katas.MorseCode.Companion.SHORT_LENGTH
import de.smartsteuer.frank.katas.MorseCode.Companion.WORD_PAUSE_LENGTH
import kotlin.text.dropLast

//----------------------------------------------------
//----- exercise 1: parse and format morse codes -----
//----------------------------------------------------

data class MorseCode(val char: Char, val code: String) {
  @Suppress("unused")
  companion object {
    val codes = arrayOf (
      MorseCode('A', "·–"),
      MorseCode('Ä', "·–·–"),
      MorseCode('B', "–···"),
      MorseCode('C', "–·–·"),
      MorseCode('D', "–··"),
      MorseCode('E', "·"),
      MorseCode('F', "··–·"),
      MorseCode('G', "––·"),
      MorseCode('H', "····"),
      MorseCode('I', "··"),
      MorseCode('J', "·–––"),
      MorseCode('K', "–·–"),
      MorseCode('L', "·–··"),
      MorseCode('M', "––"),
      MorseCode('N', "–·"),
      MorseCode('O', "–––"),
      MorseCode('Ö', "–––·"),
      MorseCode('P', "·––·"),
      MorseCode('Q', "––·–"),
      MorseCode('R', "·–·"),
      MorseCode('S', "···"),
      MorseCode('T', "–"),
      MorseCode('U', "··–"),
      MorseCode('Ü', "··––"),
      MorseCode('V', "···–"),
      MorseCode('W', "·––"),
      MorseCode('X', "–··–"),
      MorseCode('Y', "–·––"),
      MorseCode('Z', "––··"),
      MorseCode('0', "–––––"),
      MorseCode('1', "·––––"),
      MorseCode('2', "··–––"),
      MorseCode('3', "···––"),
      MorseCode('4', "····–"),
      MorseCode('5', "·····"),
      MorseCode('6', "–····"),
      MorseCode('7', "––···"),
      MorseCode('8', "–––··"),
      MorseCode('9', "––––·"),
      MorseCode(',', "––··––"),
      MorseCode(';', "–·–·––"),
      MorseCode('/', "–··–·"),
      MorseCode('!', "–·–·––"),
      MorseCode('?', "··––··"),
      MorseCode('.', "·–·–·–"),
      MorseCode('(', "–·––·"),
      MorseCode(')', "–·––·–")
    )
    const val LONG_LENGTH            = 3
    const val SHORT_LENGTH           = 1
    const val SYMBOL_PAUSE_LENGTH    = 1
    const val CHARACTER_PAUSE_LENGTH = 3
    const val WORD_PAUSE_LENGTH      = 7
    const val DOT                    = '·'
    const val DASH                   = '–'
  }
}

interface MorseCodeTransformer {
  /**
   * Formats a human-readable normal text to a morse-string. For example, the message
   * `"Hello World!"` is formatted to `"···· · ·–·· ·–·· –––    ·–– ––– ·–· ·–·· –·· –·–·–– "`·
   * @param message input message
   * @return resulting morse code«
   */
  fun format(message: String): String

  /**
   * Parses a morse-string to a human-readable normal text. For example, the
   * morse code `"···· · ·–·· ·–·· –––    ·–– ––– ·–· ·–·· –·· –·–·–– "` is
   * parsed to the message `"Hello World!"`.
   * @param encodedMessage input morse code
   * @return resulting message««
   */
  fun parse(encodedMessage: String): String
}

//-------------------------------------------------------------
//----- exercise 2: parse and format morse regular signal -----
//-------------------------------------------------------------

interface RegularMorseSignalTransformer {
  /**
   * Formats a human-readable normal text to a morse-signal.
   * The morse-signal uses these signal lengths:
   *
   * | constant name          | description                                        |
   * |:-----------------------|:---------------------------------------------------|
   * | LONG_LENGTH            | length of a "dah"                                  |
   * | SHORT_LENGTH           | length of a "dit"                                  |
   * | SYMBOL_PAUSE_LENGTH    | length of pause between 2 symbols ("dah" or "dit") |
   * | CHARACTER_PAUSE_LENGTH | length of pause between 2 characters               |
   * | WORD_PAUSE_LENGTH      | length of pause between 2 words                    |
   *
   * For example, the message
   * `"Hello World!"` is formatted to the signal
   * `00000000`.
   * @param message input message
   * @return resulting morse signal
   */
  fun format(message: String): String

  /**
   * Parses a morse-signal ('0' nd '1' for 'off' and 'on') to a human-readable
   * normal text. For example, the
   * morse code `000000` is
   * parsed to the message `"Hello World!"`.
   * @param signal input morse signal
   * @return resulting message
   */
  fun parse(signal: String): String
}


//----------------------------------------------------
//----- solution 1: parse and format morse codes -----
//----------------------------------------------------

object SimpleMorseCodeTransformer: MorseCodeTransformer {
  private val charToCode     = MorseCode.codes.associateBy { it.char }
  private val codeToChar     = MorseCode.codes.associateBy { it.code }
  private val characterPause = " ".repeat(1)
  private val wordPause      = " ".repeat(3)

  override fun format(message: String): String =
    message.uppercase().map { character ->
      val code = charToCode[character]
      when {
        code != null     -> code.code + characterPause
        character == ' ' -> wordPause
        else             -> ""
      }
    }.joinToString(separator = "").trim()

  override fun parse(encodedMessage: String): String =
    encodedMessage.split(wordPause).joinToString(separator = " ") { word ->
      word.split(characterPause).mapNotNull { code ->
        codeToChar[code]?.char
      }.joinToString(separator = "")
    }
}

//------------------------------------------------------
//----- solution 2: parse and format morse signals -----
//------------------------------------------------------

object SimpleRegularMorseSignalTransformer: RegularMorseSignalTransformer {
  private val dot            = "1".repeat(SHORT_LENGTH) + "0"
  private val dash           = "1".repeat(LONG_LENGTH)  + "0"
  private val characterPause = "0".repeat(CHARACTER_PAUSE_LENGTH)
  private val wordPause      = "0".repeat(WORD_PAUSE_LENGTH)
  private val charToCode     = MorseCode.codes.associate { it.char            to it.code.toSignal() }
  private val codeToChar     = MorseCode.codes.associate { it.code.toSignal() to it.char }

  private fun String.toSignal(): String =
    fold(StringBuilder()) { signal, char ->
      when (char) {
        DOT  -> signal.append(dot)
        DASH -> signal.append(dash)
        else -> throw IllegalArgumentException("invalid morse code char: $char")
      }
    }.dropLast(1).toString()

  override fun format(message: String): String =
    message.uppercase().split(' ').joinToString(separator = wordPause) { word ->
      word.map { charToCode[it] ?: "" }.joinToString(separator = characterPause)
    }

  override fun parse(signal: String): String =
    signal.fold(ParseState()) { state, bit -> transition(state, bit) }.let { it.message + codeToChar[it.code.dropLast(1)] }

  private data class ParseState(val offCount: Int = 0, val onCount: Int = 0, val code: String = "", val message: String = "") {
    override fun toString(): String = "off: $offCount, on: $onCount, code: $code, message: $message"
  }

  private fun transition(state: ParseState, bit: Char): ParseState =
    when {
      bit == '1' && state.offCount == CHARACTER_PAUSE_LENGTH      -> state.copy(onCount = 1, offCount = 0, code = "", message = state.message + codeToChar[state.code.dropLast(1)])
      bit == '1' && state.offCount == WORD_PAUSE_LENGTH           -> state.copy(onCount = 1, offCount = 0, code = "", message = state.message + codeToChar[state.code.dropLast(1)] + " ")
      bit == '1' && state.onCount in (0 until LONG_LENGTH)        -> state.copy(onCount = state.onCount + 1)
      bit == '0' && state.onCount == 1                            -> state.copy(onCount = 0, offCount = 1, code = state.code + dot)
      bit == '0' && state.onCount == 3                            -> state.copy(onCount = 0, offCount = 1, code = state.code + dash)
      bit == '0' && state.offCount in (0 until WORD_PAUSE_LENGTH) -> state.copy(offCount = state.offCount + 1)
      else                                                        -> throw IllegalStateException("invalid morse signal. onCount = ${state.onCount}, offCount = ${state.offCount}, code = ${state.code}, ${state.message}: bit = $bit")
    }.also { println("bit = $bit -> next state: $it") }
}



fun main() {
  formatAndParse("abc")
  //formatAndParse("Hello World!")
  //formatAndParse("Steuererklärungen macht man mit smartsteuer!")
}

private fun formatAndParse(message: String) {
  println(message)
  //val encoded = SimpleMorseCodeTransformer.format(message)
  //println(encoded)
  val signal = SimpleRegularMorseSignalTransformer.format(message)
  println(signal)
  val decodedSignal = SimpleRegularMorseSignalTransformer.parse(signal)
  println(decodedSignal)
  //val decoded = SimpleMorseCodeTransformer.parse(encoded)
  //println(decoded)
}
