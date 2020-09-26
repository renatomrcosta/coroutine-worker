package com.xunfos.co_worker

import com.xunfos.co_worker.util.trace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class Worker(
    private val block: suspend (scope: CoroutineScope) -> Unit
) {
    suspend fun run(runContext: CoroutineContext = EmptyCoroutineContext) =
        withContext(runContext) { block(this) }
}
