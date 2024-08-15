package com.epicdima.findwords.solve

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.epicdima.findwords.common.FindWordsSolutionParameters
import com.epicdima.findwords.common.FindWordsSolutionResult

interface FindWordsSolveComponent {

    val findWordsSolveViewModel: FindWordsSolveViewModel
}

class FindWordsDefaultSolveComponent(
    componentContext: ComponentContext,
    private val parameters: FindWordsSolutionParameters,
    private val onSolutionComplete: (FindWordsSolutionResult) -> Unit,
) : FindWordsSolveComponent, ComponentContext by componentContext {

    override val findWordsSolveViewModel: FindWordsSolveViewModel
        get() = instanceKeeper.getOrCreate {
            FindWordsSolveViewModel(parameters, onSolutionComplete)
        }
}
