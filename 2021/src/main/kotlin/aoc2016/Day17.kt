package aoc2016

import java.security.MessageDigest
import kotlin.system.measureTimeMillis
import utils.Heading
import utils.Vector

class Day17 {
  companion object {
    fun run() {
      val time = measureTimeMillis {
        // Day17().part1()
        Day17().part2()
      }
      println("millis: $time")
    }
  }

  val top = 0
  val bottom = 3
  val left = 0
  val right = 3

  // val key = "hijkl"
  // val key = "ihgpwlah"
  // val key = "kglvqrro"
  // val key = "ulqzkmiv"

  // Real input
  val key = "awrkjxxr"

  val goal = Vector(right, bottom)

  private fun part1() {
    val options = mutableListOf<State>()

    options.add(State(Vector(0, 0), emptyList()))

    var success = false

    while (options.isNotEmpty()) {
      val currentState = options.first()
      options.removeAt(0)

      val location = currentState.location
      val path = currentState.path

      // println()

      // println(location)
      val pathString = path.joinToString("")
      // println(pathString)

      val doorCodes = hashCurrent(key, path).substring(0, 4)
      val nextSteps = doorCodes.map {
        if (it > 'a') DoorState.OPEN else DoorState.CLOSED
      }.mapIndexedNotNull { index, doorState ->
        if (doorState == DoorState.OPEN) {
          headingMap[index]
        } else {
          null
        }
      }.mapNotNull { heading ->
        val candidate = location.advance(heading)
        if (candidate.y in top..bottom && candidate.x in left..right) {
          candidate
        } else {
          null
        }
      }

      // up, down, left, right
      // println(nextSteps)

      if (nextSteps.contains(goal)) {
        println("success")
        val next = nextSteps.firstOrNull { it == goal }!!
        val delta = next - location
        val p = deltaToChar(delta)
        val newPath = path.toMutableList().also { it.add(p) }
        println(String(newPath.toCharArray()))
        success = true
        break
      }

      nextSteps.forEach { next ->
        val delta = next - location
        val p = deltaToChar(delta)
        val newPath = path.toMutableList().also { it.add(p) }

        options.add(State(next, newPath))
      }
    }

    if (!success) {
      println("failed")
    }

  }

  private fun part2() {
    val options = mutableListOf<State>()

    options.add(State(Vector(0, 0), emptyList()))

    val successfulPaths = mutableListOf<String>()

    while (options.isNotEmpty()) {
      val currentState = options.first()
      options.removeAt(0)

      val location = currentState.location
      val path = currentState.path

      // println()

      // println(location)
      val pathString = path.joinToString("")
      // println(pathString)

      val doorCodes = hashCurrent(key, path).substring(0, 4)
      val nextSteps = doorCodes.map {
        if (it > 'a') DoorState.OPEN else DoorState.CLOSED
      }.mapIndexedNotNull { index, doorState ->
        if (doorState == DoorState.OPEN) {
          headingMap[index]
        } else {
          null
        }
      }.mapNotNull { heading ->
        val candidate = location.advance(heading)
        if (candidate.y in top..bottom && candidate.x in left..right) {
          candidate
        } else {
          null
        }
      }

      nextSteps.forEach { next ->
        if (next == goal) {
          val delta = next - location
          val p = deltaToChar(delta)
          val newPath = path.toMutableList().also { it.add(p) }
          successfulPaths.add(String(newPath.toCharArray()))
        } else {
          val delta = next - location
          val p = deltaToChar(delta)
          val newPath = path.toMutableList().also { it.add(p) }

          options.add(State(next, newPath))
        }
      }
    }

    val longest = successfulPaths.maxByOrNull { it.length }
    if (longest != null) {
      println("success")
      println(longest.length)
    } else {
      println("fail")
    }
  }

  private fun deltaToChar(delta: Vector) = when (delta) {
    Heading.NORTH.vector -> 'U'
    Heading.SOUTH.vector -> 'D'
    Heading.WEST.vector -> 'L'
    Heading.EAST.vector -> 'R'
    else -> throw Exception("unexpected delta: $delta")
  }

  private fun hashCurrent(key: String, path: List<Char>): String {
    val s = key + String(path.toCharArray())
    return hash(s)
  }
}

data class State(val location: Vector, val path: List<Char>)

val headingMap = listOf(
  utils.Heading.NORTH, utils.Heading.SOUTH, utils.Heading.WEST, utils.Heading.EAST
)

private val md = MessageDigest.getInstance("MD5")

fun hash(trial: String): String {
  return md.digest(trial.toByteArray()).joinToString("") { "%02x".format(it) }
}

enum class DoorState {
  OPEN,
  CLOSED
}
