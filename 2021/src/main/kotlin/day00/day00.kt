package day00

import java.util.LinkedList
import java.util.Queue
import utils.CharGrid
import utils.Heading
import utils.Vector

class day00 {
  companion object {
    fun run() {
      day00().bfs()
      day00().dfs()
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

  val sample3 = """
    0 0 0	0	1	1	0	0 1 0
    0 0 0	0	1	1	0	0 1 0
    0 0 0	0	1	1	0	0 1 0
    0 0 0	0	0	1	0	0 0 0
    0 0 0	0	1	1	0	0 0 0
    0 0 0	0	1	1	0	0 0 0
    """.trimIndent().filter { it !in  listOf('\t', ' ') }.split("\n").filter { it.isNotBlank() }

  val sample4 = """
    0 0 0	0	1	1	0	0 1 0
    0 0 0	0	1	0	0	1 1 0
    0 0 0	0	1	0	1	1 1 0
    0 0 0	0	1	0	1 1 0 0
    0 0 0	0	0	0	1	1 0 0
    0 0 0	0	1	1	1	0 0 0
    """.trimIndent().filter { it !in  listOf('\t', ' ') }.split("\n").filter { it.isNotBlank() }

  private fun getInput(useRealInput: Boolean): CharGrid {
    return CharGrid(sample4)
  }

  private fun bfs() {
    val grid = getInput(useRealInput = false)
    println("=============== BFS =================")
    println(grid)

    val startBorder = getBorderBfs(grid, Vector(0, 0))
    val endBorder = getBorderBfs(grid, Vector(grid.width - 1, grid.height - 1))

//    println(startBorder)
//    println(endBorder)

    val smash = startBorder.intersect(endBorder)
    println(smash)
    println(smash.isNotEmpty())
    println("=============== BFS =================")
  }

  fun getBorderBfs(grid: CharGrid, origin: Vector): Set<Vector> {
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

    val pgrid = CharGrid(grid.width, grid.height, '.')
    plot(seen, pgrid, '*')

    val bs = border.joinToString(", ", prefix = "[", postfix = "]") { "(${it.x}, ${it.y})" }
    println("border: ${bs}")
    plot(border, pgrid,'#')

    println(pgrid)

    return border.toSet()
  }

  private fun dfs() {
    println("=============== DFS =================")
    val grid = getInput(useRealInput = false)
    println(grid)

    val startBorder = getBorderDfsInit(grid, Vector(0, 0), Heading.EAST)
    val endBorder = getBorderDfsInit(grid, Vector(grid.width - 1, grid.height - 1), Heading.WEST)

    val smash = startBorder.intersect(endBorder)
    println(smash)
    println(smash.isNotEmpty())
    println("=============== DFS =================")
  }

  fun getBorderDfsInit(grid: CharGrid, origin: Vector, initialHeading: Heading): MutableSet<Vector> {
    var heading = initialHeading
    val border = mutableSetOf<Vector>()

    val path = mutableListOf<Vector>()

    path.add(origin)
    path.last()

    do {
      val leftHeading = heading.turnLeft()
      val leftVector = leftHeading.vector + path.last()
      val leftCell = grid.getCellOrNull(leftVector)

      val forwardVector = heading.vector + path.last()
      val forwardCell = grid.getCellOrNull(forwardVector)

      if (leftCell != '0') {
        border.add(leftVector)
      }

      if (leftCell == '0') {
        // Move left
        // Turn left
        path.add(leftVector)
        heading = leftHeading
      } else if (forwardCell == '0') {
        path.add(forwardVector)
      } else {
        heading = heading.turnRight()
      }

    } while(path.last() != origin)

    val ps = path.joinToString(", ", prefix = "[", postfix = "]") { "(${it.x}, ${it.y})" }
    println("Path: ${ps}")

    val pgrid = CharGrid(grid.width, grid.height, '.')
    plot(path, pgrid, '*')

    val bs = border.joinToString(", ", prefix = "[", postfix = "]") { "(${it.x}, ${it.y})" }
    println("border: ${bs}")
    plot(border, pgrid,'#')

    println(pgrid)

    return border
  }

  private fun plot(locations: Collection<Vector>, grid: CharGrid, character: Char) {
    locations.forEach {
      if (grid.validLocation(it)) {
        grid.setCell(it, character)
      }
    }
  }
}
