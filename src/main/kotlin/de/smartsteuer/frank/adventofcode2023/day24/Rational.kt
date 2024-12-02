@file:Suppress("MemberVisibilityCanBePrivate")

package de.smartsteuer.frank.adventofcode2023.day24

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.absoluteValue

class Rational(val numerator: Long, val denominator: Long): Comparable<Rational> {

  constructor(): this(0, 1)

  fun canonical(): Rational = gcd(numerator.absoluteValue, denominator.absoluteValue).let { gcd -> Rational(numerator / gcd, denominator / gcd) }

  operator fun plus(other: Int): Rational = Rational(numerator + denominator * other, denominator).canonical()

  operator fun plus(other: Long): Rational = Rational(numerator + denominator * other, denominator).canonical()

  operator fun plus(other: Rational): Rational = Rational(numerator * other.denominator + denominator * other.numerator,
                                                          denominator * other.denominator).canonical()

  operator fun minus(other: Int): Rational = Rational(numerator - denominator * other, denominator).canonical()

  operator fun minus(other: Long): Rational = Rational(numerator - denominator * other, denominator).canonical()

  operator fun minus(other: Rational): Rational = Rational(numerator * other.denominator - denominator * other.numerator,
                                                           denominator * other.denominator).canonical()

  operator fun unaryMinus(): Rational = Rational(-numerator, denominator)

  operator fun unaryPlus(): Rational = this

  operator fun dec(): Rational = this - 1

  operator fun inc(): Rational = this + 1

  override operator fun compareTo(other: Rational): Int = (numerator * other.denominator).compareTo(other.numerator * denominator)

  operator fun times(other: Int): Rational = Rational(numerator * other, denominator).canonical()

  operator fun times(other: Long): Rational = Rational(numerator * other, denominator).canonical()

  operator fun times(other: Rational): Rational = Rational(numerator * other.numerator, denominator * other.denominator).canonical()

  operator fun div(other: Int): Rational = this * !other.rational

  operator fun div(other: Long): Rational = this * !other.rational

  operator fun div(other: Rational): Rational = this * !other

  fun abs(): Rational = Rational(if (numerator < 0) -numerator else numerator, if (denominator < 0) -denominator else denominator)

  fun reciprocal(): Rational = !this

  operator fun not(): Rational = Rational(denominator, numerator)

  override fun toString(): String = canonical().run { if (denominator == 1L) numerator.toString() else "$numerator/$denominator" }

  fun toString(digits: Int = 2): String = numerator.toBigDecimal()
    .divide(denominator.toBigDecimal(), MathContext(16, RoundingMode.HALF_UP))
    .setScale(digits, RoundingMode.HALF_UP)
    .toPlainString()

  fun ceil(): Rational = if (numerator < 0) Rational(-((-numerator - 1) / denominator + 1), 1) else Rational((numerator - 1) / denominator + 1, 1)

  fun floor(): Rational = Rational(numerator / denominator, 1)

  fun toDouble(): Double = numerator.toDouble() / denominator.toDouble()

  fun toLong(): Long = numerator / denominator

  fun toInt(): Int = toLong().toInt()

  fun toBigDecimal(): BigDecimal = numerator.toBigDecimal(MathContext.DECIMAL64).divide(denominator.toBigDecimal(MathContext.DECIMAL64), MathContext.DECIMAL64)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Rational
    return numerator * other.denominator == other.numerator * denominator
  }

  override fun hashCode(): Int = 31 * numerator.hashCode() + denominator.hashCode()

  companion object {
    infix fun Int.over(other: Int): Rational = Rational(this.toLong(), other.toLong())

    infix fun Long.over(other: Long): Rational = Rational(this, other)

    private infix fun Long.pow(exponent: Int): Long = if (exponent == 0) 1 else (1..exponent).fold(1) { result, _ -> result * this }

    fun Double.toRational(): Rational = this.toBigDecimal().toPlainString().toRational()

    fun Int.toRational(): Rational = Rational(this.toLong(), 1)

    fun Long.toRational(): Rational = Rational(this, 1)

    fun String.toRational(): Rational {
      val rationalParts = split("/")
      if (rationalParts.size == 2) {
        return Rational(rationalParts[0].trim().toLong(), rationalParts[1].trim().toLong())
      }
      val decimalParts = split(".")
      if (decimalParts.size == 2) {
        return Rational((decimalParts[0] + decimalParts[1]).toLong(), 10L pow decimalParts[1].length)
      }
      return Rational(toLong(), 1)
    }

    val Double.rational get() = this.toRational()

    val Int.rational get() = this.toRational()

    val Long.rational get() = this.toRational()

    val String.rational get() = this.toRational()

    val Double.r get() = this.toRational()

    val Int.r get() = this.toRational()

    val Long.r get() = this.toRational()

    val String.r get() = this.toRational()

    fun abs(rational: Rational) = rational.abs()

    fun min(a: Rational, b: Rational) = a.coerceAtMost(b)

    fun max(a: Rational, b: Rational) = a.coerceAtLeast(b)

    operator fun Int.plus(rational: Rational): Rational = rational + this

    operator fun Int.minus(rational: Rational): Rational = this.toRational() - rational

    operator fun Int.times(rational: Rational): Rational = rational * this

    operator fun Int.div(other: Rational): Rational = this.toRational() * !other

    infix fun Int.percentOf(rational: Rational): Rational = (this over 100) * rational

    operator fun Long.plus(rational: Rational): Rational = rational + this

    operator fun Long.minus(rational: Rational): Rational = this.toRational() - rational

    operator fun Long.times(rational: Rational): Rational = rational * this

    operator fun Long.div(other: Rational): Rational = this.toRational() * !other

    infix fun Long.percentOf(rational: Rational): Rational = (this over 100) * rational

    infix fun Rational.percentOf(rational: Rational): Rational = (this / 100) * rational

    private tailrec fun gcd(a: Long, b: Long): Long = when {
      a < b   -> gcd(b, a)
      b == 0L -> a
      else    -> gcd(b, a % b)
    }
  }
}