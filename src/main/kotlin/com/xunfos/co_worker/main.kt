package com.xunfos.co_worker

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.random.nextUInt

private val rngesus = Random(System.currentTimeMillis())
private suspend fun fetchValue(): Int = rngesus.nextInt(0, Int.MAX_VALUE)

private fun transformValue(value: Int): String {
    return "transformed $value"
}

private fun processValue(value: String): Unit {
    println(value)
}

// Make transformation optional and a collection
// rename parameters
// Fix the parameter inference stuff

// Write a normal worker (that is a runnable substitute)
// Write a pipeline worker (that is, the current co-worker)

// Write tests
// Deploy in GH package

fun main() = runBlocking {
    val coworker = coworker<Int, String> {
        fetchAction = {
            fetchValue()
        }
        transformAction = {
            transformValue(it)
        }
        outputAction = {
            processValue(it)
        }
    }
    coworker.start()
    // Think about making this as a flow to control throughput?
    coworker.cancel()
}

/**
Has one input
Has one output
Has 0-n transformation patterns

Accepts Context Dispatchers
I can write as a DSL and tell it to run
 **/
