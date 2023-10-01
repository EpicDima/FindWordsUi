package com.epicdima.findwords.common

import com.epicdima.findwords.solver.WordAndMask

data class FindWordsSolutionResult(
    val lettersGrid: List<List<Char>>,
    val words: List<WordAndMask>,
    val fullMatches: List<List<WordAndMask>>
)
