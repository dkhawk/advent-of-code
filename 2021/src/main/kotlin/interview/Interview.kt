package interview

import java.util.Deque
import java.util.LinkedList
import java.util.Queue
import utils.CharGrid
import utils.Heading
import utils.Vector

class Interview {
  companion object {
    fun run() {
      Interview().bfs()
      //  DayInt().part2()
    }
  }

  val sample = """
    0	0	1	1	0	0
    0	0	1	1	0	0
    0	0	1	1	0	0
    0	0	1	1	0	0
    0	0	1	1	0	0
    0	0	1	1	0	0
    """.trimIndent().filter { it !in  listOf('\t', ' ') }.split("\n").filter { it.isNotBlank() }

  val sample2 = """
    0	0	1	1	0	0
    0	0	1	1	0	0
    0	0	1	1	0	0
    0	0	0	1	0	0
    0	0	1	1	0	0
    0	0	1	1	0	0
    """.trimIndent().filter { it !in  listOf('\t', ' ') }.split("\n").filter { it.isNotBlank() }

  private fun getInput(useRealInput: Boolean): CharGrid {
    return CharGrid(sample2)
  }

  private fun bfs() {
    val grid = getInput(useRealInput = false)
    println(grid)

    val startBorder = getBorder(grid, Vector(0, 0))
    val endBorder = getBorder(grid, Vector(grid.width - 1, grid.height - 1))

    println(startBorder)
    println(endBorder)

    val smash = startBorder.intersect(endBorder)
    println(smash)
  }

  fun getBorder(grid: CharGrid, origin: Vector): Set<Vector> {
    // Collect the boundary from the start
    val queue = LinkedList<Vector>() as Queue<Vector>
    queue.add(origin)
    val seen = mutableSetOf<Vector>()
    val border = mutableSetOf<Vector>()

    while (queue.isNotEmpty()) {
      val location = queue.remove()
      seen.add(location)

      val neighbors = grid.getNeighborsWithLocation(location)
      neighbors.forEach { cell ->
        if (cell.second == '0') {
          if (cell.first !in seen) {
            queue.add(cell.first)
          }
        } else {
          border.add(cell.first)
        }
      }
    }

    return border.toSet()
  }

  private fun dfs() {
    val grid = getInput(useRealInput = false)
    println(grid)

    val startBorder = getBorderDfsInit(grid, Vector(0, 0))
    val endBorder = getBorderDfsInit(grid, Vector(grid.width - 1, grid.height - 1))

    println(startBorder)
    println(endBorder)

//    val smash = startBorder.intersect(endBorder)
//    println(smash)
  }

  fun getBorderDfsInit(grid: CharGrid, origin: Vector, ): Boolean {
    var current = origin
    var heading = Heading.EAST
    val border = mutableSetOf<Vector>()

    do {
      val outsideDir = heading.turnLeft()
      val outsideVect = outsideDir.vector + current
      val cell = grid.getCellOrNull(outsideVect)
      if (cell == null || cell == '1') {
        border.add(outsideVect)
      }
    } while(current != origin)

    // Take the first step
    Heading.EAST.sweep().forEach { heading ->
      val neighbor = origin + heading.vector
      val cell = grid.getCellOrNull(neighbor)
      var success = false
      if (cell == null || cell == '1') {
        border.add(neighbor)
      } else {
        getBorderDfs(grid, neighbor, origin, border, heading)
      }
    }

    return false

//    // getBorderDfs(grid, origin, Heading.EAST, )
//
//    // Travel clockwise until we get back the origin
//    val path = LinkedList<Pair<Heading, Vector>>() as Deque<Pair<Heading, Vector>>
//    val turns = LinkedList<Vector>() as Deque<Vector>
//    path.push(Heading.EAST to origin)
//
//    val border = mutableSetOf<Vector>()
//
//    do {
//      val (heading, current) = path.peek()!!
//      // Can we turn left?
//
//      grid.getLeftNeighbor(current, heading)
//
//      heading.sweep().firstOrNull { h ->
//        val neighbor = h.vector + current
//        val cell = grid.getCell(neighbor)
//        if (cell == '1') {
//          border.add(neighbor)
//        } else {
//          turns.add(h)
//        }
//        cell == '0'
//      }
//
//      val leftNeighbor = current + heading.turnLeft().vector
//      if (grid.getCell(leftNeighbor) == '1') {
//        path.push(leftNeighbor)
//      } else
//    } while (current != origin)
//
//    return emptySet<Vector>()
  }

  private fun getBorderDfs(
    grid: CharGrid,
    next: Vector,
    origin: Vector,
    border: MutableSet<Vector>,
    heading: Heading,
  ) : Boolean {
    if (next == origin) {

    }
     return false
  }
}

private fun Heading.sweep(): List<Heading> {
  return listOf(
    this.turnLeft(),
    this,
    this.turnRight()
  )
}

private fun CharGrid.getLeftNeighbor(location: Vector, heading: Heading): Pair<Vector, Char> {
  val neighbor = location + heading.turnLeft().vector
  val cell = getCell(neighbor)

  return neighbor to cell
}

private fun CharGrid.getStraightNeighbor(location: Vector, heading: Heading): Pair<Vector, Char> {
  val neighbor = location + heading.vector
  val cell = getCell(neighbor)

  return neighbor to cell
}
