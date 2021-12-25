package day23

import java.util.LinkedList
import java.util.Queue
import kotlin.system.measureTimeMillis
import utils.COLORS
import utils.CharGrid
import utils.Vector

@OptIn(ExperimentalStdlibApi::class)
class Day23 {
  companion object {
    fun run() {
      measureTimeMillis {
        Day23().part1()
      }.also {
        println("millis: $it")
      }
      measureTimeMillis {
    //  Day23().part2()
      }.also {
        println("millis: $it")
      }
    }
  }

  val sample = """
    #############
    #...........#
    ###b#c#B#D###
    ###a#d#C#A###
    #############""".trimIndent().split("\n").filter { it.isNotBlank() }

  val solved = """
    #############
    #...........#
    ###A#B#C#D###
    ###A#B#C#D###
    #############""".trimIndent().split("\n").filter { it.isNotBlank() }

  val real = """
    #############
    #...........#
    ###a#d#A#B###
    ###b#c#D#C###
    #############""".trimIndent().split("\n").filter { it.isNotBlank() }

  fun getInput(useRealInput: Boolean): CharGrid {
    val input = if (useRealInput) {
      real
    } else {
      sample
    }

    return CharGrid(input)
  }

  val movementCost = mapOf(
    'A' to 1,
    'B' to 10,
    'C' to 100,
    'D' to 1000,
  )

  val amphipods = movementCost.keys.flatMap { listOf(it, it.lowercaseChar()) }.toSet()

  val solvedGrid = CharGrid(solved)

  val emptyGrid = CharGrid(solvedGrid.grid.map {
    if (it !in listOf('.', '#')) {
      '.'
    } else {
      it
    }
  }.toCharArray(), solvedGrid.width)

  val roomDoors = listOf(
    Vector(3, 1),
    Vector(5, 1),
    Vector(7, 1),
    Vector(9, 1),
  )

  val safeSpaces = emptyGrid.copy().also { grid ->
    roomDoors.forEach {
      grid[it] = 'x'
    }
  }

  val goalLocations = mapOf(
    'A' to listOf(Vector(3, 2), Vector(3, 3)),
    'B' to listOf(Vector(5, 2), Vector(5, 3)),
    'C' to listOf(Vector(7, 2), Vector(7, 3)),
    'D' to listOf(Vector(9, 2), Vector(9, 3)),
  )

  private val roomCells = goalLocations.values.flatten().toSet()

  private fun solutionSequence(): Sequence<List<Pair<Char, Int>>> {
    val amphipods = "DCBA"
    val permutations = amphipods.map { amphipod ->
      permutations(amphipod)
    }

    val indices = IntArray(amphipods.length)

    return sequence {
      while (true) {
        yield(indices.mapIndexed { index, value -> permutations[index][value] }.flatten())

        var i = indices.lastIndex
        while (true) {
          indices[i] = (indices[i] + 1) % 4
          if (indices[i] > 0) {
            break
          }
          i -= 1
          if (i < 0) {
            return@sequence
          }
        }
      }
    }
  }

  private fun part1() {
    val grid = getInput(useRealInput = false)
    println(grid)

    val s = solutionSequence()

    val orderToSolve = s.first().map { (amphipod, goalIndex) ->
      amphipod to goalLocations[amphipod.uppercaseChar()]!![goalIndex]
    }

    val (solvedGrid, paths) = solve(orderToSolve, grid)
    replayPaths(paths, grid, showEachMove = false)
  }

  private fun solve(orderToSolve: List<Pair<Char, Vector>>, grid: CharGrid): Pair<CharGrid, List<List<Vector>>> {
    var currentGrid = grid.copy()
    val paths = mutableListOf<List<Vector>>()

    orderToSolve.indices.forEach { index ->
      (0..index).forEach {
        val (amphipod, goal) = orderToSolve[it]
        val start = whereIs(amphipod, currentGrid)
        if (start != goal) {
          val result = solveTask(start, goal, currentGrid)
          currentGrid = result.first
          if (result.second.isNotEmpty()) {
            paths.addAll(result.second)
          }
        }
      }
    }

    return currentGrid to paths
  }

  private fun solveTask(
    start: Vector,
    goal: Vector,
    grid: CharGrid
  ): Pair<CharGrid, List<List<Vector>>> {
    if (start == goal) {
      return grid to emptyList()
    }

    val path = getPath(start, goal)
    return executeMove(path, grid)
  }

  private fun permutations(amphipod: Char): List<List<Pair<Char, Int>>> {
    return listOf(
      listOf(
        amphipod to 0,
        amphipod.lowercaseChar() to 1,
      ),
      listOf(
        amphipod.lowercaseChar() to 1,
        amphipod to 0,
      ),
      listOf(
        amphipod to 1,
        amphipod.lowercaseChar() to 0,
      ),
      listOf(
        amphipod.lowercaseChar() to 0,
        amphipod to 1,
      )
    )
  }

  private fun solveFor(
    amphipod: Char,
    goal: Vector,
    grid: CharGrid,
    previouslySolved: List<Pair<Char, Vector>>
  ): Pair<CharGrid, List<List<Vector>>> {
    // Solve for the upper case version
    val start = whereIs(amphipod, grid)
    if (start == goal) {
      return grid to emptyList()
    }

    var outputGrid = grid.copy()
    var path = getPath(start, goal)
    var result = executeMove(path, outputGrid)
    outputGrid = result.first
    val paths = result.second.toMutableList()

    // Make sure all the previously solved states are still solved!
    previouslySolved.forEach { previous ->
      result = solveFor(previous.first, previous.second, outputGrid, emptyList())
    }

    replayPaths(paths, grid)

    return result

//    val (a, b) = getLocationsFor(amphipod, grid)
//
//    val targets = goalLocations[amphipod]!!
//
//    val goals = listOf(
//      // listOf(a to targets.first(), b to targets[1]),
//      listOf(b to targets.first(), a to targets[1]),
//    )
//
//    goals.forEach { goal ->
//      var outputGrid = grid.copy()
//
//      // TODO() execute these in the opposite order as well!
//
//      var objective = goal[0]
//      // This is the path that we want to execute
//      var path = getPath(objective.first, objective.second)
//      var result = executeMove(path, outputGrid)
//      outputGrid = result.first
//      val paths = result.second.toMutableList()
//
//      objective = goal[1]
//      // This is the path that we want to execute
//      path = getPath(objective.first, objective.second)
//      result = executeMove(path, outputGrid)
//      println(outputGrid)
//      paths.addAll(result.second)
//      println(paths)
//    }
  }

  private fun replayPaths(paths: List<List<Vector>>, grid: CharGrid, showEachMove: Boolean = true) {
    val outputGrid = grid.copy()
    paths.forEach { path ->
      if (showEachMove) {
        path.windowed(2, 1) { steps ->
          outputGrid[steps.last()] = outputGrid[steps.first()]
          outputGrid[steps.first()] = '.'
          println(
            outputGrid.toStringWithMultipleHighlights(
              COLORS.INVERTED_GREEN.toString() to { c, v -> v == steps.last() },
              COLORS.INVERTED_RED.toString() to { c, v -> v == steps.first() },
            )
          )
        }
      } else {
        outputGrid[path.last()] = outputGrid[path.first()]
        outputGrid[path.first()] = '.'
        println(
          outputGrid.toStringWithMultipleHighlights(
            COLORS.INVERTED_RED.toString() to { c, v -> v in path },
          )
        )
      }
    }
  }

  private fun whereIs(amphipod: Char, grid: CharGrid): Vector {
    return grid.findCharacter(amphipod)!!
  }

  private fun showPath(path: List<Vector>, grid: CharGrid) {
    val pathGrid = grid.copy()
    path.forEach {
      pathGrid[it] = '*'
    }

    println(
      pathGrid.toStringWithMultipleHighlights(
        COLORS.LT_GREEN.toString() to { c, v -> c == 'o' },
        COLORS.LT_RED.toString() to { c, v -> c == '*' },
      )
    )
  }

  private fun executeMove(
    path: List<Vector>,
    inputGrid: CharGrid,
  ): Pair<CharGrid, List<List<Vector>>> {
    if (path.size <= 1) {
      // The goal and destination are the same!
      return inputGrid to listOf(path)
    }

    val (clearedGrid, obstaclePaths) = clearPath2(path, inputGrid)
    val outputGrid = clearedGrid.copy().also { grid ->
      grid[path.last()] = grid[path.first()]
      grid[path.first()] = '.'
    }
    val paths = obstaclePaths.toMutableList().also {it.add(path)}

    return outputGrid to paths
  }

  fun getPath(start: Vector, target: Vector): List<Vector> {
    val queue: Queue<Vector> = LinkedList()
    queue.add(start)
    val seen = mutableSetOf<Vector>()
    val paths = mutableMapOf<Vector, Vector>()

    while (queue.isNotEmpty()) {
      val location = queue.remove()
      seen.add(location)

      if (location == target) {
        val path = mutableListOf<Vector>()
        path.add(location)
        while (path.last() != start) {
          path.add(paths[path.last()]!!)
        }
        return path.reversed()
      }

      emptyGrid.getNeighborsWithLocation(location).forEach { neighbor ->
        if (neighbor.second == '.') {
          val loc = neighbor.first
          if (loc !in seen && loc !in queue) {
            queue.add(loc)
            paths[loc] = location
          }
        }
      }
    }

    throw Exception("Path not found")
  }

  fun getObstacles(path: List<Vector>, grid: CharGrid): List<Pair<Vector, Char>> {
    return path.mapNotNull { location ->
      if (grid[location] in amphipods) {
        location to grid[location]
      } else {
        null
      }
    }
  }

  private fun getLocationsFor(amphipod: Char, grid: CharGrid): Pair<Vector, Vector> {
    return grid.findAll { i, c -> c == amphipod }.map { grid.indexToVector(it.first) }.toPair()
  }

  private fun part2() {
    val grid = getInput(useRealInput = false)
    println(grid)
    TODO("Not yet implemented")
  }

  fun clearPath(path: List<Vector>, obstacles: List<Pair<Vector, Char>>, grid: CharGrid) {
    // move the obstacles starting with the most expensive
    val sorted = obstacles.sortedByDescending { movementCost[it.second] }

    sorted.take(1).forEach { obstacle ->
      moveOffPath(obstacle, path, grid)
    }
  }

  fun clearPath2(path: List<Vector>, grid: CharGrid): Pair<CharGrid, List<List<Vector>>> {
    var outputGrid = grid.copy()
    val paths = mutableListOf<List<Vector>>()
    while (true) {
      val obstacles = getObstacles(path.drop(1), outputGrid).sortedByDescending { it.second }
      if (obstacles.isNotEmpty()) {
        val (newGrid, obstaclePath) = moveObstacleToFreeSpace(obstacles.first(), outputGrid, path)
        outputGrid = newGrid
        paths.addAll(obstaclePath)
      } else {
        break
      }
    }
    return outputGrid to paths
  }

  private fun moveObstacleToFreeSpace(
    obstacle: Pair<Vector, Char>,
    inputGrid: CharGrid,
    path: List<Vector>,
  ): Pair<CharGrid, List<List<Vector>>> {
    val start = obstacle.first
    val amphipod = obstacle.second

    val goalGrid = safeSpaces.copy().also { grid ->
      // Block any room except the room where the amphipod can go
      roomCells.forEach {
        grid[it] = 'X'
      }

      // Block the path cells
      path.forEach {
        grid[it] = '*'
      }
    }

    val obstaclePath = pathToClosestFreeCell(start, goalGrid)
    return executeMove(obstaclePath, inputGrid)
  }

  fun moveOffPath(obstacle: Pair<Vector, Char>, path: List<Vector>, grid: CharGrid): Pair<List<Vector>, Int> {
    val start = obstacle.first
    val amphipod = obstacle.second

    val goalGrid = safeSpaces.copy().also { grid ->
      // Block any room except the room where the amphipod can go
      roomCells.forEach {
        grid[it] = 'X'
      }

      // Block the path cells
      path.forEach {
        grid[it] = '*'
      }
    }

    println(goalGrid)
    val path = pathToClosestFreeCell(start, goalGrid)

    val pathGrid = goalGrid.copy()
    path.forEach {
      pathGrid[it] = 'o'
    }

    println(
      pathGrid.toStringWithMultipleHighlights(
        COLORS.LT_GREEN.toString() to { c, v -> c == 'o' },
        COLORS.LT_RED.toString() to { c, v -> c == '*' },
      )
    )


    do {
      val obstacles = getObstacles(path.drop(1), grid).sortedByDescending { it.second }
      val move = moveOffPath(obstacles.first(), path, goalGrid)
      grid[move.first.last()] = grid[move.first.first()]
      grid[move.first.first()] = '.'
    } while (obstacles.isNotEmpty())

    val cost = path.drop(1).size * movementCost[amphipod]!!
    return path to cost
  }

  fun pathToClosestFreeCell(start: Vector, goalGrid: CharGrid): List<Vector> {
    val queue: Queue<Vector> = LinkedList()
    queue.add(start)
    val seen = mutableSetOf<Vector>()
    val paths = mutableMapOf<Vector, Vector>()

    while (queue.isNotEmpty()) {
      val location = queue.remove()
      seen.add(location)

      if (goalGrid[location] == '.') {
        val path = mutableListOf<Vector>()
        path.add(location)
        while (path.last() != start) {
          path.add(paths[path.last()]!!)
        }
        return path.reversed()
      }

      goalGrid.getNeighborsWithLocation(location).forEach { neighbor ->
        if (neighbor.second in listOf('*', '.', 'x')) {
          val loc = neighbor.first
          if (loc !in seen && loc !in queue) {
            queue.add(loc)
            paths[loc] = location
          }
        }
      }
    }

    throw Exception("Path not found")
  }

}

private fun <E> List<E>.toPair(): Pair<E, E> {
  return this[0] to this[1]
}
