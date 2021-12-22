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
      5,9
      0,2
      4,1
      3,3
      
      --- scanner 1 ---
      10,13
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

  val sample3 = """
    --- scanner 0 ---
    404,-588,-901
    528,-643,409
    -838,591,734
    390,-675,-793
    -537,-823,-458
    -485,-357,347
    -345,-311,381
    -661,-816,-575
    -876,649,763
    -618,-824,-621
    553,345,-567
    474,580,667
    -447,-329,318
    -584,868,-557
    544,-627,-890
    564,392,-477
    455,729,728
    -892,524,684
    -689,845,-530
    423,-701,434
    7,-33,-71
    630,319,-379
    443,580,662
    -789,900,-551
    459,-707,401

    --- scanner 1 ---
    686,422,578
    605,423,415
    515,917,-361
    -336,658,858
    95,138,22
    -476,619,847
    -340,-569,-846
    567,-361,727
    -460,603,-452
    669,-402,600
    729,430,532
    -500,-761,534
    -322,571,750
    -466,-666,-811
    -429,-592,574
    -355,545,-477
    703,-491,-529
    -328,-685,520
    413,935,-424
    -391,539,-444
    586,-435,557
    -364,-763,-893
    807,-499,-711
    755,-354,-619
    553,889,-390
  """.trimIndent().split("\n").filter { it.isNotBlank() }

  val numberRegex = Regex("\\d+")

  data class VectorN(val coords: List<Int>) {
    val size: Int
      get() = coords.size

    operator fun get(index: Int): Int = coords[index]
  }

  data class Scanner(val id: Int, val scans: List<List<Int>>)
  data class BeaconDelta(val beaconId1: Int, val beaconId2: Int, val delta: Vector3d)

  private fun part1() {
//    val inputs = File("/Users/dkhawk/Downloads/2021/input-19-sample.txt").readLines().filter(String::isNotBlank)
    val inputs = sample3

//    val inputs = getInput(useRealInput = false)

    val scanners = createScannerMap(inputs)

    val axes = scanners.map { scanner ->
      scanner.scans.pivot() // .also { println(it) }
    }

    val first = axes.first()

    axes.drop(1).forEach { axis ->
      val corrections = correlate(first, axis)
      val correctedAxes = applyCorrections(corrections, axis)
      // println(correctedAxes)
      val scans = correctedAxes.pivot()
      println(scans.joinToString("\n"))
      println("--")

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

  fun checkAxis(target: List<Int>, points: List<Int>) {
    val sortedTarget = target.sorted()
    val sortedPoints = points.sorted()

    val start = sortedTarget.first() - sortedPoints.last()
    val end = sortedTarget.last()

    val intersections = (start..end).map { offset ->
      val shifted = sortedPoints.map { it + offset }
      offset to sortedTarget.intersect(shifted.toSet())
    }.filter { it.second.isNotEmpty() }
//    println(intersections)
    println(intersections.maxByOrNull { it.second.size })


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
    var scans = mutableListOf<List<Int>>()

    inputs.forEach { line ->
      if (line.contains("scanner")) {
        if (scannerId >= 0) {
          scanners.add(Scanner(scanners.size, scans))
        }
        scannerId = numberRegex.find(line)?.groups?.get(0)?.value?.toInt() ?: -1
        scans = mutableListOf()
      } else {
        scans.add(line.split(',').map(String::toInt))
      }
    }

    scanners.add(Scanner(scanners.size, scans))

    return scanners
  }

  private fun part2() {
    TODO("Not yet implemented")
  }
}

private fun <E> List<List<E>>.pivot(): List<List<E>> {
  val dimensions = first().size

  val result = ArrayList<MutableList<E>>(dimensions)
  repeat(dimensions) {
    result.add(mutableListOf())
  }

  forEach{ list ->
    list.forEachIndexed { dimension, value ->
      result[dimension].add(value)
    }
  }

  return result
}

private fun List<Day19.VectorN>.unzip(): List<List<Int>> {
  return map { it.coords }.pivot()
}

private fun IntRange.range(): Int {
  return last - first
}
