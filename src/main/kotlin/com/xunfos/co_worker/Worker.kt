package com.xunfos.co_worker

import kotlinx.coroutines.coroutineScope

class Worker(
    private val block: suspend () -> Unit
) {
    suspend fun run() = coroutineScope {
        block()
    }
}
