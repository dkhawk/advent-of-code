package day05

import kotlin.system.measureTimeMillis
import utils.Input
import utils.Vector

@OptIn(ExperimentalStdlibApi::class)
class Day05 {
  companion object {
    fun run() {
      val time1 = measureTimeMillis {
        Day05().part1()
      }
      println("millis: $time1")
      val time2 = measureTimeMillis {
        Day05().part2()
      }
      println("millis: $time2")
    }
  }

  val sample = """
      0,9 -> 5,9
      8,0 -> 0,8
      9,4 -> 3,4
      2,2 -> 2,1
      7,0 -> 7,4
      6,4 -> 2,0
      0,9 -> 2,9
      3,4 -> 1,4
      0,0 -> 8,8
      5,5 -> 8,2
      """.trimIndent().split("\n").filter { it.isNotBlank() }

  private fun getInput(useRealInput: Boolean): List<Pair<Vector, Vector>> {
    val input = if (useRealInput) {
      Input.readAsLines("05")
    } else {
      sample
    }

    val lines = input.map { line ->
      line.split(" -> ").map { point ->
        point.split(",").map { it.toInt() }
      }
    }.map { line ->
      line.first().toPoint() to line.last().toPoint()
    }

    return lines
  }

  private fun part1() {
    solve(includeDiagonals = false)
  }

  private fun solve(includeDiagonals: Boolean) {
    val inputs = getInput(useRealInput = true)

    val locations = HashMap<Vector, Int>()
    inputs.filter { line ->
      includeDiagonals || (line.second.x == line.first.x) || (line.second.y == line.first.y)
    }.forEach { line ->
      var start = line.first
      val end = line.second

      val dirVector = start.directionTo(end).sign

      locations[start] = locations.getOrDefault(start, 0) + 1
      while (start != end) {
        start += dirVector
        locations[start] = locations.getOrDefault(start, 0) + 1
      }
    }

    val answer = locations.count { (_, v) ->
      v > 1
    }

    println(answer)
  }

  private fun part2() {
    solve(includeDiagonals = true)
  }
}

private fun  List<Int>.toPoint(): Vector = Vector(first(), last())
