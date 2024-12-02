package de.smartsteuer.frank.test

import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.fakerConfig
import java.util.*

val fakerRandom = Random(42)

val faker = Faker(
  fakerConfig {
    random = fakerRandom
    locale = "de"
  }
)

data class Customer(val firstName: String, val lastName: String, val isNew: Boolean): Comparable<Customer> {
  override fun compareTo(other: Customer): Int =
    (this.lastName + this.firstName).compareTo(other.lastName + other.firstName)
}

data class Coupon(val name: String, val customers: List<Customer>)

val customers = (1..100).map { Customer(faker.name.firstName(), faker.name.lastName(), fakerRandom.nextBoolean()) }

val coupons = listOf(Coupon("Finanzfluss42", customers.filter { fakerRandom.nextBoolean() }))

fun Customer.hasUsedCoupon(couponName: String) = coupons.first { it.name == couponName }
                                                        .let { this in it.customers }

@Suppress("ReplaceRangeToWithRangeUntil", "ReplaceManualRangeWithIndicesCalls")
fun customersBadWay(): List<String> {
  val found = mutableListOf<Customer>()
  var coupon: Coupon? = null
  for (i in 0 .. coupons.size - 1) {
    if (coupons[i].name == "Finanzfluss42") {
      coupon = coupons[i]
    }
  }
  if (coupon == null) return emptyList()
  for (i in 0 .. customers.size - 1) {
    if (!customers[i].isNew) continue
    for (j in 0..coupon.customers.size - 1) {
      if (coupon.customers[j] == customers[i]) {
        found.add(customers[i])
      }
    }
  }
  found.sort()
  val result = mutableListOf<String>()
  for (i in 0..found.size - 1) {
    result.add(found[i].firstName + " " + found[i].lastName)
  }
  return result.subList(0, 5)
}

@Suppress("ConvertCallChainIntoSequence")
fun customersGoodWay() =
  customers
    .filter { it.isNew }
    .filter { it.hasUsedCoupon("Finanzfluss42") }
    .sorted()
    .map    { it.firstName + " " + it.lastName }
    .take(5)

fun main() {
  println(customersBadWay())
  println(customersGoodWay())
}