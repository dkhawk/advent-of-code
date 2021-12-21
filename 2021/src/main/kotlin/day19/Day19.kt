package day19

import kotlin.math.sign
import utils.Input
import utils.Vector3d

@OptIn(ExperimentalStdlibApi::class)
class Day19 {
  companion object {
    fun run() {
      Day19().part1()
      //  Day19().part2()
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

    --- scanner 0 ---
    1,-1,1
    2,-2,2
    3,-3,3
    2,-1,3
    -5,4,-6
    -8,-7,0
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
    // TODO(dkhawk): Plot each axis of scan set zero
    // Then check against plots of each axis in both directions finding the highest correlation
    // so for the second scanner (scanner 1), plot x in both the position and reverse direction
    // the slide the plot to find the highest number of matching pixels
    // then repeat for the y and z axis.  Then try the different axis against each other
    // x vs x, x vs -x
    // x vs y, x vs -y
    // x vs z, x vs -z
    //
    // Naturally, the remaining two axis should be compared to the other remaining two axis
    // i.e., the combination


//    val inputs = File("/Users/dkhawk/Downloads/2021/input-19-sample.txt").readLines().filter(String::isNotBlank)
    val inputs = sample2

//    val inputs = getInput(useRealInput = false)
//    println(inputs.joinToString("\n"))

    val scanners = createScannerMap(inputs)

    val axes0 = (0 until scanners[0].scans.first().size).map {
      plotAxis(scanners[0], it).sorted()
    }

    val axes1 = (0 until scanners[1].scans.first().size).flatMap {
      val list = plotAxis(scanners[1], it).sorted()
      val list2 = list.map { it * it.sign }.sorted()
      listOf(list, list2)
    }

    correlate(axes0, axes1)
  }

  private fun correlate(axes0: List<List<Int>>, axes1: List<List<Int>>) {
    val target = axes0.first()
    axes1.forEach { axis ->
      // can this be shifted to match?
      val delta = target[0] - axis[0]
      val errors = target.zip(axis).map { (a, b) -> a - (b + delta) }
      println(errors)
    }
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

private fun IntRange.range(): Int {
  return last - first
}
