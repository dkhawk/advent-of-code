package aoc2016

import kotlin.system.measureTimeMillis

class Day19 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
        // Day19().part1()
        Day19().part2()
      }
      println("millis: $time")
    }
  }

  class Elf(
    val id: Int,
    var numPresents: Int = 1,
    var prev: Elf? = null,
    var next: Elf? = null
  ) {
    override fun toString(): String {
      return "$id: $numPresents (next: ${next?.id}) (prev: ${prev?.id})"
    }

    fun advance(steps: Int): Elf {
      var result = this
      repeat(steps) {
        result = result.next!!
      }
      return result
    }
  }

  private fun part1() {
    val rootElf = createElfCircle(3004953)

    // printElves(rootElf)

    val lastElf = exchangeGifts(rootElf)
    print("Winner: ")
    printElves(lastElf)
  }

  private fun part2() {
    // val numElves = 5
    val numElves = 3004953
    val rootElf = createElfCircle(numElves)

    // printElves(rootElf)

    val lastElf = exchangeGifts2(rootElf, numElves)
    print("Winner: ")
    printElves(lastElf)
  }

  private fun printElves(rootElf: Elf) {
    var elf = rootElf
    while (true) {
      println(elf)
      elf = elf.next!!
      if (elf == rootElf) {
        break
      }
    }
  }

  private fun exchangeGifts(rootElf: Elf): Elf {
    var currentElf = rootElf

    while (currentElf.next != currentElf) {
      val next = currentElf.next!!
      currentElf.numPresents += next.numPresents
      currentElf.next = next.next
      currentElf.next!!.prev = currentElf
      currentElf = currentElf.next!!
    }

    return currentElf
  }

  private fun exchangeGifts2(rootElf: Elf, initialNumElves: Int): Elf {
    var currentElf = rootElf
    var oppElf = currentElf.advance(initialNumElves / 2)
    var numElves = initialNumElves

    // val progressStep = 1000
    // var nextProgress = Int.MAX_VALUE

    var count = 0

    while (currentElf.next!! != currentElf) {
      // printElves(currentElf)
      // println(oppElf.id)
      // println()
      // if (numElves < nextProgress) {
      //   println(numElves)
      //   nextProgress = numElves - progressStep
      // }

      currentElf.numPresents += oppElf.numPresents

      val newOpp = oppElf.next!!

      removeElf(oppElf)

      currentElf = currentElf.next!!
      oppElf = newOpp
      count += 1
      if (count % 2 == 1) {
        oppElf = oppElf.next!!
      }

      numElves -= 1
    }

    return currentElf
  }

  private fun removeElf(elf: Elf) {
    elf.prev!!.next = elf.next
    elf.next!!.prev = elf.prev
  }

  private fun createElfCircle(numElves: Int): Elf {
    var nextId = 1
    val firstElf = Elf(nextId++)
    var prevElf = firstElf
    repeat(numElves - 1) {
      val elf = Elf(nextId++, prev = prevElf)
      prevElf.next = elf
      prevElf = elf
    }
    prevElf.next = firstElf
    firstElf.prev = prevElf

    return firstElf
  }
}
