package de.smartsteuer.frank.adventofcode2022.day07

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val fileSystemOutput = lines("/adventofcode2022/day07/file-system-output.txt")
  println("day 07, part 1: ${part1(fileSystemOutput)}")
  println("day 07, part 2: ${part2(fileSystemOutput)}")
}

fun part1(fileSystemOutput: List<String>): Int =
  parseFileSystemOutput(fileSystemOutput)
    .findAll()
    .asSequence()
    .filter { it.isDirectory }
    .map { it.size() }
    .filter { it < 100_000 }
    .sum()

fun part2(fileSystemOutput: List<String>): Int {
  val root        = parseFileSystemOutput(fileSystemOutput)
  val totalSpace  = 70_000_000
  val neededSpace = 30_000_000
  val usedSpace   = root.size()
  val freeSpace   = totalSpace - usedSpace
  val spaceToFree = neededSpace - freeSpace
  return root
    .findAll()
    .asSequence()
    .filter { it.isDirectory }
    .map { it.size() }
    .filter { it >= spaceToFree }
    .sorted()
    .first()
}

sealed interface FileSystemNode {
  val name:        String
  val size:        Int
  val isDirectory: Boolean
  fun size(): Int
  fun find(name: String): Directory
  fun findAll(): List<FileSystemNode>
  fun addChild(child: FileSystemNode): Directory
}

data class File(override val name: String, override val size: Int): FileSystemNode {
  override val isDirectory: Boolean = false
  override fun size(): Int = size
  override fun find(name: String) = throw IllegalStateException("find cannot be applied to files")
  override fun findAll() = listOf(this)
  override fun addChild(child: FileSystemNode) = throw IllegalStateException("children cannot be added to files")
}

data class Directory(override val name: String, private val children: MutableList<FileSystemNode> = mutableListOf()): FileSystemNode {
  override val size:        Int     = 0
  override val isDirectory: Boolean = true
  override fun size(): Int = children.sumOf { it.size() }
  override fun find(name: String) = (children.firstOrNull { it.isDirectory && it.name == name }  ?: throw IllegalStateException("could not find sub directory with name $name in children $children of ${this.name}")) as Directory
  override fun findAll() = children + children.flatMap { it.findAll() }
  override fun addChild(child: FileSystemNode): Directory = this.also { children += child }  // sorry for mutability 😔
}

fun parseFileSystemOutput(fileSystemOutput: List<String>): Directory {
  tailrec fun parseLines(lines: Sequence<String>, directories: List<Directory>, result: Directory): Directory {
    if (!lines.iterator().hasNext()) return directories.first()
    val line = lines.first()
    val remainingLines = lines.drop(1)
    return when {
      line.startsWith("$ ls")    -> parseLines(remainingLines, directories, result)
      line.startsWith("$ cd ..") -> parseLines(remainingLines, directories.dropLast(1), directories.dropLast(1).last())
      line.startsWith("$ cd")    -> {
        val directory = result.find(line.split(" ")[2])
        parseLines(remainingLines, directories + directory, directory)
      }
      else                       -> parseLines(remainingLines, directories, result.addChild(parseLine(line)))
    }
  }
  val root = Directory("/")
  return parseLines(fileSystemOutput.asSequence().drop(1), listOf(root), root)
}

fun parseLine(line: String): FileSystemNode =
  line.split(" ").let { (detail, name) ->
    when (detail) {
      "dir" -> Directory(name)
      else  -> File(name, detail.toInt())
    }
  }