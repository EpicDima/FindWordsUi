package com.epicdima.findwords.solution

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.epicdima.findwords.common.FindWordsSolutionResult

interface FindWordsSolutionComponent {

    val findWordsSolutionViewModel: FindWordsSolutionViewModel
}

class FindWordsDefaultSolutionComponent(
    componentContext: ComponentContext,
    private val result: FindWordsSolutionResult,
    private val openStartScreen: () -> Unit,
    private val openStartScreenWithReset: () -> Unit,
) : FindWordsSolutionComponent, ComponentContext by componentContext {

    override val findWordsSolutionViewModel: FindWordsSolutionViewModel
        get() = instanceKeeper.getOrCreate {
            FindWordsSolutionViewModel(result, openStartScreen, openStartScreenWithReset)
        }
}
