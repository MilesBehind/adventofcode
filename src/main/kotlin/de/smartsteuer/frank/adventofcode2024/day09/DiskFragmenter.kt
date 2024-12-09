package de.smartsteuer.frank.adventofcode2024.day09

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  DiskFragmenter.execute(lines("/adventofcode2024/day09/disk-map.txt"))
}

object DiskFragmenter: Day {

  override fun part1(input: List<String>): Long =
    input.parseDiskMapAsBlocks().compactByMovingBlocks().checkSumOfBlocks()

  override fun part2(input: List<String>): Long =
    input.parseDiskMapAsDiskSpace().compactByMovingFiles().checkSumOfFiles()

  private fun List<Block>.compactByMovingBlocks(): List<Block> {
    tailrec fun compact(blocks: List<Block>, targetIndex: Int, sourceIndex: Int, result: MutableList<Block>): List<Block> {
      if (sourceIndex == targetIndex) return result.apply { add(blocks[targetIndex]) }
      if (this[targetIndex] is File) return compact(blocks, targetIndex + 1, sourceIndex, result.apply { add(blocks[targetIndex]) })
      if (this[sourceIndex] is Free) return compact(blocks, targetIndex, sourceIndex - 1, result)
      return compact(blocks, targetIndex + 1, sourceIndex - 1, result.apply { add(blocks[sourceIndex]) })
    }
    return compact(this, 0, this.size - 1, mutableListOf())
  }

  private fun List<Block>.checkSumOfBlocks(): Long =
    this.mapIndexed { index, block -> block.checkSum(index)  }.sum()

  sealed interface Block {
    fun checkSum(position: Int): Long
  }

  data object Free: Block {
    override fun checkSum(position: Int): Long = 0
  }

  data class File(val id: Int): Block {
    override fun checkSum(position: Int): Long = id.toLong() * position
  }

  private fun List<String>.parseDiskMapAsBlocks(): List<Block> =
    this.first().flatMapIndexed { index, c ->
      if (index % 2 == 0) List(c.digitToInt()) { File(index / 2) } else List(c.digitToInt()) { Free }
    }

  //-----------------------

  private fun List<DiskSpace>.compactByMovingFiles(): List<DiskSpace> {
    tailrec fun compact(sourceIndex: Int, result: MutableList<DiskSpace>): List<DiskSpace> {
      if (sourceIndex < 0) return result
      val fileToMove = result[sourceIndex]
      if (fileToMove is FreeSpace) return compact(sourceIndex - 1, result)
      val targetIndex = result.take(sourceIndex).indexOfFirst { it is FreeSpace && it.size >= fileToMove.size }
      if (targetIndex < 0) return compact(sourceIndex - 1, result)
      return compact(sourceIndex - 1, result.apply {
        val freeSpace = result[targetIndex].size
        set(targetIndex, fileToMove)
        set(sourceIndex, FreeSpace(fileToMove.size))
        if (fileToMove.size < freeSpace) add(targetIndex + 1, FreeSpace(freeSpace - fileToMove.size))
      })
    }
    return compact(this.size - 1, this.toMutableList())
  }

  private fun List<DiskSpace>.checkSumOfFiles(): Long =
    this.fold(0 to 0L) { (position, sum), diskSpace -> (position + diskSpace.size) to (sum + diskSpace.checkSum(position)) }.second

  sealed interface DiskSpace {
    val size: Int
    fun checkSum(position: Int): Long
  }

  data class FileSpace(val id: Int, override val size: Int): DiskSpace {
    override fun checkSum(position: Int): Long = (0 until size).sumOf { (position + it) * id.toLong() }
  }

  data class FreeSpace(override val size: Int): DiskSpace {
    override fun checkSum(position: Int): Long = 0
  }

  private fun List<String>.parseDiskMapAsDiskSpace(): List<DiskSpace> =
    this.first().mapIndexed { index, c ->
      if (index % 2 == 0) FileSpace(index / 2, c.digitToInt()) else FreeSpace(c.digitToInt())
    }
}
