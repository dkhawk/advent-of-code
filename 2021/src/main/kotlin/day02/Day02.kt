package day02

import java.io.File
import kotlin.math.max
import kotlin.system.measureTimeMillis
import utils.AlphaOnly
import utils.CharGrid
import utils.InputFactory
import utils.Template
import utils.Vector

@OptIn(ExperimentalStdlibApi::class)
class Day02 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
//        Day02().part1()
        Day02().part2()
      }
      println("millis: $time")
    }

    val testInput = """
      forward 5
      down 5
      forward 8
      up 3
      down 8
      forward 2""".trimIndent().split("\n").filter { it.isNotBlank() }

    @Template("#0 #1")
    data class Step(val direction: String, val distance: Int)

    val realInput = File("/Users/dkhawk/Downloads/2021/input-02.txt").readLines().filter { it.isNotBlank() }
  }

  private fun part1() {
//    val input = testInput
    val input = realInput
    val inputFactory = InputFactory(Step::class)
    val inputs = input.map{ inputFactory.lineToClass<Step>(it) }

    var maxX = 0
    var maxD = 0

    var loc = Vector(0, 0)
    inputs.forEach { step ->
      when (step!!.direction) {
        "forward" -> loc += Vector(step.distance, 0)
        "down" -> loc += Vector(0, step.distance)
        "up" -> loc += Vector(0, -step.distance)
      }
      maxX = max(maxX, loc.x)
      maxD = max(maxD, loc.y)
    }

    println(maxX)
    println(maxD)

    println(loc.x * loc.y)

  }

  private fun part2() {
//    val input = testInput
    val input = realInput
    val inputFactory = InputFactory(Step::class)
    val inputs = input.map{ inputFactory.lineToClass<Step>(it) }

    var aim = 0
    var loc = Vector(0, 0)
    inputs.forEach { step ->
      when (step!!.direction) {
        "forward" -> { loc += Vector(step.distance, step.distance * aim) }
        "down" -> {
          aim += step.distance
        }
        "up" -> {
          aim -= step.distance
        }
      }
    }

    println(loc)
    println(loc.x * loc.y)
  }
}
