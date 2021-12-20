package day19

import java.io.File
import utils.CharGrid
import utils.Input
import utils.Vector
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

  private fun getInput(useRealInput: Boolean): List<String> {
    val input = if (useRealInput) {
      Input.readAsLines("19")
    } else {
      sample
    }

    return input
  }

  val numberRegex = Regex("\\d+")

  data class Scanner(val id: Int, val scans: List<Vector3d>)
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


    val inputs = File("/Users/dkhawk/Downloads/2021/input-19-sample.txt").readLines().filter(String::isNotBlank)

//    val inputs = getInput(useRealInput = false)
//    println(inputs.joinToString("\n"))

    val scanners = createScannerMap(inputs)

    val scannerDeltas = scanners.map { scanner ->
      computeDeltas(scanner.scans)
    }


    val sdms = scannerDeltas.map { scannerDeltaMap(it) }
    val beaconSortedDeltaMaps = sdms.take(2).map { sdm ->
      sdm.map { (bid, bds) ->
        bid to bds.map { it.delta }
      }.toMap()
        .map { (bid, bdm) ->
        bid to bdm.map {
          listOf(it.x, it.y, it.z).sorted()
        }.sortedBy { it.sum() }
          .map { it.toString() }
      }
    }

    val s = beaconSortedDeltaMaps.joinToString("\n\n") {
      it.joinToString("\n") { "${it.second.toString()} ${it.first}" }
    }

    val deltas = beaconSortedDeltaMaps[0][9]
    val d1 = deltas.second.first()
    println(d1)

    beaconSortedDeltaMaps[1].forEach { (a, b) ->
      if (b.contains(d1)) {
        println("$a  =>  $b")
      }
    }

//    beaconSortedDeltaMaps[1].filter {
//      val second = it.second
//      second.contains(beaconSortedDeltaMaps[0][9].second)
//    }

//    println(s)

    // [[8, 43, 46], [1, 81, 163], [149, 172, 1008], [236, 280, 1022], [133, 467, 968],
    // [171, 495, 939], [197, 269, 1162], [273, 513, 1002], [34, 64, 1692], [71, 91, 1669],
    // [70, 171, 1724], [550, 625, 791], [117, 1022, 1077], [123, 1041, 1055], [181, 1030, 1146],
    // [54, 1169, 1171], [144, 1182, 1216], [242, 1143, 1248], [274, 1305, 1348], [220, 1355, 1415],
    // [258, 1384, 1473], [1061, 1283, 1404], [1092, 1288, 1404], [1073, 1349, 1553]] 9


    // [[8, 43, 46], [1, 81, 163], [17, 22, 824], [21, 100, 857], [119, 149, 783], [149, 172, 1008],
    // [284, 556, 591], [236, 280, 1022], [133, 467, 968], [171, 495, 939], [197, 269, 1162],
    // [273, 513, 1002], [17, 913, 1107], [69, 776, 1197], [4, 1014, 1115], [58, 1014, 1107],
    // [117, 1022, 1077], [123, 1041, 1055], [121, 921, 1289], [181, 1030, 1146], [44, 1183, 1186],
    // [991, 1026, 1424], [1088, 1152, 1389], [1050, 1185, 1471]] 0


//    val t = s.split("\n").filter(String::isNotBlank).sorted()
//
//    println(t.joinToString("\n"))

    return

    val sdm1 = scannerDeltaMap(scannerDeltas.first())
    val beaconDeltaMap = sdm1.map { (bid, bds) ->
      bid to bds.map { it.delta }
    }.toMap()


    val beaconSortedDeltaMap = beaconDeltaMap.map { (bid, bdm) ->
      bid to bdm.map {
        listOf(it.x, it.y, it.z).sorted()
      }.sortedBy { it.sum() }
    }

    println(beaconSortedDeltaMap.joinToString("\n"))

    return







    val sdm2 = scannerDeltaMap(scannerDeltas[1])


    val beaconDeltas = sdm2.map { (k, v) -> k to v.map { it.delta } }.toMap()

    val resolved = beaconDeltas.map { (bid, deltas) ->
      bid to findBeacon(beaconDeltaMap, deltas)
    }

    val (beaconInScanner1, possibilities) = resolved[0]
    val beaconInScanner2 = possibilities.first()

    println("++++++++++++++")
    println(scanners[0].scans[beaconInScanner1])
    println(scanners[1].scans[beaconInScanner2])

    val scanner1 = scanners[0].scans[beaconInScanner1] - scanners[1].scans[beaconInScanner2]

//    val grid = CharGrid(20, 20)
//    val offset = Vector3d(10, 10, 10)
//    scanners[0].scans.forEach { scan ->
//      val loc = scan + offset
//      grid.setCell(loc, '*')
//    }
//
//    grid.setCell(offset, '0')
//    grid.setCell(offset + scanner1, '1')
//
//    println(grid.flipAlongHorizontalAxis())
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
    var scans = mutableListOf<Vector3d>()

    inputs.forEach { line ->
      if (line.contains("scanner")) {
        if (scannerId >= 0) {
          scanners.add(Scanner(scanners.size, scans))
        }
        scannerId = numberRegex.find(line)?.groups?.get(0)?.value?.toInt() ?: -1
        scans = mutableListOf()
      } else {
        line.split(',')
          .map(String::toInt)
          .also {
            scans.add(Vector3d(it[0], it[1], it[2]))
          }
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
