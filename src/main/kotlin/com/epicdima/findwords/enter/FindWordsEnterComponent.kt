package com.epicdima.findwords.enter

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.epicdima.findwords.common.FindWordsSolutionParameters

interface FindWordsEnterComponent {

    val findWordsEnterViewModel: FindWordsEnterViewModel
}

class FindWordsDefaultEnterComponent(
    componentContext: ComponentContext,
    private val onSolveButtonClick: (FindWordsSolutionParameters) -> Unit,
) : FindWordsEnterComponent, ComponentContext by componentContext {

    override val findWordsEnterViewModel: FindWordsEnterViewModel
        get() = instanceKeeper.getOrCreate {
            FindWordsEnterViewModel(onSolveButtonClick)
        }
}
