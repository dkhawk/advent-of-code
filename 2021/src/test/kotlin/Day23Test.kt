package day23

import kotlin.test.assertEquals
import org.junit.Test
import utils.Vector

class Day23Test {
  val day = Day23()

  @Test
  fun `can get path`() {
    val path = day.getPath(Vector(5, 3), Day23.goalLocations['D']!!.first())
    println(path)

    val expected = listOf(Vector(x=5, y=3), Vector(x=5, y=2), Vector(x=5, y=1), Vector(x=6, y=1),
                          Vector(x=7, y=1), Vector(x=8, y=1), Vector(x=9, y=1), Vector(x=9, y=2))

//    val pathGrid = day.emptyGrid.copy()
//    path.forEach {
//      pathGrid[it] = '*'
//    }
//    println(pathGrid.toStringWithHighlights { c, v -> c == '*' })
    assertEquals(expected, path)
  }

  @Test
  fun `can detect loops`() {
    val grid = day.sample
    println(grid)

    'B' to Day23.goalLocations['B']!![0]

    return

    val orderToSolve = listOf(
      'B' to 0, 'b' to 1, 'A' to 0, 'a' to 1, 'C' to 0, 'c' to 1, 'D' to 0, 'd' to 1,
//      'D' to 1, 'd' to 0, 'C' to 0, 'c' to 1, 'B' to 0, 'b' to 1, 'A' to 0, 'a' to 1,
    ).map { (amphipod, goalIndex) ->
      print("'$amphipod' to $goalIndex, ")
      amphipod to Day23.goalLocations[amphipod.uppercaseChar()]!![goalIndex]
    }
    val (solvedGrid, paths) = day.solve(orderToSolve, grid)
    day.replayPaths(paths, grid, showEachMove = false)
    day.calculateCost(paths, grid)
  }

  @Test
  fun `can solved a goal`() {
    val goals = listOf(
      'D' to 1, 'd' to 0, 'C' to 0, 'c' to 1, 'B' to 0, 'b' to 1,
      'A' to 0, 'a' to 1,
    ).map { Day23.Goal(it) }

    val world = Day23.WorldState(day.sample, goals.drop(1).take(1))

    day.solve(world)
  }
}
