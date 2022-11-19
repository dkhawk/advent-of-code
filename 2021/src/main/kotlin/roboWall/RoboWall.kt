package roboWall

import utils.CharGrid

class RoboWall {
  val sample = """
    ..11..
    ..11..
    ..11..
    ...1..
    ..11..
    ..11..""".trimIndent().split("\n").filter { it.isNotBlank() }.let {
    CharGrid(it)
  }

  companion object {
    fun run() {
      RoboWall().part1()
    }
  }

  private fun part1() {
    sample
    val size = sample.width

    var isWallCrossed = false

    for (row in 0 until size) {
      for (col in 0 until size) {
        if (sample.getCell_xy(row, col) == '0') {
          continue
        }

        if (checkForWallCross(sample.getColumn(col))) {
          continue
        }
      }
    }
  }

  private fun checkForWallCross(column: CharArray): Boolean {
    for(i in column) {
      if(i == '.') {
        return true
      }
    }
    return false
  }
}

/*
..1111..... 0,0 = 0
..111...11. 0,1 = 0
....11..11.	0,2 = 1
11..11..11. 0,3 = 1
....11..11. 0,4 = 1
11111..111. 0,5 = 1
...111..11. 0,6 = 0
11......11. 0,7 = 0
1111111111. 0,8 = 0
		0,9

Fun navigate(matrix: Matrix): Boolean {

	Val size = matrix.size()
	Var isWallCrossed = false;

	for(val row=0; row<size; row++) {

		for(val col=0; col<size; col++) {

			if(matrix[row][col] == 0) {
				continue
}
if(checkForWallCross(matrix.coloumn[col])){
	continue
}

if(!isWallCrossed) {
	isWallCrossed = true
	continue
}

if(col == size-1) {
	Val r =  checkForWallCross(matrix.coloumn[col])
	if(!r && isWallCrossed) {
	Return false
}
Return true
}

}

}

	Return true
}





Fun checkForWallCross(val col: Array<Int>): Boolean {
	for(i in col) {
		if(i == 0) {
		true
}
}
Return false
}

 */