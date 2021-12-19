package de.smartsteuer.frank.adventofcode2021

import java.net.URL

fun resource(path: String): URL? = object {}.javaClass.getResource(path)

fun lines(path: String): List<String> = resource(path)?.openStream()?.reader()?.readLines() ?: emptyList()