import java.io.File
import kotlin.system.measureTimeMillis
import utils.AlphaOnly
import utils.CharGrid
import utils.InputFactory
import utils.Template

@kotlin.ExperimentalStdlibApi
class Day00 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
        Day00().part1()
        Day00().part1b()
        Day00().part2()
      }
      println("millis: $time")
    }

    // val realInput = File("/Users/dkhawk/Downloads/2021/input-1.txt").readLines()

    val testInput = """
      1-3 a: abcde
      1-3 b: cdefg
      2-9 c: ccccccccc""".trimIndent().split("\n")

    val testInput1 = """
      1721
      979
      366
      299
      675
      1456""".trimIndent().split("\n")

    val testInput3 = """
        value 5 goes to bot 2
        bot 2 gives low to bot 1 and high to bot 0
        value 3 goes to bot 1
        bot 1 gives low to output 1 and high to bot 0
        bot 0 gives low to output 2 and high to output 0
        value 2 goes to bot 2""".trimIndent().split("\n")

    val realRules = """
      departure location: 26-404 or 427-951""".trimIndent().split("\n")

    @Template("#0: #1-#2 or #3-#4")
    data class Rule(val attr: String, val low1: Int, val high1: Int, val low2: Int, val high2: Int)

    val realGrid = File("/Users/dkhawk/Downloads/2020/input-11.txt").readLines().filter { it.isNotBlank() }

    val testGrid = """
      L.LL.LL.LL
      LLLLLLL.LL
      L.L.L..L..
      LLLL.LL.LL
      L.LL.LL.LL
      L.LLLLL.LL
      ..L.L.....
      LLLLLLLLLL
      L.LLLLLL.L
      L.LLLLL.LL""".trimIndent().split("\n")

    val testInput12 = """
      F10
      N3
      F7
      R90
      F11""".trimIndent().split("\n")
  }

  @Template("#0-#1 #2: #3")
  data class Input(val low: Int, val high: Int, val letter: String, val password: String)

  @Template("#0")
  data class Input1(val value: Int)

  @Template("value #0 goes to bot #1")
  data class Value(val value: Int, val bot: Int)

  @Template("bot #0 gives low to #1 #2 and high to #3 #4")
  data class Bot(val botId: Int, val lowType: String, val lowId: Int, val highType: String, val highId: Int)

  @Template("#0#1")
  data class InputAlphaOnly(@AlphaOnly val action: String, val amount: Int)

  @Template("#0#1")
  data class InputChar(val action: Char, val amount: Int)

  private fun part1() {
    val input = testInput
//    val input = realInput

    val inputFactory = InputFactory(Input::class)
    val inputs = input.map{ inputFactory.lineToClass<Input>(it) }
    println(inputs.joinToString("\n"))

    val inputFactory1 = InputFactory(Input1::class)
    val inputs1 = testInput1.map{ inputFactory1.lineToClass<Input1>(it) }
    println(inputs1.joinToString("\n"))

    val inputFactory3a = InputFactory(Value::class)
    val inputFactory3b = InputFactory(Bot::class)
    val values = testInput3.mapNotNull { inputFactory3a.lineToClass(it) }
    val bots = testInput3.mapNotNull { inputFactory3b.lineToClass(it) }
    println(values.joinToString("\n"))
    println()
    println(bots.joinToString("\n"))
    println()

    val inputFactory12a = InputFactory(InputAlphaOnly::class)
    val directions12a = testInput12.mapNotNull { inputFactory12a.lineToClass(it) }
    val inputFactory12b = InputFactory(InputChar::class)
    val directions12b = testInput12.mapNotNull { inputFactory12b.lineToClass(it) }
    println(directions12a)
    println(directions12b)
    println()

    val inputFactory16 = InputFactory(Rule::class)
    val rules = realRules.mapNotNull { inputFactory16.lineToClass(it) }
    println(rules)
    println()
  }

  private fun part1b() {
    val input = testGrid
//    val input = realGrid
    // ==============================
    // Grids and cellular automata
    // ==============================
    //    val input = realGrid
    var grid = CharGrid(input)

    while (true) {
      val nextGrid = grid.advance { index, location, c ->
        //        val n = grid.getNeighbors8(index)
        //        when (c) {
        //          'L' -> if (n.firstOrNull { it == '#' } == null) '#' else c
        //          '#' -> if (n.count { it == '#' } >= 4) 'L' else c
        //          else -> c
        //        }
        c
      }

      if (grid.grid contentEquals nextGrid.grid) {
        break
      }

      grid = nextGrid
    }
    //    println(grid)
    val c = grid.grid.count { it == '#' }
    println(c)
  }

  private fun part2() {
  }
}
