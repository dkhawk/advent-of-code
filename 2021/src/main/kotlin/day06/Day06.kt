package day06

import kotlin.system.measureTimeMillis
import utils.Input

@OptIn(ExperimentalStdlibApi::class)
class Day06 {
  companion object {
    fun run() {
      val time1 = measureTimeMillis {
        Day06().part1()
      }
      println("millis: $time1")
      val time2 = measureTimeMillis {
        Day06().part2()
      }
      println("millis: $time2")
    }
  }

  val sample = """3,4,3,1,2""".split(",").filter { it.isNotBlank() }.map { it.toInt() }

  private fun getInput(useRealInput: Boolean): List<Int> {
    val input = if (useRealInput) {
      Input.readAsLines("06").first().split(",").map { it.toInt() }
    } else {
      sample
    }

    return input
  }

  private fun part1() {
    val inputs = getInput(useRealInput = true)

    var fish = inputs

    repeat(80) {
      fish = fish.flatMap { it ->
        val n = it - 1

        if (n == -1) {
          listOf(6,8)
        } else {
          listOf(n)
        }
      }
    }

    println(fish.count())
  }

  private fun part2() {
    val inputs = getInput(useRealInput = true)

    val fishByAge = inputs.groupingBy { it }.eachCount()
      // This whole song and dance is to force the values to be longs
      .toList().associate { it.first to it.second.toLong() }
      .toMutableMap()

    repeat(256) {
      val tmp = fishByAge.getOrDefault(0, 0)

      fishByAge[0] = fishByAge.getOrDefault(1, 0)
      fishByAge[1] = fishByAge.getOrDefault(2, 0)
      fishByAge[2] = fishByAge.getOrDefault(3, 0)
      fishByAge[3] = fishByAge.getOrDefault(4, 0)
      fishByAge[4] = fishByAge.getOrDefault(5, 0)
      fishByAge[5] = fishByAge.getOrDefault(6, 0)
      fishByAge[6] = fishByAge.getOrDefault(7, 0) + tmp
      fishByAge[7] = fishByAge.getOrDefault(8, 0)
      fishByAge[8] = tmp
    }

    println(fishByAge.values.sum())
  }
}
