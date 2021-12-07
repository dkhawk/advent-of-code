package day07

import kotlin.math.abs
import kotlin.math.pow
import utils.Input

@OptIn(ExperimentalStdlibApi::class)
class Day07 {
  companion object {
    fun run() {
//      Day07().part1()
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
    val inputs = getInput(useRealInput = true)

    val crabs = inputs

    val m = crabs.maxOf { it }

    val best = (0..m).map { position ->
      position to crabs.map { abs(it - position) }.sum()
    }.minByOrNull { it.second }

    println(best)
  }

  private fun part2() {
    val inputs = getInput(useRealInput = true)

    val crabs = inputs

    val m = crabs.maxOf { it }

    val best = (0..m).map { position ->
      position to crabs.map { cost(it, position) }.sum()
    }.minByOrNull { it.second }

    println(best)
  }

  val costFunction = HashMap<Int, Long>()

  private fun cost(it: Int, position: Int) : Long {
    val d = abs(it - position)
    return if (costFunction.containsKey(d)) {
      costFunction[d]!!
    } else {
      val c = generateSequence(1L) { it + 1}.take(d).sum()
      costFunction[d] = c
      c
    }
  }
}
