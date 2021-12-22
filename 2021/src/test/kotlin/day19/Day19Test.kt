package day19

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Day19Test {
  val day = Day19()

  @Test
  fun `find best axis`() {
    val target = ("404, 528, -838, 390, -537, -485, -345, -661, -876, -618, 553, 474, -447, -584, " +
      "544, 564, 455, -892, -689, 423, 7, 630, 443, -789, 459")
      .filter { it != ' ' }
      .split(",")
      .map(String::toInt)

    val axes = """686, 605, 515, -336, 95, -476, -340, 567, -460, 669, 729, -500, -322, -466, -429,
      -355, 703, -328, 413, -391, 586, -364, 807, 755, 553;
      422, 423, 917, 658, 138, 619, -569, -361, 603, -402, 430, -761, 571, -666, -592, 545, -491, 
      -685, 935, 539, -435, -763, -499, -354, 889;
      578, 415, -361, 858, 22, 847, -846, 727, -452, 600, 532, 534, 750, -811, 574,
      -477, -529, 520, -424, -444, 557, -893, -711, -619, -390""".filter { it !in listOf(' ', '\n') }
        .split(";")
        .map { axis ->
          axis.split(",").map(String::toInt)
        }

//    println(target)
//    println(axes)

    day.checkAxis(target, axes[0])
    day.checkAxis(target, axes[1])
    day.checkAxis(target, axes[2])
    day.checkAxis(target, axes[0].map { -it })
    day.checkAxis(target, axes[1].map { -it })
    day.checkAxis(target, axes[2].map { -it })
  }
}