package de.smartsteuer.frank.adventofcode2022.day01

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val calories: List<List<Int>> =
    lines("/adventofcode2022/day01/calories.txt").split { it.isBlank() }.map { it.map { string -> string.toInt() } }
  part1(calories)
  part2(calories)
}

private fun part1(calories: List<List<Int>>) {
  println("part 1: top calories = ${computeCalories(calories).max()}")
}

private fun part2(calories: List<List<Int>>) {
  println("part 2: sum of top 3 calories = ${computeCalories(calories).sorted().takeLast(3).sum()}")
}

fun computeCalories(calories: List<List<Int>>): List<Int> = calories.map { it.sum() }

fun <T> List<T>.split(isSplittingElement: (T) -> Boolean): List<List<T>> {
  tailrec fun split(listToSplit: List<T>, groups: List<List<T>>, group: List<T>): List<List<T>> = when {
    listToSplit.isEmpty()                   -> if (group.isNotEmpty()) groups.plus(element = group) else groups
    isSplittingElement(listToSplit.first()) -> split(listToSplit.drop(1), groups.plus(element = group), emptyList())
    else                                    -> split(listToSplit.drop(1), groups, group + listToSplit.first())
  }
  return split(this, emptyList(), emptyList())
}

// split without using recursion
fun <T> List<T>.splitFold(isSplittingElement: (T) -> Boolean): List<List<T>> {
  data class GroupsAndGroup(val groups: List<List<T>> = emptyList(), val group: List<T> = emptyList())
  infix fun List<List<T>>.append(element: List<T>) = plus(element = element)
  return fold(GroupsAndGroup()) { (groups, group), element ->
    if (isSplittingElement(element)) GroupsAndGroup(groups append group)
    else GroupsAndGroup(groups, group + element)
  }.let { (groups, group) ->
    if (group.isNotEmpty()) groups append group
    else groups
  }
}

// split, the old-school way
fun <T> List<T>.splitMutable(isSplittingElement: (T) -> Boolean): List<List<T>> {
  val result = mutableListOf<List<T>>()
  val group  = mutableListOf<T>()
  forEach { element ->
    if (isSplittingElement(element)) {
      result += group.toList()
      group.clear()
    } else {
      group += element
    }
  }
  if (group.isNotEmpty()) {
    result += group
  }
  return result
}