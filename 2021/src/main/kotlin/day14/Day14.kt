package day14

import utils.Input
import utils.InputFactory
import utils.Template

@OptIn(ExperimentalStdlibApi::class)
class Day14 {
  companion object {
    fun run() {
      Day14().part1()
      //  Day14().part2()
    }
  }

  val sample = """
      foo 1
      bar 2
      baz 3
      """.trimIndent().split("\n").filter { it.isNotBlank() }

  @Template("#0 #1")
  data class Move(val dir: String, val dist: Int)

  private fun getInput(useRealInput: Boolean): List<Move?> {
    val input = if (useRealInput) {
      Input.readAsLines("14")
    } else {
      sample
    }

    val inputFactory = InputFactory(Move::class)
    val inputs = input.map { inputFactory.lineToClass<Move>(it) }
    return inputs
  }

  private fun part1() {
    val inputs = getInput(useRealInput = false)
    println(inputs.joinToString("\n"))
  }

  private fun part2() {
    val inputs = getInput(useRealInput = false)
    println(inputs.joinToString("\n"))
    TODO("Not yet implemented")
  }
}
