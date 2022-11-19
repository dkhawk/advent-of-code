package aoc2016

import kotlin.system.measureTimeMillis

class Day18 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
        // Day18().part1()
        Day18().part2()
      }
      println("millis: $time")
    }
  }

  private fun part1() {
    // val input = "..^^."
    // val input = ".^^.^.^^^^"
    val input = ".^.^..^......^^^^^...^^^...^...^....^^.^...^.^^^^....^...^^.^^^...^^^^.^^.^.^^..^.^^^..^^^^^^.^^^..^"
    var next = input.toMask()
    var safeTiles = next.countSafeTiles()
    repeat(40 - 1) {
      next = nextRow(next)
      safeTiles += next.countSafeTiles()
    }
    println(safeTiles)
  }

  private fun part2() {
    val input = ".^.^..^......^^^^^...^^^...^...^....^^.^...^.^^^^....^...^^.^^^...^^^^.^^.^.^^..^.^^^..^^^^^^.^^^..^"
    var next = input.toMask()
    var safeTiles = next.countSafeTiles()
    repeat(400_000 - 1) {
      next = nextRow(next)
      safeTiles += next.countSafeTiles()
    }
    println(safeTiles)
  }

  private fun nextRow(input: String): String {
    return "0${input}0".windowed(3, 1, false) {
      val isTrap = isTrap(it.toString())
      if (isTrap) {
        '1'
      } else {
        '0'
      }
    }.joinToString("")
  }
}

private fun String.countSafeTiles(): Int = count { it == '0' }

private fun String.toMapRow(): String {
  return map {
    when (it) {
      '0' -> '.'
      '1' -> '^'
      else -> throw Exception("Illegal character: $it")
    }
  }.joinToString("")
}

private fun String.toMask(): String {
  return map {
    when (it) {
      '.' -> '0'
      '^' -> '1'
      else -> throw Exception("Illegal character: $it")
    }
  }.joinToString("")
}

fun isTrap(mask: String): Boolean {
  return when (mask) {
    "110" -> true
    "011" -> true
    "100" -> true
    "001" -> true
    else -> false
  }
}

fun isTrap(left: Int, center: Int, right: Int): Boolean {
  val mask = left shl 2 or center shl 1 or right
  return when (mask) {
    0b110 -> true
    0b011 -> true
    0b100 -> true
    0b001 -> true
    else -> false
  }
}

