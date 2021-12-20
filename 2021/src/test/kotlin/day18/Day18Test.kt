package day18

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Day18Test {
  val day = Day18()

  @Before
  fun setUp() {
  }

  @Test
  fun `can parse numbers`() {
    val num = day.parseNumber("1234".getBufferedIterator())
    assertEquals(1234, num)
  }

  @Test
  fun `can parse simple pair`() {
    val p = day.parsePair("[1,2]".getBufferedIterator())
    assertEquals(Spair(Value(1), Value(2)), p)
  }

  @Test
  fun `can parse mixed pair`() {
    val p = day.parsePair("[[1,2],3]".getBufferedIterator())
    val expected = Spair(Spair(Value(1), Value(2)), Value(3))
    assertEquals(expected, p)
  }

  @Test
  fun `can parse mixed pair 2`() {
    val p = day.parsePair("[1,[2,3]]".getBufferedIterator())
    val expected = Spair(Value(1), Spair(Value(2), Value(3)))
    assertEquals(expected, p)
  }

  @Test
  fun `can parse complex pair`() {
    val p = day.parsePair("[[1,2],[3,4]]".getBufferedIterator())
    val sp = Spair(Spair(Value(1), Value(2)),Spair(Value(3), Value(4)))
    assertEquals(sp, p)
  }

  @Test
  fun `add to first`() {
    assertEquals(
      "[9,[5,[4,[3,2]]]]",
      day.addToFirst(day.parse("[6,[5,[4,[3,2]]]]".getBufferedIterator()), 3).toString()
    )
  }

  @Test
  fun `explode`() {
    val tests = listOf(
      "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
      "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
      "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
      "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
      "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
    )

    tests.forEach { (input, expected) ->
      val exploded = day.explode(input, useString = false)
      assertEquals(expected, exploded)
    }
  }

  @Test
  fun `split`() {
    //    10 becomes [5,5], 11 becomes [5,6], 12 becomes [6,6], and so on.
    val tests = listOf(
      "10" to "[5,5]",
      "11" to "[5,6]",
      "12" to "[6,6]",
    )

    tests.forEach { (input, expected) ->
      assertEquals(expected, day.split(input))
    }
  }

  @Test
  fun reduce() {
    val tests = listOf(
      "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]" to "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
    )

    tests.forEach { (input, expected) ->
      assertEquals(expected, day.reduce(input))
    }
  }

  @Test
  fun sumAndReduce() {
    val input = """
      [1,1]
      [2,2]
      [3,3]
      [4,4]""".trimIndent().split('\n')

    assertEquals("[[[[1,1],[2,2]],[3,3]],[4,4]]", day.sumAndReduce(input))
  }

  @Test
  fun sumAndReduce2() {
    val input = """
      [1,1]
      [2,2]
      [3,3]
      [4,4]
      [5,5]""".trimIndent().split('\n')

    assertEquals("[[[[3,0],[5,3]],[4,4]],[5,5]]", day.sumAndReduce(input))
  }

  @Test
  fun sumAndReduce3() {
    val input = """
      [1,1]
      [2,2]
      [3,3]
      [4,4]
      [5,5]
      [6,6]""".trimIndent().split('\n')

    assertEquals("[[[[5,0],[7,4]],[5,5]],[6,6]]", day.sumAndReduce(input))
  }

  @Test
  fun magnitudeTest() {
    val cases = listOf(
      "[[1,2],[[3,4],5]]" to 143,
      "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" to 1384,
      "[[[[1,1],[2,2]],[3,3]],[4,4]]" to 445,
      "[[[[3,0],[5,3]],[4,4]],[5,5]]" to 791,
      "[[[[5,0],[7,4]],[5,5]],[6,6]]" to 1137,
      "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" to 3488,
    )

    cases.forEach { (sample, expected) ->
      assertEquals(expected, day.magnitude(sample))
    }
  }

  @Test
  fun sampleHomework() {
    val assignment = """
      [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
      [[[5,[2,8]],4],[5,[[9,9],0]]]
      [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
      [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
      [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
      [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
      [[[[5,4],[7,7]],8],[[8,3],8]]
      [[9,3],[[9,9],[6,[4,9]]]]
      [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
      [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]""".trimIndent().split("\n")

    val sum = day.sumAndReduce(assignment)
    assertEquals("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]", sum)
    val mag = day.magnitude(sum)
    assertEquals(4140, mag)
  }

  @Test
  fun broken() {

    val reduced = day.reduce("[[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]],[2,9]]")
    val expected = "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]"
    assertEquals(expected, reduced)

    //[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]
    //+ [2,9]
    //= [[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]
    val sum = day.sumAndReduce(
      listOf(
        "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]",
        "[2,9]"
  //      "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]",
  //      "[[2,[2,2]],[8,[8,1]]]"
      )
    )

//    val expected = "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]"
    assertEquals(expected, sum)
  }

  @Test
  fun complexTest() {
    val numbers = """
      [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
      [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
      [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
      [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
      [7,[5,[[3,8],[1,4]]]]
      [[2,[2,2]],[8,[8,1]]]
      [2,9]
      [1,[[[9,3],9],[[9,0],[0,7]]]]
      [[[5,[7,4]],7],1]
      [[[[4,2],2],6],[8,7]]""".trimIndent().split('\n')

//    assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", day.sumAndReduce(numbers))

    val steps = """
      [[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]
      [[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]
      [[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]
      [[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]
      [[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]
      [[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]
      [[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]
      [[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]
      [[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]
      """.trimIndent().split('\n')

    val nIter = numbers.iterator()

    var sum = nIter.next()

    steps.forEach { step ->
      val input = listOf(sum, nIter.next())
      sum = day.sumAndReduce(input)
      assertEquals(step, sum)
    }
  }
}