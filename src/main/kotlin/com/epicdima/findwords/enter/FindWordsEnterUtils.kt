package com.epicdima.findwords.enter

internal const val EMPTY_LETTER: Char = ' '

fun createMutableLettersGrid(rows: Int, cols: Int): MutableList<MutableList<Char>> {
    return MutableList(rows) {
        MutableList(cols) { EMPTY_LETTER }
    }
}

fun createMutableLettersGrid(text: String): List<List<Char>> {
    val lines = text.lines()
    val rows = lines.size
    val cols = if (lines.isEmpty()) 0 else lines.maxOf { it.length }
    val lettersGrid = createMutableLettersGrid(rows, cols)
    for (row in lines.indices) {
        val line = lines[row]
        for (col in 0..<cols) {
            if (col < line.length) {
                lettersGrid[row][col] = line[col]
            } else {
                lettersGrid[row][col] = EMPTY_LETTER
            }
        }
    }
    return lettersGrid
}
