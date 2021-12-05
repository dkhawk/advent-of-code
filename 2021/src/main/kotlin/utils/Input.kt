package utils

import java.io.File

class Input {
  companion object {
    private const val YEAR = 2021
    private const val PATH = "/Users/dkhawk/Downloads/$YEAR/"

    fun readAsLines(day: Int): List<String> = readFile(inputFileName(dayIntToString(day)))

    fun readAsLines(day: String): List<String> = readFile(inputFileName(day))

    private fun readFile(baseFilename: String): List<String> =
      File(PATH + baseFilename).readLines().filter(String::isNotBlank)

    private fun dayIntToString(day: Int) = day.toString().padStart(2, '0')

    private fun inputFileName(day: String): String = "input-$day.txt"
  }
}