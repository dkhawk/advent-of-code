package template

import utils.Input
import utils.InputFactory
import utils.Template

@OptIn(ExperimentalStdlibApi::class)
class TemplateDay {
  companion object {
    fun run() {
      TemplateDay().part1()
      //  TemplateDay().part2()
    }
  }

  val sample = """
      foo
      bar
      baz
      """.trimIndent().split("\n").filter { it.isNotBlank() }

  @Template("#0 #1")
  data class Move(val dir: String, val dist: Int)

  private fun getInput(useRealInput: Boolean): List<Move?> {
    val input = if (useRealInput) {
      Input.readAsLines(100000)  // <====== TODO Set the day number!!!!
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
