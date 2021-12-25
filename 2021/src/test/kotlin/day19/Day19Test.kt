package day19

import java.io.File
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Day19Test {
  val day = Day19()

  @Test
  fun `find best axis`() {
//    val inputs = day.sample3
    val inputs = File("/Users/dkhawk/Downloads/2021/input-19-sample.txt").readLines().filter(String::isNotBlank)

    val scanners = day.createScannerMap(inputs)

    val axes = scanners.map { scanner ->
      scanner.scans.pivot() // .also { println(it) }
    }

    val firstScanner = axes.first()

    val corrections = firstScanner.map { target ->
      day.bestAxis(target, axes[1])!!
    }

    val corrected = day.applyAxisCorrections(corrections, axes[1])
    val correctedScans = corrected.pivot()
    val commonBeacons = scanners.first().scans.toSet().intersect(correctedScans.toSet())

    val expectedBeacons = """
      -618,-824,-621
      -537,-823,-458
      -447,-329,318
      404,-588,-901
      544,-627,-890
      528,-643,409
      -661,-816,-575
      390,-675,-793
      423,-701,434
      -345,-311,381
      459,-707,401
      -485,-357,347""".trimIndent().split("\n").filter { it.isNotBlank() }
      .map { it.split(",").map { it.toInt() } }
      .toSet()

    assertEquals(expectedBeacons, commonBeacons)

//    var target = ("404, 528, -838, 390, -537, -485, -345, -661, -876, -618, 553, 474, -447, -584, " +
//      "544, 564, 455, -892, -689, 423, 7, 630, 443, -789, 459")
//      .filter { it != ' ' }
//      .split(",")
//      .map(String::toInt)
//
//    val axes = """686, 605, 515, -336, 95, -476, -340, 567, -460, 669, 729, -500, -322, -466, -429,
//      -355, 703, -328, 413, -391, 586, -364, 807, 755, 553;
//      422, 423, 917, 658, 138, 619, -569, -361, 603, -402, 430, -761, 571, -666, -592, 545, -491,
//      -685, 935, 539, -435, -763, -499, -354, 889;
//      578, 415, -361, 858, 22, 847, -846, 727, -452, 600, 532, 534, 750, -811, 574,
//      -477, -529, 520, -424, -444, 557, -893, -711, -619, -390""".filter { it !in listOf(' ', '\n') }
//        .split(";")
//        .map { axis ->
//          axis.split(",").map(String::toInt)
//        }

//    println(target)
//    println(axes)

//    day.checkAxis(target, axes[0])
//    day.checkAxis(target, axes[1])
//    day.checkAxis(target, axes[2])
//    day.checkAxis(target, axes[0].map { -it })
//    day.checkAxis(target, axes[1].map { -it })
//    day.checkAxis(target, axes[2].map { -it })

//    val best = day.bestAxis(target, axes)
//    println(best)

//    target = ("-588, -643, 591, -675, -823, -357, -311, -816, 649, -824, 345, 580, -329, 868, " +
//      "-627, 392, 729, 524, 845, -701, -33, 319, 580, 900, -707")
//      .filter { it != ' ' }
//      .split(",")
//      .map(String::toInt)
//
//    println()
//
//    day.checkAxis(target, axes[0])
//    day.checkAxis(target, axes[1])
//    day.checkAxis(target, axes[2])
//    day.checkAxis(target, axes[0].map { -it })
//    day.checkAxis(target, axes[1].map { -it })
//    day.checkAxis(target, axes[2].map { -it })
//
//    target = ("-901, 409, 734, -793, -458, 347, 381, -575, 763, -621, -567, 667, 318, -557, " +
//      "-890, -477, 728, 684, -530, 434, -71, -379, 662, -551, 401")
//      .filter { it != ' ' }
//      .split(",")
//      .map(String::toInt)
//
//    println()
//
//    day.checkAxis(target, axes[0])
//    day.checkAxis(target, axes[1])
//    day.checkAxis(target, axes[2])
//    day.checkAxis(target, axes[0].map { -it })
//    day.checkAxis(target, axes[1].map { -it })
//    day.checkAxis(target, axes[2].map { -it })

  }

}