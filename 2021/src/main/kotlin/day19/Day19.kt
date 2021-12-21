package day19

import kotlin.system.measureTimeMillis
import utils.Input
import utils.Vector3d

@OptIn(ExperimentalStdlibApi::class)
class Day19 {
  companion object {
    fun run() {
      measureTimeMillis {
        Day19().part1()
      }.also {
        println("millis: $it")
      }
      measureTimeMillis {
        //  Day19().part2()
      }.also {
        println("millis: $it")
      }

    }
  }

  val sample = """
      --- scanner 0 ---
      0,2
      4,1
      3,3
      
      --- scanner 1 ---
      -1,-1
      -5,0
      -2,1""".trimIndent().split("\n").filter { it.isNotBlank() }

  val sample2 = """
    --- scanner 0 ---
    -1,-1,1
    -2,-2,2
    -3,-3,3
    -2,-3,1
    5,6,-4
    8,0,7

    --- scanner 1 ---
    1,-1,1
    2,-2,2
    3,-3,3
    2,-1,3
    -5,4,-6
    -8,-7,0
    
    --- scanner 2 ---
    -1,-1,-1
    -2,-2,-2
    -3,-3,-3
    -1,-3,-2
    4,6,5
    -7,0,8

    --- scanner 3 ---
    1,1,-1
    2,2,-2
    3,3,-3
    1,3,-2
    -4,-6,5
    7,0,8

    --- scanner 4 ---
    1,1,1
    2,2,2
    3,3,3
    3,1,2
    -6,-4,-5
    0,7,-8
  """.trimIndent().split("\n").filter { it.isNotBlank() }

  private fun getInput(useRealInput: Boolean): List<String> {
    val input = if (useRealInput) {
      Input.readAsLines("19")
    } else {
      sample
    }

    return input
  }

  val numberRegex = Regex("\\d+")

  data class VectorN(val coords: List<Int>) {
    val size: Int
      get() = coords.size

    operator fun get(index: Int): Int = coords[index]
  }

  data class Scanner(val id: Int, val scans: List<VectorN>)
  data class BeaconDelta(val beaconId1: Int, val beaconId2: Int, val delta: Vector3d)

  private fun part1() {
//    val inputs = File("/Users/dkhawk/Downloads/2021/input-19-sample.txt").readLines().filter(String::isNotBlank)
    val inputs = sample2

//    val inputs = getInput(useRealInput = false)

    val scanners = createScannerMap(inputs)

    val axes = scanners.map { scanner ->
      scanner.scans.unzip() // .also { println(it) }
    }

    val first = axes.first()

    axes.drop(1).forEach { axis ->
      val corrections = correlate(first, axis)
      val scans = applyCorrections(corrections, axis)
      println(scans)
    }
  }

  private fun applyCorrections(
    corrections: List<Correction>,
    originalScans: List<List<Int>>,
  ): List<List<Int>> {
    return corrections.map { correction ->
      val scans = originalScans[correction.axis]
      scans.map { (it * correction.sign) + correction.delta }
    }
  }

  data class Correction(val axis: Int, val error: Int, val delta: Int, val sign: Int = 1)

  private fun correlate(axes0: List<List<Int>>, axes1: List<List<Int>>): List<Correction> {
    return axes0.map { target ->
      val sortedTarget = target.sorted()
      val errors = axes1.mapIndexed { index, axis ->
        val axisSorted = axis.sorted()
        var result = correlationHelper(sortedTarget, axisSorted, index)

        if (result.error > 0) {
          val reversed = correlationHelper(sortedTarget,
                                           axisSorted.reversed().map { -it },
                                           index).copy(sign = -1)
          if (reversed.error < result.error) {
            result = reversed
          }
        }
        result
      }
      errors.minByOrNull { it.error }!!
    }
  }

  private fun correlationHelper(
    sortedTarget: List<Int>,
    axisSorted: List<Int>,
    index: Int,
  ): Correction {
    // can this be shifted to match?
    val delta = sortedTarget[0] - axisSorted[0]
    val errors = sortedTarget.zip(axisSorted).map { (a, b) -> a - (b + delta) }
    val error = errors.map { it * it }.sum()
    return Correction(index, error, delta)
  }

  private fun plotAxis(scanner: Scanner, axisNumber: Int): List<Int> {
    return scanner.scans.map { it[axisNumber] }
  }

  private fun findBeacon(
    beaconDeltaMap: Map<Int, List<Vector3d>>,
    deltas: List<Vector3d>,
  ): Set<Int> {
    val answer = deltas.map { delta ->
      beaconDeltaMap.filterValues { listOfDeltas ->
        listOfDeltas.any {
          it == delta
        }
      }.keys.toSet()
    }.reduce { acc, it ->
      acc.intersect(it)
    }

    return answer
  }

  private fun scannerDeltaMap(scanner: List<BeaconDelta>): MutableMap<Int, List<BeaconDelta>> {
    val m1 = scanner.groupBy { it.beaconId1 }.toMutableMap()
    val m2 = scanner.groupBy { it.beaconId2 }.toMutableMap()

    m2.forEach { (id, deltas) ->
      if (!m1.contains(id)) {
        m1[id] = deltas
      } else {
        m1[id] = m1[id]!! + deltas
      }
    }

    return m1
  }

  private fun computeDeltas(scans: List<Vector3d>, offset: Int = 0): List<BeaconDelta> {
    if (scans.size <= 1) {
      return listOf()
    }

    val s0 = scans.first()
    return scans.drop(1).mapIndexed { index, s1 ->
      BeaconDelta(offset, offset + index + 1, (s0 - s1).abs())
    } + computeDeltas(scans.drop(1), offset + 1)
  }

  private fun createScannerMap(inputs: List<String>): List<Scanner> {
    val scanners = mutableListOf<Scanner>()
    var scannerId = -1
    var scans = mutableListOf<VectorN>()

    inputs.forEach { line ->
      if (line.contains("scanner")) {
        if (scannerId >= 0) {
          scanners.add(Scanner(scanners.size, scans))
        }
        scannerId = numberRegex.find(line)?.groups?.get(0)?.value?.toInt() ?: -1
        scans = mutableListOf()
      } else {
        scans.add(VectorN(line.split(',').map(String::toInt)))
      }
    }

    scanners.add(Scanner(scanners.size, scans))

    return scanners
  }

  private fun part2() {
    val inputs = getInput(useRealInput = false)
    println(inputs.joinToString("\n"))
    TODO("Not yet implemented")
  }
}

private fun List<Day19.VectorN>.unzip(): ArrayList<MutableList<Int>> {
  val dimensions = first().size

  val result = ArrayList<MutableList<Int>>(dimensions)
  repeat(dimensions) {
    result.add(mutableListOf())
  }

  forEach{ vectorN ->
    vectorN.coords.forEachIndexed { dimension, value ->
      result[dimension].add(value)
    }
  }

  return result
}

private fun IntRange.range(): Int {
  return last - first
}
