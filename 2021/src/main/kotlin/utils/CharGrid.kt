package utils

import kotlin.math.abs
import kotlin.math.sign

data class Vector(val x: Int, val y: Int) {
  val sign: Vector
    get() = Vector(x.sign, y.sign)

  operator fun times(scale: Int): Vector {
    return Vector(x * scale, y * scale)
  }

  operator fun minus(other: Vector): Vector = Vector(x - other.x, y - other.y)
  operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)

  fun advance(heading: Heading): Vector {
    return this + heading.vector
  }

  fun directionTo(end: Vector): Vector = (end - this)
}

enum class Direction {
  NORTH, EAST, SOUTH, WEST
}

enum class Heading(val vector: Vector) {
  NORTH(Vector(0, -1)),
  EAST(Vector(1, 0)),
  SOUTH(Vector(0, 1)),
  WEST(Vector(-1, 0));

  fun turnRight(): Heading {
    return values()[(this.ordinal + 1) % values().size]
  }

  fun turnLeft(): Heading {
    return values()[(this.ordinal + values().size - 1) % values().size]
  }

  fun opposite() : Heading {
    return values()[(this.ordinal + 2) % values().size]
  }

  fun turnTo(other: Heading): Int {
    return other.ordinal - ordinal
  }
}

data class Vector3d(val x: Long, val y: Long, val z: Long) {
  operator fun plus(other: Vector3d): Vector3d = Vector3d(x + other.x, y + other.y, z + other.z)

  fun distanceTo(other: Vector3d): Long = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

  override fun toString(): String = "<$x,$y,$z>"
}

enum class Heading8(val vector: Vector) {
  NORTH(Vector(0, -1)),
  NORTHEAST(Vector(1, -1)),
  EAST(Vector(1, 0)),
  SOUTHEAST(Vector(1, 1)),
  SOUTH(Vector(0, 1)),
  SOUTHWEST(Vector(-1, 1)),
  WEST(Vector(-1, 0)),
  NORTHWEST(Vector(-1, -1));

  fun turnRight(): Heading8 {
    return values()[(this.ordinal + 1) % values().size]
  }

  fun turnLeft(): Heading8 {
    return values()[(this.ordinal + values().size - 1) % values().size]
  }
}


interface Grid<T> {
  fun coordsToIndex(x: Int, y: Int) : Int
  fun setIndex(index: Int, value: T)
  fun getIndex(index: Int): T
}

class CharGrid() : Grid<Char> {
  var width: Int = 0
  var height: Int = 0
  var grid : CharArray = CharArray(width * height)

  constructor(input: CharArray, width: Int, height: Int? = null) : this() {
    grid = input.clone()
    this.width = width
    this.height = height ?: (grid.size / width)
  }

  constructor(inputLines: List<String>) : this() {
    width = inputLines.first().length
    height = inputLines.size
    grid = inputLines.map(String::toList).flatten().toCharArray()
  }

  constructor(size: Int, default: Char = ' ') : this() {
    width = size
    height = size
    grid = CharArray(width * height)
    grid.fill(default)
  }

  constructor(width: Int, height: Int, default: Char = '.') : this() {
    this.width = width
    this.height = height
    grid = CharArray(width * height)
    grid.fill(default)
  }

  override fun toString(): String {
    val output = StringBuilder()
    output.append("$width, $height\n")
    grid.toList().windowed(width, width) {
      output.append(it.joinToString("")).append('\n')
    }

    return output.toString()
  }

  operator fun get(Vector: Vector): Char = getCell(Vector)

  fun findCharacter(target: Char): Vector? {
    val index = grid.indexOf(target)
    if (index == -1) {
      return null
    }
    return indexToVector(index)
  }

  fun indexToVector(index: Int): Vector {
    val y = index / width
    val x = index % width
    return Vector(x, y)
  }

  fun setCell(Vector: Vector, c: Char) {
    grid[VectorToIndex(Vector)] = c
  }

  fun VectorToIndex(Vector: Vector): Int {
    return Vector.x + Vector.y * width
  }

  fun getCell(Vector: Vector): Char {
    return grid[VectorToIndex(Vector)]
  }

  fun getCellOrNull(Vector: Vector): Char? {
    if (!validLocation(Vector)) {
      return null
    }
    return grid[VectorToIndex(Vector)]
  }

  fun getNeighbors(index: Int): List<Char> {
    val Vector = indexToVector(index)
    return getNeighbors(Vector)
  }

  fun getCell_xy(x: Int, y: Int): Char {
    return grid[coordsToIndex(x, y)]
  }

  fun getCell_row_col(x: Int, y: Int): Char {
    return grid[coordsToIndex(x, y)]
  }

  override fun coordsToIndex(x: Int, y: Int): Int {
    return x + y * width
  }

  fun getNeighbors(Vector: Vector): List<Char> {
    return Heading.values().map { heading->
      getCell(Vector + heading.vector)
    }
  }

  fun getNeighborsIf(Vector: Vector, predicate: (Char, Vector) -> Boolean): List<Vector> {
    return Heading.values().mapNotNull { heading->
      val loc = Vector + heading.vector
      if (validLocation(loc) && predicate(getCell(loc), loc)) {
        loc
      } else {
        null
      }
    }
  }

  override fun getIndex(index: Int): Char {
    return grid[index]
  }

  override fun setIndex(index: Int, value: Char) {
    grid[index] = value
  }

  fun initialize(input: String) {
    input.forEachIndexed{ i, c -> if (i < grid.size) grid[i] = c }
  }

  fun getNeighbors8(Vector: Vector, default: Char): List<Char> {
    return Heading8.values()
      .map { heading-> Vector + heading.vector }
      .map { neighborVector ->
        if (validLocation(neighborVector)) getCell(neighborVector) else default
      }
  }

  fun validLocation(Vector: Vector): Boolean  {
    return Vector.x < width && Vector.y < height && Vector.x >= 0 && Vector.y >= 0
  }

  fun copy(): CharGrid {
    return CharGrid(grid, width, height)
  }

  fun getNeighbors8(index: Int): List<Char> {
    val Vector = indexToVector(index)
    return Heading8.values()
      .map { heading-> Vector + heading.vector }
      .mapNotNull { neighborVector ->
        if (validLocation(neighborVector)) getCell(neighborVector) else null
      }
  }

  fun advance(action: (index: Int, Vector: Vector, Char) -> Char): CharGrid {
    val nextGrid = CharArray(width * height)
    for ((index, item) in grid.withIndex()) {
      val loc = indexToVector(index)
      nextGrid[index] = action(index, loc, item)
    }
    return CharGrid(nextGrid, width, height)
  }

  fun sameAs(other: CharGrid): Boolean {
    return grid contentEquals other.grid
  }

  fun getBorders(): List<Int> {
    val trans = mapOf('.' to '0', '#' to '1')

    return listOf(
      (0 until width).mapNotNull { trans[getCell_xy(it, 0)] },
      (0 until height).mapNotNull { trans[getCell_xy(width - 1, it)] },
      (0 until width).mapNotNull { trans[getCell_xy(it, height - 1)] },
      (0 until height).mapNotNull { trans[getCell_xy(0, it)] },
    ).flatMap {
      listOf(it, it.reversed())
    }.map { it.joinToString("") }
      .map { it.toInt(2) }
  }

  fun rotate(rotation: Int): CharGrid {
    var r = rotation
    var src = this
    if (r < 0) {
      while (r < 0) {
        val nextGrid = CharGrid(CharArray(width * height), width, height)
        (0 until height).forEach { row ->
          val rowContent = src.getRow(row).reversedArray()
          nextGrid.setColumn(row, rowContent)
        }
        r += 1
        src = nextGrid
      }
    } else {
      while (r > 0) {
        val nextGrid = CharGrid(CharArray(width * height), width, height)
        (0 until height).forEach { row ->
          val rowContent = src.getRow(row)
          nextGrid.setColumn((width - 1) - row, rowContent)
        }
        r -= 1
        src = nextGrid
      }
    }

    return src
  }

  private fun setColumn(col: Int, content: CharArray) {
    var index = col
    repeat(height) {
      grid[index] = content[it]
      index += width
    }
  }

  private fun getRow(row: Int): CharArray {
    val start = row * width
    return grid.sliceArray(start until (start + width))
  }

  private fun setRow(row: Int, content: CharArray) {
    val start = row * width
    val end = start + width
    (start until end).forEachIndexed { index, it -> grid[it] = content[index] }
  }

  fun getBorder(heading: Heading) {
    val trans = mapOf('.' to '0', '#' to '1')

    val thing = when (heading) {
      Heading.NORTH -> (0 until width).mapNotNull { trans[getCell_xy(it, 0)] }
      Heading.EAST -> (0 until height).mapNotNull { trans[getCell_xy(width - 1, it)] }
      Heading.SOUTH -> (0 until width).mapNotNull { trans[getCell_xy(it, height - 1)] }
      Heading.WEST ->  (0 until height).mapNotNull { trans[getCell_xy(0, it)] }
    }.joinToString("")
    println(thing)
//      .map { it.toInt(2) }
  }

  fun flipAlongVerticalAxis(): CharGrid {
    var i = 0
    var j = width - 1
    val nextGrid = CharGrid(CharArray(width * height), width, height)
    while (i < j) {
      nextGrid.setColumn(j, getColumn(i))
      nextGrid.setColumn(i, getColumn(j))
      i++
      j--
    }
    if (i == j) {
      nextGrid.setColumn(i, getColumn(i))
    }
    return nextGrid
  }

  fun flipAlongHorizontalAxis(): CharGrid {
    var i = 0
    var j = height - 1
    val nextGrid = CharGrid(CharArray(width * height), width, height)
    while (i < j) {
      nextGrid.setRow(j, getRow(i))
      nextGrid.setRow(i, getRow(j))
      i++
      j--
    }
    if (i == j) {
      nextGrid.setRow(i, getRow(j))
    }
    return nextGrid
  }


  private fun getColumn(col: Int): CharArray {
    val result = CharArray(height)
    var index = col
    repeat(height) {
      result[it] = grid[index]
      index += width
    }
    return result
  }

  fun stripBorder(): CharGrid {
    val nextWidth = width - 2
    val nextHeight = height - 2
    val nextGrid = CharGrid(CharArray(nextWidth * nextHeight), nextWidth, nextHeight)
    (1 .. nextHeight).forEach { row ->
      (1 .. nextHeight).forEach { col ->
        nextGrid.setCell_xy(col - 1, row - 1, getCell_xy(col, row))
      }
    }
    return nextGrid
  }

  fun setCell_xy(x: Int, y: Int, ch: Char) {
    grid[coordsToIndex(x, y)] = ch
  }

  fun getPermutations(): List<CharGrid> {
    return getFlips().flatMap {
      it.getRotations()
    }
  }

  private fun getFlips(): List<CharGrid> {
    return listOf(
      this,
      this.flipAlongHorizontalAxis(),
      this.flipAlongVerticalAxis(),
      this.flipAlongHorizontalAxis().flipAlongVerticalAxis()
    )
  }

  private fun getRotations(): List<CharGrid> {
    return (0..3).map {
      rotate(it)
    }
  }

  fun insertAt_xy(xOffset: Int, yOffset: Int, other: CharGrid) {
    repeat(other.height) { row ->
      val y = row + yOffset
      repeat(other.width) { col ->
        val x = col + xOffset
        setCell_xy(x, y, other.getCell_xy(col, row))
      }
    }
  }
}