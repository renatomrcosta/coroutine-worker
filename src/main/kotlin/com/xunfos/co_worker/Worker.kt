package com.xunfos.co_worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class Worker(
    private val workerContext: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend (scope: CoroutineScope) -> Unit
) {
    suspend fun run(runContext: CoroutineContext = EmptyCoroutineContext) =
        withContext(chooseContext(runContext)) { block(this) }

    private fun chooseContext(
        runContext: CoroutineContext
    ): CoroutineContext =
        if (runContext !== EmptyCoroutineContext) {
            runContext
        } else {
            workerContext
        }
}

