package com.epicdima.findwords.utils

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class FindWordsBaseViewModel : InstanceKeeper.Instance {

    @Volatile
    private var _scope: CoroutineScope? = null
    protected val scope: CoroutineScope
        get() {
            if (_scope == null) {
                _scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
            }
            return checkNotNull(_scope)
        }

    override fun onDestroy() {
        _scope?.cancel()
        _scope = null
    }
}
