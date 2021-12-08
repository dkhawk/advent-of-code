package day08

import kotlin.system.measureTimeMillis
import utils.Input

@OptIn(ExperimentalStdlibApi::class)
class Day08 {
  companion object {
    fun run() {
      val time1 = measureTimeMillis {
        Day08().part1()
      }
      println("millis: $time1")
      val time2 = measureTimeMillis {
        Day08().part2()
      }
      println("millis: $time2")
    }
  }

  val sample = """acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf""".split("\n").filter { it.isNotBlank() }

  val sample2 = """
    be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
    edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
    fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
    fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
    aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
    fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
    dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
    bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
    egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
    gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
  """.trimIndent().split("\n").filter { it.isNotBlank() }

  private fun getInput(useRealInput: Boolean): List<String> {
    val input = if (useRealInput) {
      Input.readAsLines("08")  // <====== TODO Set the day number!!!!
    } else {
      sample2
    }
    return input
  }

  private fun part1() {
    val inputs = getInput(useRealInput = true)

    val g = decodeValues(inputs, getDigitsMap())
      .flatten()
      .filter { it in listOf(1, 4, 7, 8) }
      .groupingBy { it }
      .eachCount()
    println(g.values.sum())
  }

  private fun part2() {
    val inputs = getInput(useRealInput = true)

    val g = decodeValues(inputs, getDigitsMap())
      .sumOf {
        it.reduce { acc, i -> (acc * 10) + i }
      }

    println(g)
  }

  private fun decodeValues(
    inputs: List<String>,
    digits: Map<Set<Char>, Int>,
  ): List<List<Int>> {
    val w = inputs.map { line ->
      val s = line.split('|')
      val patterns = s.first().trim().split(Regex("""\s+"""))
      val values = s.last().trim().split(Regex("""\s+"""))

      val decoder = decode(patterns)

      values.mapNotNull { v ->
        val d = v.map {
          decoder[it]!!
        }.toSet()
        digits[d]
      }
    }
    return w
  }

  private fun decode(patterns: List<String>): HashMap<Char, Char> {
    val decoder = HashMap<Char, Char>()

    // Do stats
    val grp = patterns.flatMap { it.toCharArray().toList()}.groupingBy { it }.eachCount()

    decoder[grp.filterValues { it == 6 }.keys.first()] = 'b'
    decoder[grp.filterValues { it == 4 }.keys.first()] = 'e'
    decoder[grp.filterValues { it == 9 }.keys.first()] = 'f'

    val one = patterns.first { it.length == 2 }.toSet().toMutableSet()

    one.remove(decoder.getKey('f'))
    decoder[one.first()] = 'c'

    val aa = grp.filterValues { it == 8 }.filterKeys { it != one.first() }
    decoder[aa.keys.first()] = 'a'

    val four = patterns.first { it.length == 4 }.toSet().toMutableSet()
    four.remove(decoder.getKey('b'))
    four.remove(decoder.getKey('c'))
    four.remove(decoder.getKey('f'))
    decoder[four.first()] = 'd'

    val all = "abcdefg".toCharArray().toSet()

    val gg = (all - decoder.keys.toSet()).first()
    decoder[gg] = 'g'

    return decoder
  }

  private fun getDigitsMap(): Map<Set<Char>, Int> {
    val digits = listOf(
      "abcefg",
      "cf",
      "acdeg",
      "acdfg",
      "bcdf",
      "abdfg",
      "abdefg",
      "acf",
      "abcdefg",
      "abcdfg",
    ).map { it.toCharArray().toSet() }.withIndex().associate { it.value to it.index }
    return digits
  }
}

fun <K, V> Map<K, V>.getKey(value: V) =
  entries.firstOrNull { it.value == value }?.key