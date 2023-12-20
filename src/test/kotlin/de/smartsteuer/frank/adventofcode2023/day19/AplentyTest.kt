package de.smartsteuer.frank.adventofcode2023.day19

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class AplentyTest {
  private val lines = listOf(
    "px{a<2006:qkq,m>2090:A,rfg}",
    "pv{a>1716:R,A}",
    "lnx{m>1548:A,A}",
    "rfg{s<537:gd,x>2440:R,A}",
    "qs{s>3448:A,lnx}",
    "qkq{x<1416:A,crn}",
    "crn{x>2662:A,R}",
    "in{s<1351:px,qqz}",
    "qqz{s>2770:qs,m<1801:hdj,R}",
    "gd{a>3333:R,R}",
    "hdj{m>838:A,pv}",
    "",
    "{x=787,m=2655,a=1222,s=2876}",
    "{x=1679,m=44,a=2067,s=496}",
    "{x=2036,m=264,a=79,s=2244}",
    "{x=2461,m=1339,a=466,s=291}",
    "{x=2127,m=1623,a=2188,s=1013}",
  )

  @Test
  fun `part 1`() {
    val (workflowEngine, parts) = parseWorkflowsAndParts(lines)
    part1(workflowEngine, parts) shouldBe 19_114
  }

  @Test
  fun `workflows and parts can be parsed`() {
    val (workflowEngine, parts) = parseWorkflowsAndParts(lines)
    workflowEngine shouldBe WorkflowEngine(listOf(
      Workflow("px",  listOf(Less   ("a", 2006, "qkq"), Greater    ("m", 2090, "A"), MatchAlways("rfg"))),
      Workflow("pv",  listOf(Greater("a", 1716, "R"),   MatchAlways("A"))),
      Workflow("lnx", listOf(Greater("m", 1548, "A"),   MatchAlways("A"))),
      Workflow("rfg", listOf(Less   ("s",  537, "gd"),  Greater    ("x", 2440, "R"), MatchAlways("A"))),
      Workflow("qs",  listOf(Greater("s", 3448, "A"),   MatchAlways("lnx"))),
      Workflow("qkq", listOf(Less   ("x", 1416, "A"),   MatchAlways("crn"))),
      Workflow("crn", listOf(Greater("x", 2662, "A"),   MatchAlways("R"))),
      Workflow("in",  listOf(Less   ("s", 1351, "px"),  MatchAlways("qqz"))),
      Workflow("qqz", listOf(Greater("s", 2770, "qs"),  Less       ("m", 1801, "hdj"), MatchAlways("R"))),
      Workflow("gd",  listOf(Greater("a", 3333, "R"),   MatchAlways("R"))),
      Workflow("hdj", listOf(Greater("m",  838, "A"),   MatchAlways("pv"))),
    ))
    parts shouldBe listOf(
      Part(mapOf("x" to  787, "m" to 2655, "a" to 1222, "s" to 2876)),
      Part(mapOf("x" to 1679, "m" to   44, "a" to 2067, "s" to  496)),
      Part(mapOf("x" to 2036, "m" to  264, "a" to   79, "s" to 2244)),
      Part(mapOf("x" to 2461, "m" to 1339, "a" to  466, "s" to  291)),
      Part(mapOf("x" to 2127, "m" to 1623, "a" to 2188, "s" to 1013)),
    )
  }

  /*
    s < 1351 and s >  536 and x < 2441                 =>   s in  537..1350,  x in    1..2440,  m in    1..4000,  a in 1..4000
    s < 1351 and a < 2006 and x > 1415 and x > 2662    =>   s in    1..1350,  x in 2663..4000,  m in    1..4000,  a in 1..2005
    s < 1351 and a < 2006 and x < 1416                 =>   s in    1..1350,  x in    1..1415,  m in    1..4000,  a in 1..2005
    s < 1351 and m > 2090                              =>   s in    1..1350,  x in    1..4000,  m in 2091..4000,  a in 1..4000
    s > 1350 and s > 2770 and s < 3449                 =>   s in 2771..3448,  x in    1..4000,  m in    1..4000,  a in 1..4000   =>   s in 2771..4000,  x in    1..4000,  m in    1..4000,  a in 1..4000
    s > 1350 and s > 2770 and s > 3448                 =>   s in 3449..4000,  x in    1..4000,  m in    1..4000,  a in 1..4000   =>   ""
    s > 1350 and m < 1801 and m >  838                 =>   s in 1351..4000,  x in    1..4000,  m in  839..1800,  a in 1..4000
    s > 1350 and m < 1801 and m <  839 and a < 1717    =>   s in 1351..4000,  x in    1..4000,  m in    1.. 838,  a in 1..1716
  */
}