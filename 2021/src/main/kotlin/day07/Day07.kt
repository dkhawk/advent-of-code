package day07

import kotlin.math.abs
import utils.Input

@OptIn(ExperimentalStdlibApi::class)
class Day07 {
  companion object {
    fun run() {
      Day07().part1()
      Day07().part2()
    }
  }

  val sample = """16,1,2,0,4,2,7,1,2,14""".split(",").filter { it.isNotBlank() }

  private fun getInput(useRealInput: Boolean): List<Int> {
    return if (useRealInput) {
      Input.readAsLines("07").first().split(",").filter { it.isNotBlank() }
    } else {
      sample
    }.map { it.toInt() }

  }

  private fun part1() {
    val crabs = getInput(useRealInput = true)

    val max = crabs.maxOf { it }
    val min = crabs.minOf { it }

    val best = minCost(min, max, crabs) { start, end ->
      abs(end - start).toLong()
    }

    println(best)
  }

  private fun part2() {
    val crabs = getInput(useRealInput = true)

    val max = crabs.maxOf { it }
    val min = crabs.minOf { it }

    val costValues = LongArray(max - min + 1)
    costValues[0] = 0
    costValues.indices.drop(1).forEach {
      costValues[it] = costValues[it - 1] + it
    }

    val best = minCost(min, max, crabs) { start, end ->
      costValues[abs(end - start)]
    }

    println(best)
  }

  private fun minCost(
    min: Int,
    max: Int,
    crabs: List<Int>,
    costFunction: (Int, Int) -> Long
  ): Pair<Int, Long>? {
    return (min..max).map { position ->
      position to crabs.sumOf { costFunction(it, position) }
    }.minByOrNull { it.second }
  }
}
