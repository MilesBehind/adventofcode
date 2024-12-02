package de.smartsteuer.frank.test

fun getNameSuffix2(nameMann: String?, nameFrau: String?): String = when {
  nameMann.isNullOrBlank() && nameFrau.isNullOrBlank() -> ""
  nameMann.isNullOrBlank()                             -> "($nameFrau)"
  nameFrau.isNullOrBlank()                             -> "($nameMann)"
  else                                                 -> "($nameMann + $nameFrau)"
}