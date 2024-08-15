package com.epicdima.findwords

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.epicdima.findwords.common.FindWordsSolutionParameters
import com.epicdima.findwords.common.FindWordsSolutionResult
import com.epicdima.findwords.enter.FindWordsDefaultEnterComponent
import com.epicdima.findwords.enter.FindWordsEnterComponent
import com.epicdima.findwords.solution.FindWordsDefaultSolutionComponent
import com.epicdima.findwords.solution.FindWordsSolutionComponent
import com.epicdima.findwords.solve.FindWordsDefaultSolveComponent
import com.epicdima.findwords.solve.FindWordsSolveComponent
import java.util.concurrent.atomic.AtomicInteger

interface FindWordsRootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class EnterChild(val component: FindWordsEnterComponent) : Child()

        class SolveChild(val component: FindWordsSolveComponent) : Child()

        class SolutionChild(val component: FindWordsSolutionComponent) : Child()
    }
}

class FindWordsDefaultRootComponent(
    componentContext: ComponentContext,
) : FindWordsRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val enterId = AtomicInteger(0) // needed for hack to reset enter screen

    override val stack: Value<ChildStack<*, FindWordsRootComponent.Child>> = createChildStack()

    private fun createChildStack(): Value<ChildStack<Config, FindWordsRootComponent.Child>> {
        return childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.Enter(enterId.get()),
            childFactory = ::child,
        )
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): FindWordsRootComponent.Child {
        return when (config) {
            is Config.Enter -> FindWordsRootComponent.Child.EnterChild(
                enterComponent(componentContext)
            )

            is Config.Solve -> FindWordsRootComponent.Child.SolveChild(
                solveComponent(componentContext, config)
            )

            is Config.Solution -> FindWordsRootComponent.Child.SolutionChild(
                solutionComponent(componentContext, config)
            )
        }
    }

    private fun enterComponent(
        componentContext: ComponentContext,
    ): FindWordsEnterComponent {
        return FindWordsDefaultEnterComponent(
            componentContext = componentContext,
            onSolveButtonClick = { parameters -> navigation.push(Config.Solve(parameters)) }
        )
    }

    private fun solveComponent(
        componentContext: ComponentContext,
        config: Config.Solve
    ): FindWordsSolveComponent {
        return FindWordsDefaultSolveComponent(
            componentContext = componentContext,
            parameters = config.parameters,
            onSolutionComplete = { result -> navigation.push(Config.Solution(result)) }
        )
    }

    private fun solutionComponent(
        componentContext: ComponentContext,
        config: Config.Solution
    ): FindWordsSolutionComponent {
        return FindWordsDefaultSolutionComponent(
            componentContext = componentContext,
            result = config.result,
            openStartScreen = { navigation.replaceAll(Config.Enter(enterId.get())) },
            openStartScreenWithReset = {
                navigation.replaceAll(Config.Enter(enterId.incrementAndGet()))
            },
        )
    }

    private sealed interface Config {
        data class Enter(val id: Int) : Config

        data class Solve(val parameters: FindWordsSolutionParameters) : Config

        data class Solution(val result: FindWordsSolutionResult) : Config
    }
}
