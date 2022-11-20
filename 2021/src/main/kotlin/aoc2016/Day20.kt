package aoc2016

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

class Day20 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
        // Day20().part1()
        Day20().part2()
      }
      println("millis: $time")
    }
  }

  private fun part1() {
    val badList = listOf(
      "5-8",
      "0-2",
      "4-7",
    )

    val input = File("/Users/dkhawk/Documents/advent-of-code/2021/src/main/resources/2016/20.txt").readLines()

    val r = input

    val ranges = r.map {
      val numbs = it.split('-')
      numbs.first().toLong()..numbs.last().toLong()
    }

    val mergedRanges = mergeRanges(ranges)

    println(mergedRanges)

    val smallest = mergedRanges.first().last + 1
    println(smallest)
  }

  private fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
    val sortedByFirst = ranges.sortedBy { it.first }
    val mergedRanges = mutableListOf<LongRange>()
    var merged = sortedByFirst.first()

    sortedByFirst.drop(1).forEach { candidate ->
      merged = if (candidate.first <= merged.last) {
        merged.merge(candidate)
      } else {
        mergedRanges.add(merged)
        candidate
      }
    }

    mergedRanges.add(merged)
    return mergedRanges
  }

  private fun part2() {
    val badList = listOf(
      "5-8",
      "0-2",
      "4-7",
    )

    val input = File("/Users/dkhawk/Documents/advent-of-code/2021/src/main/resources/2016/20.txt").readLines()

    val r = input

    val ranges = r.map {
      val numbs = it.split('-')
      numbs.first().toLong()..numbs.last().toLong()
    }

    val mergedRanges = mergeRanges(ranges)

    println(mergedRanges)
    val sum = mergedRanges.windowed(2, 1).map { (it.last().first - it.first().last) -1 }.sum()
    println(sum)
  }

}

private fun LongRange.merge(candidate: LongRange): LongRange =
  min(first, candidate.first)..max(last, candidate.last)
