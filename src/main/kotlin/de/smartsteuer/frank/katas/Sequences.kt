package de.smartsteuer.frank.katas

import de.smartsteuer.frank.katas.BlockSequence.blocks
import de.smartsteuer.frank.katas.Sequences.BufferedSequence
import kotlinx.coroutines.yield

interface Sequences {
  interface BufferedSequence<T>: Iterator<T> {
    /**
     * Marks the current position of the iterator, so it can later be reset
     * to this mark.
     */
    fun mark()

    /**
     * Resets the position of this iterator to the last mark.
     */
    fun reset()
  }

  /**
   * Takes a sequence and returns a new sequence that tells for each block
   * of same values in the input sequence, the size of this block.
   * For example, a sequence with characters "0001000111100" will be
   * transformed to a sequence of '0' to 3, '1' to 1, '0' to 3, '1' to 4 and
   * '0' to 2.
   * @receiver sequence of some type T
   * @return sequence of blocks of same values and the block length
   */
  fun <T> Sequence<T>.blocks(): Sequence<Pair<T, Int>>
}

fun <T> Sequence<T>.buffered(): BufferedSequence<T> = object: BufferedSequence<T> {
  private val sourceIterator = this@buffered.iterator()
  private val buffer         = mutableListOf<T>()
  private var bufferIndex    = 0
  private var marked         = false
  private var inResetMode    = false

  override fun hasNext(): Boolean =
    if (inResetMode) bufferIndex < buffer.size || sourceIterator.hasNext() else sourceIterator.hasNext()

  override fun next(): T {
    if (inResetMode) {
      if (bufferIndex < buffer.size) return buffer[bufferIndex++] else {
        inResetMode = false
        marked      = false
      }
    }
    if (!sourceIterator.hasNext()) throw NoSuchElementException()
    val next = sourceIterator.next()
    if (marked) {
      buffer += next
    }
    return next
  }

  override fun mark() {
    buffer.clear()
    marked      = true
    bufferIndex = 0
    inResetMode = false
  }

  override fun reset() {
    if (!marked) throw IllegalStateException("No mark set.")
    bufferIndex = 0
    inResetMode = true
  }
}


object BlockSequence: Sequences {
  override fun <T> Sequence<T>.blocks(): Sequence<Pair<T, Int>> =
    sequence {
      val iterator = this@blocks.iterator()
      if (!iterator.hasNext()) return@sequence
      var current = iterator.next()
      var count = 1
      while (iterator.hasNext()) {
        val next = iterator.next()
        if (next == current) {
          count++
        } else {
          yield(current to count)
          current = next
          count = 1
        }
      }
      yield(current to count)
    }
}


fun main() {
  printInputAndBlocks("")
  printInputAndBlocks("0")
  printInputAndBlocks("00")
  printInputAndBlocks("000")
  printInputAndBlocks("01")
  printInputAndBlocks("abc")
  printInputAndBlocks("011222")
  printInputAndBlocks("0001000111100")
}

private fun printInputAndBlocks(input: String) {
  println("-----------")
  input.asSequence().blocks().toList().let { println("'$input' -> $it") }
}