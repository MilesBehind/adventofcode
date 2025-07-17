package de.smartsteuer.frank.katas

data class MorseCode(val char: Char, val code: String) {
  @Suppress("unused")
  companion object {
    val codes = arrayOf (
      MorseCode('A', ".-"),
      MorseCode('Ä', ".-.-"),
      MorseCode('B', "-..."),
      MorseCode('C', "-.-."),
      MorseCode('D', "-.."),
      MorseCode('E', "."),
      MorseCode('F', "..-."),
      MorseCode('G', "--."),
      MorseCode('H', "...."),
      MorseCode('I', ".."),
      MorseCode('J', ".---"),
      MorseCode('K', "-.-"),
      MorseCode('L', ".-.."),
      MorseCode('M', "--"),
      MorseCode('N', "-."),
      MorseCode('O', "---"),
      MorseCode('Ö', "---."),
      MorseCode('P', ".--."),
      MorseCode('Q', "--.-"),
      MorseCode('R', ".-."),
      MorseCode('S', "..."),
      MorseCode('T', "-"),
      MorseCode('U', "..-"),
      MorseCode('Ü', "..--"),
      MorseCode('V', "...-"),
      MorseCode('W', ".--"),
      MorseCode('X', "-..-"),
      MorseCode('Y', "-.--"),
      MorseCode('Z', "--.."),
      MorseCode('0', "-----"),
      MorseCode('1', ".----"),
      MorseCode('2', "..---"),
      MorseCode('3', "...--"),
      MorseCode('4', "....-"),
      MorseCode('5', "....."),
      MorseCode('6', "-...."),
      MorseCode('7', "--..."),
      MorseCode('8', "---.."),
      MorseCode('9', "----."),
      MorseCode(',', "--..--"),
      MorseCode(';', "-.-.--"),
      MorseCode('/', "-..-."),
      MorseCode('!', "-.-.--"),
      MorseCode('?', "..--.."),
      MorseCode('.', ".-.-.-"),
      MorseCode('(', "-.--."),
      MorseCode(')', "-.--.-")
    )
    const val LONG_LENGTH            = 3
    const val SHORT_LENGTH           = 1
    const val CHARACTER_PAUSE_LENGTH = 3
    const val WORD_PAUSE_LENGTH      = 7
  }
}




object MorseCodeTranslator {
  private val charToCode     = MorseCode.codes.associateBy { it.char }
  private val codeToChar     = MorseCode.codes.associateBy { it.code }
  private val characterPause = " ".repeat(1)
  private val wordPause      = " ".repeat(3)

  fun format(message: String): String =
    message.uppercase().map { character ->
      val code = charToCode[character]
      when {
        code != null     -> code.code + characterPause
        character == ' ' -> wordPause
        else             -> ""
      }
    }.joinToString(separator = "")

  fun parse(encodedMessage: String): String =
    encodedMessage.split(wordPause).joinToString(separator = " ") { word ->
      word.split(characterPause).mapNotNull { code ->
        codeToChar[code]?.char
      }.joinToString(separator = "")
    }
}

fun main() {
  formatAndParse("Hello World!")
  formatAndParse("Steuererklärungen macht man mit smartsteuer!")
}

private fun formatAndParse(message: String) {
  println(message)
  val encoded = MorseCodeTranslator.format(message)
  println(encoded)
  val decoded = MorseCodeTranslator.parse(encoded)
  println(decoded)
}
