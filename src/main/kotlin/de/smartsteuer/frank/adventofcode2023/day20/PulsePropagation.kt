package de.smartsteuer.frank.adventofcode2023.day20

import de.smartsteuer.frank.adventofcode2023.day20.Pulse.*
import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
  val lines = lines("/adventofcode2023/day20/modules.txt")
  measureTime { println("part 1: ${part1(parseModules(lines))}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(parseModules(lines))}") }.also { println("part 2 took $it") }
}

internal fun part1(communication: Communication): Long =
  communication.pushButton1000Times()

internal fun part2(communication: Communication): Long =
  communication.pushButtonUntilRxReceivesLowPulse()

internal enum class Pulse { LOW, HIGH }

internal typealias ModuleName = String

internal data class Communication(val modules: List<Module>) {
  private val modulesByNames = modules.associateBy { it.name }
  private val pulseCounts    = modules.associate { it.name to (0L to 0L) }.toMutableMap()
  // These need LOW pulses in order to send a HIGH pulse to the direct sender of rx.
  // The direct sender will send a LOW pulse to rx if it receives a HIGH pulse from all senders, that send pulses to this direct sender.
  // All these indirect senders receive low triggers at different cycles. Find these cycles and compute the lcm of these cycles.
  private val indirectSendersToButtonPressCountsWithLowPulse = findIndirectSendersForRx().associateWith { mutableListOf<Int>() }
  private var resultOfPart2: Long = 0

  private data class Signal(val sender: ModuleName, val pulse: Pulse, val destinations: List<ModuleName>)

  fun pushButton1000Times(): Long {
    repeat(1_000) { pushButton() }
    val (low, high) = pulseCounts.values
      .fold(0L to 0L) { acc, value -> (acc.first + value.first) to (acc.second + value.second) }
    return low * high
  }

  fun pushButtonUntilRxReceivesLowPulse(): Long {
    var buttonPresses = 1
    while (resultOfPart2 <= 0) {
      pushButton(buttonPresses)
      buttonPresses++
    }
    return resultOfPart2
  }

  fun pushButton(count: Int = 0) {
    tailrec fun processPulses(signals: List<Signal>) {
      if (signals.isEmpty()) return
      val (sender, pulse, destinations) = signals.first()
      //printPulses(sender, pulse, destinations)
      destinations.forEach { destination ->
        val (low, high) = pulseCounts.getOrDefault(destination, 0L to 0L)
        val (addLow, addHigh) = if (pulse == LOW) 1 to 0 else 0 to 1
        pulseCounts[destination] = (low + addLow) to (high + addHigh)
      }
      if (pulse == LOW && indirectSendersToButtonPressCountsWithLowPulse.isNotEmpty()) {
        indirectSendersToButtonPressCountsWithLowPulse.entries.forEach { (indirectSenderName, buttonPresses) ->
          if (indirectSenderName in destinations) {
            buttonPresses.add(count)
          }
        }
        if (indirectSendersToButtonPressCountsWithLowPulse.values.all { it.isNotEmpty() }) {
          resultOfPart2 = lcm(indirectSendersToButtonPressCountsWithLowPulse.values.map { it.first().toLong() })
        }
      }
      val newSignals = destinations.filter { it in modulesByNames }.map { destination ->
        val (newPulse, newDestinations) = modulesByNames.getValue(destination).receive(sender, pulse)
        Signal(destination, newPulse, newDestinations)
      }
      processPulses(signals.drop(1) + newSignals.filter { it.destinations.isNotEmpty() })
    }
    processPulses(listOf(Signal("button", LOW, listOf("broadcaster"))))
  }

  fun findIndirectSendersForRx(): List<ModuleName> {
    val directSenders = modules.filter { module -> "rx" in module.destinationModules }
    return modules.filter { module -> directSenders.any { sender -> sender.name in module.destinationModules } }.map { it.name }
  }

  // private fun printPulses(sender: ModuleName, pulse: Pulse, destinations: List<ModuleName>) {
  //   destinations.forEach { destination ->
  //     println("$sender -$pulse -> $destination")
  //   }
  // }
}

internal sealed interface Module {
  val name: ModuleName
  val destinationModules: List<ModuleName>
  fun receive(sender: ModuleName, pulse: Pulse): Pair<Pulse, List<ModuleName>>
}

internal data class FlipFlop(override val name: ModuleName, override val destinationModules: List<ModuleName>): Module {
  private var state = false
  override fun receive(sender: ModuleName, pulse: Pulse): Pair<Pulse, List<ModuleName>> =
    when (pulse) {
      HIGH -> HIGH to emptyList()
      LOW  -> {
        state = !state
        (if (state) HIGH else LOW) to destinationModules
      }
    }
}

internal data class Conjunction(override val name: ModuleName, override val destinationModules: List<ModuleName>,
                                val watchedInputs: List<ModuleName>): Module {
  private val receivedPulses = watchedInputs.associateWith { LOW }.toMutableMap()
  override fun receive(sender: ModuleName, pulse: Pulse): Pair<Pulse, List<ModuleName>> {
    receivedPulses[sender] = pulse
    return (if (receivedPulses.values.all { it == HIGH }) LOW else HIGH) to destinationModules
  }
}

internal data class Broadcaster(override val name: ModuleName, override val destinationModules: List<ModuleName>): Module {
  init { require(name == "broadcaster") }
  override fun receive(sender: ModuleName, pulse: Pulse): Pair<Pulse, List<ModuleName>> {
    return pulse to destinationModules
  }
}

internal fun parseModules(lines: List<String>): Communication {
  val modules = lines.map { line ->
    val (name, destinationString) = line.split(" -> ")
    val destinations = destinationString.split(",").map { it.trim() }
    when {
      name.startsWith('%') -> FlipFlop(name.drop(1), destinations)
      name.startsWith('&') -> Conjunction(name.drop(1), destinations, emptyList())
      else                 -> Broadcaster(name, destinations)
    }
  }
  val modifiedModules = modules.map { module ->
    if (module is Conjunction) {
      module.copy(watchedInputs = modules.filter { module.name in it.destinationModules }.map { it.name })
    } else module
  }
  return Communication(modifiedModules)
}

internal fun lcm(values: List<Long>): Long =
  values.drop(1).fold(values.first()) { acc, value -> max(acc, lcm(acc, value)) }

internal fun lcm(number1: Long, number2: Long): Long =
  if (number1 == 0L || number2 == 0L) 0 else abs(number1 * number2) / gcd(number1, number2)

@Suppress("DuplicatedCode")
internal tailrec fun gcd(number1: Long, number2: Long): Long {
  if (number1 == 0L || number2 == 0L) return number1 + number2
  val absNumber1 = abs(number1)
  val absNumber2 = abs(number2)
  val biggerValue = max(absNumber1, absNumber2)
  val smallerValue = min(absNumber1, absNumber2)
  return gcd(biggerValue % smallerValue, smallerValue)
}

