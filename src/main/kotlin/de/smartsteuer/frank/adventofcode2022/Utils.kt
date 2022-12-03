package de.smartsteuer.frank.adventofcode2022

import java.net.URL

fun resource(path: String): URL? = object {}.javaClass.getResource(path)

fun lines(path: String): List<String> = resource(path)?.openStream()?.reader()?.readLines() ?: emptyList()

fun linesSequence(path: String): Sequence<String> {
  return resource(path)?.openStream()?.reader()?.buffered(200)?.let { reader ->
    generateSequence { reader.readLine() }
  } ?: emptySequence()
}
