package com.epicdima.findwords.solve

import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epicdima.findwords.solve.FindWordsSolveUiState.Companion.NO_FULL_MATCHES

@Composable
fun FindWordsSolveScreen(component: FindWordsSolveComponent, modifier: Modifier = Modifier) {
    val uiState by component.findWordsSolveViewModel.uiStateFlow.collectAsState()
    FindWordsSolveScreen(uiState, modifier)
}

@Composable
private fun FindWordsSolveScreen(uiState: FindWordsSolveUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Solution is in progress", fontSize = 24.sp)
            CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val wordsCountState = remember { mutableIntStateOf(0) }
            IncreaseIntValueAnimation(wordsCountState, uiState.wordsFound)
            Text("Words found: ${wordsCountState.value}", fontSize = 20.sp)
            if (uiState.fullMatchesFound != NO_FULL_MATCHES) {
                val fullMatchesCountState = remember { mutableIntStateOf(0) }
                IncreaseIntValueAnimation(fullMatchesCountState, uiState.fullMatchesFound)
                Text("Complete solutions found: ${fullMatchesCountState.value}", fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun IncreaseIntValueAnimation(valueState: MutableIntState, targetValue: Int) {
    if (targetValue <= 0) {
        return
    }
    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(1000),
            typeConverter = Int.VectorConverter,
            initialValue = 0,
            targetValue = targetValue,
        )
    }
    var playTime by remember { mutableStateOf(0L) }
    LaunchedEffect(anim) {
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
            valueState.value = anim.getValueFromNanos(playTime)
        } while (!anim.isFinishedFromNanos(playTime))
    }
}
