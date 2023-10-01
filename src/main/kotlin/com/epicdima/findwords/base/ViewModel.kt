package com.epicdima.findwords.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class ViewModel {

    private var _scope: CoroutineScope? = null
    protected val scope: CoroutineScope
        get() {
            if (_scope == null) {
                _scope = CoroutineScope(Dispatchers.Main)
            }
            return requireNotNull(_scope)
        }

    fun onCleared() {
        _scope?.cancel()
        _scope = null
    }
}