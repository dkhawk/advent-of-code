package day23

import kotlin.test.assertEquals
import org.junit.Test
import utils.COLORS
import utils.Vector

class Day23Test {
  val day = Day23()

  @Test
  fun `can get path`() {
    val path = day.getPath(Vector(5, 3), day.goalLocations['D']!!.first())
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
  fun `can find obstacles`() {
    val grid = day.getInput(useRealInput = false)
    println(grid)

    val path = day.getPath(Vector(5, 3), day.goalLocations['D']!!.last())

    val obstacles = day.getObstacles(path.drop(1), grid)
    println(obstacles)

    val expected = listOf(
      (Vector(x=5, y=2) to 'C'), (Vector(x=9, y=2) to 'D'), (Vector(x=9, y=3) to 'A')
    )
    assertEquals(expected, obstacles)

//    val pathGrid = day.emptyGrid.copy()
//    path.forEach {
//      pathGrid[it] = '*'
//    }
//
//    obstacles.forEach {
//      pathGrid[it.first] = it.second
//    }
//
//    println(
//      pathGrid.toStringWithMultipleHighlights(
//        COLORS.LT_GREEN.toString() to { c, v -> c == '*' },
//        COLORS.LT_RED.toString() to { c, v -> c in day.amphipods },
//      )
//    )
  }

   @Test
   fun `move to closest open safe cell`() {
     val grid = day.getInput(useRealInput = false)
     println(grid)

     val path = day.getPath(Vector(5, 3), day.goalLocations['D']!!.last())

     val obstacles = day.getObstacles(path.drop(1), grid)
     println(obstacles)

     val expected = listOf(
       (Vector(x=5, y=2) to 'C'), (Vector(x=9, y=2) to 'D'), (Vector(x=9, y=3) to 'A')
     )
     assertEquals(expected, obstacles)

     day.clearPath(path, obstacles, grid)
   }

  @Test
  fun `move off path`() {
    val grid = day.getInput(useRealInput = false)
    println(grid)

    val obstacles = listOf(
      (Vector(x=5, y=2) to 'C'), (Vector(x=9, y=2) to 'D'), (Vector(x=9, y=3) to 'A')
    ).sortedByDescending { it.second }

    obstacles.forEach { obstacle ->
      val path = day.getPath(Vector(5, 3), day.goalLocations['D']!!.last())

      val movePath = day.moveOffPath(obstacle, path, grid)
      grid[obstacle.first] = '.'
      grid[movePath.first.last()] = obstacle.second

      println(grid)
    }
  }
}
