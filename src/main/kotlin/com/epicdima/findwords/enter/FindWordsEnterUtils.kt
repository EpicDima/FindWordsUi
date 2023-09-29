package com.epicdima.findwords.enter

internal const val EMPTY_LETTER: Char = ' '

fun createLettersGrid(rows: Int, cols: Int): List<List<Char>> {
    return createMutableLettersGrid(rows, cols)
}

fun createLettersGridWithChange(
    grid: List<List<Char>>,
    rowForChange: Int,
    colForChange: Int,
    letter: Char
): List<List<Char>> {
    val mutableRowsGrid = grid.map { it.toMutableList() }
    mutableRowsGrid[rowForChange][colForChange] = letter
    return mutableRowsGrid
}

private fun createMutableLettersGrid(rows: Int, cols: Int): MutableList<MutableList<Char>> {
    return MutableList(rows) {
        MutableList(cols) { EMPTY_LETTER }
    }
}

fun createLettersGridWithCopy(rows: Int, cols: Int, grid: List<List<Char>>): List<List<Char>> {
    val minRows = minOf(rows, grid.size)
    val minCols = minOf(cols, grid.first().size)

    val newGrid = createMutableLettersGrid(rows, cols)

    for (row in 0 until minRows) {
        for (col in 0 until minCols) {
            newGrid[row][col] = grid[row][col]
        }
    }

    return newGrid
}
