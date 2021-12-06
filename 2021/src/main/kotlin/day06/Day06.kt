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

    val last = runSimulation(inputs, 80)
    println(last.sum())
  }

  private fun part2() {
    val inputs = getInput(useRealInput = true)

    val last = runSimulation(inputs, 256)
    println(last.sum())
  }

  private fun runSimulation(inputs: List<Int>, generations: Int): List<Long> {
    val fishMap = inputs.groupBy { it }
    val fish = (0..8).map {
      fishMap.getOrDefault(it, emptyList()).size.toLong()
    }

    val s = generateSequence(fish) { previous ->
      (0..5).map { previous[it + 1] } + (previous[0] + previous[7]) + previous[8] + previous[0]
    }

    // Have to drop the seed to get the count correct
    return s.drop(1).take(generations).last()
  }
}
