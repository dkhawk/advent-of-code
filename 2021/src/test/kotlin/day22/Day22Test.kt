package day22

import day22.Day22.Cube
import org.junit.Test
import kotlin.test.assertEquals

class Day22Test {
  val day = Day22()

  @Test
  fun `volume of cube`() {
    val cube = Cube(10, 12 + 1, 10, 12 + 1, 10, 12 + 1)

    assertEquals(27, cube.volume)
  }

  @Test
  fun `overlapping cubes`() {
    val cube = Cube(0, 3, 0, 3, 0, 3)

    assertEquals(false, cube.overlap(Cube(-1,0, -1, 0, -1, 0)))
    assertEquals(true, cube.overlap(Cube(-1,4, -1, 4, -1, 4)))
    assertEquals(false, cube.overlap(Cube(-1,0, -1, 4, -1, 4)))
  }

  @Test
  fun `slice with x`() {
    val cube = Cube(10, 12 + 1, 10, 12 + 1, 10, 12 + 1)

    assertEquals(setOf(cube), cube.slice(10, null, null))

    assertEquals(setOf(
      cube.copy(x1 = 11),
      cube.copy(x0 = 11),
    ), cube.slice(11, null, null))
  }

  @Test
  fun `slice with y`() {
    val cube = Cube(10, 12 + 1, 10, 12 + 1, 10, 12 + 1)

    assertEquals(setOf(
      cube.copy(y1 = 12),
      cube.copy(y0 = 12),
    ), cube.slice(null, 12, null))

    assertEquals(setOf(
      cube
    ), cube.slice(null, 13, null))
  }

  @Test
  fun `slice with z`() {
    val cube = Cube(10, 12 + 1, 10, 12 + 1, 10, 12 + 1)

    assertEquals(setOf(
      cube.copy(z1 = 12),
      cube.copy(z0 = 12),
    ), cube.slice(null, null, 12))

    assertEquals(setOf(
      cube
    ), cube.slice(null, null, 13))
  }

  @Test
  fun `multiple slices small cube from big cube`() {
    val cube = Cube(0, 3, 0, 3, 0, 3)
    assertEquals(27, cube.volume)

    assertEquals(
      setOf(
        Cube(0, 2, 0, 1, 0, 3),
        Cube(2, 3, 0, 1, 0, 3),
        Cube(0, 2, 1, 3, 0, 3),
        Cube(2, 3, 1, 3, 0, 3),
      ),
      cube.slice(2, 1, null)
    )
  }

  @Test
  fun `multiple slices big cube from small cube`() {
    val cube = Cube(0, 1, 0, 1, 0, 1)
    assertEquals(1, cube.volume)

    assertEquals(
      setOf(
        cube,
      ),
      cube.slice(3, 3, null)
    )
  }

  @Test
  fun `subtract a small cube from a bigger cube`() {
    val cube = Cube(0, 3, 0, 3, 0, 1)
    assertEquals(9, cube.volume)

    val smallerCube = Cube(0, 1, 0, 1, 0, 1)

    assertEquals(
      setOf(
        Cube(x0 = 0, x1 = 1, y0 = 1, y1 = 3, z0 = 0, z1 = 1),
        Cube(x0 = 1, x1 = 3, y0 = 0, y1 = 1, z0 = 0, z1 = 1),
        Cube(x0 = 1, x1 = 3, y0 = 1, y1 = 3, z0 = 0, z1 = 1),
      ),
      cube.subtract(smallerCube)
    )
  }

  @Test
  fun `subtract corner intersecting cubes`() {
    val cube1 = Cube(0, 3, 0, 3, 0, 1)
    val cube2 = Cube(-2, 1, -2, 1, 0, 1)

    assertEquals(setOf(
      Cube(x0 = 0, x1 = 1, y0 = 1, y1 = 3, z0 = 0, z1 = 1),
      Cube(x0 = 1, x1 = 3, y0 = 0, y1 = 1, z0 = 0, z1 = 1),
      Cube(x0 = 1, x1 = 3, y0 = 1, y1 = 3, z0 = 0, z1 = 1)
    ), cube1.subtract(cube2))
  }

  @Test
  fun `add corner intersection cubes`() {
    val cube1 = Cube(0, 3, 0, 3, 0, 1)
    val cube2 = Cube(-2, 1, -2, 1, 0, 1)

    assertEquals(setOf(
      Cube(x0 = 0, x1 = 1, y0 = 0, y1 = 1, z0 = 0, z1 = 1),
      Cube(x0 = 0, x1 = 1, y0 = 1, y1 = 3, z0 = 0, z1 = 1),
      Cube(x0 = 1, x1 = 3, y0 = 0, y1 = 1, z0 = 0, z1 = 1),
      Cube(x0 = 1, x1 = 3, y0 = 1, y1 = 3, z0 = 0, z1 = 1),

      Cube(x0 = -2, x1 = 0, y0 = -2, y1 = 0, z0 = 0, z1 = 1),
      Cube(x0 = -2, x1 = 0, y0 =  0, y1 = 1, z0 = 0, z1 = 1),
      Cube(x0 =  0, x1 = 1, y0 = -2, y1 = 0, z0 = 0, z1 = 1)
    ), cube1.add(cube2))
  }

  @Test
  fun `add cubes non intersecting`() {
    val cube1 = Cube(10, 12 + 1, 10, 12 + 1, 10, 12 + 1)
    val cube2 = Cube(13, 15 + 1, 13, 15 + 1, 13, 15 + 1)

    assertEquals(cube1.add(cube2), setOf(cube1, cube2))
  }

  @Test
  fun `subtract cubes non intersecting`() {
    val cube1 = Cube(10, 12 + 1, 10, 12 + 1, 10, 12 + 1)
    val cube2 = Cube(13, 15 + 1, 13, 15 + 1, 13, 15 + 1)

    assertEquals(cube1.subtract(cube2), setOf(cube1))
  }

  @Test
  fun `multiple adds`() {
    val cubes = listOf(
      Cube(x0=-20, x1=27, y0=-36, y1=18, z0=-47, z1=8),
      Cube(x0=-20, x1=34, y0=-21, y1=24, z0=-26, z1=29),
      Cube(x0=-22, x1=29, y0=-29, y1=24, z0=-38, z1=17),
    )

    val set2 = cubes[0].add(cubes[1])
    println(set2.joinToString("\n"))

    println("==")
    println(set2.combinations().map { (a, b) ->
      a.overlap(b)
    })

    val newSet = set2.flatMap { onCube ->
      onCube.subtract(cubes[2])
    }.toSet().toMutableSet()
    newSet.add(cubes[2])

    println(newSet.sumOf { it.volume })  // 225476
  }
}

private fun <E> Collection<E>.combinations(): List<Pair<E, E>> {
  if (size < 2) {
    return emptyList()
  }
  val first = this.first()
  return this.drop(1).map {
    first to it
  } + this.drop(1).combinations()
}
