package com.epicdima.findwords.common

data class FindWordsSolutionParameters(
    val dictionary: String,
    val minWordLength: Int,
    val maxWordLength: Int,
    val fullMatch: Boolean,
    val lettersGrid: List<List<Char>>
)
