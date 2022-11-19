package aoc2016

import kotlin.system.measureTimeMillis

@kotlin.ExperimentalStdlibApi
class Day16 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
        // Day16().part1()
        Day16().part2()
      }
      println("millis: $time")
    }
  }

  private fun part1() {
    val testing = false
    if (testing) {
      // val inputs = listOf(
      //   "1" to "100",
      //   "0" to "001",
      //   "11111" to "11111000000",
      //   "111100001010" to "1111000010100101011110000",
      //  )
      //
      // inputs.forEach { (input, expected) ->
      //   val result = step(input)
      //   if (result == expected) {
      //     println("pass")
      //   } else {
      //     println("fail")
      //   }
      // }

      // println(checkSum("110010110100"))

      val data = fill("10000", 20)
      println(checkSum(data))

    } else {
      val input = "11100010111110100"
      val data = fill(input, 272)
      println(checkSum(data))
    }
  }

  private fun fill(s: String, size: Int): String {
    var data = s
    while (data.length < size) {
      data = step(data)
    }
    return data.substring(0, size)
  }

  private fun checkSum(s: String): String {
    return if (s.length.isOdd()) {
      s
    } else {
      checkSum(s.windowed(2, 2).map {
        if (it.first() == it.last()) {
          '1'
        } else {
          '0'
        }
      }.joinToString(""))
    }
  }

  private fun step(a: String): String {
    val b = a.reversed().mapNotNull {
      when(it) {
        '0' -> '1'
        '1' -> '0'
        else -> null
      }
    }.joinToString("")
    return listOf(a, "0", b).joinToString("")
  }

  private fun part2() {
    val input = "11100010111110100"
    val data = fill(input, 35651584)
    println(checkSum(data))
  }
}

private fun Int.isOdd(): Boolean = this % 2 == 1
