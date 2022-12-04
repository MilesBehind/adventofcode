package de.smartsteuer.frank.adventofcode2022

import java.net.URL

fun resource(path: String): URL? = object {}.javaClass.getResource(path)

fun lines(path: String): List<String> = resource(path)?.openStream()?.reader()?.readLines() ?: emptyList()

fun linesSequence(path: String): Sequence<String> {
  return resource(path)?.openStream()?.reader()?.buffered(20_000)?.let { reader ->
    generateSequence { reader.readLine() }
  } ?: emptySequence()
}

fun text(path: String): String = resource(path)?.openStream()?.reader()?.readText() ?: ""
