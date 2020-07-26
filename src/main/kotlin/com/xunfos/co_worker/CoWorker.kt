package com.xunfos.co_worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Design ideas
 *
 * Probably use a builder -> Interestingly, how do I do that?
 * Require instead lambdas instead of interfaces?
 */

abstract class CoWorker<InputType, OutputType>(
    private val inputFetch: Fetcher<InputType>,
    private val transformation: Transformer<InputType, OutputType>,
    private val outputter: Outputter<OutputType, Unit>
) {
    private lateinit var jobScope: Job
    private val inputChannel: Channel<InputType> by lazy { Channel<InputType>() }
    private val transformationChannel: Channel<OutputType> by lazy { Channel<OutputType>() }
    private val outputChannel: Channel<OutputType> by lazy { Channel<OutputType>() }

    fun <T> CoroutineScope.startJob(job: suspend () -> T) = launch {
        job()
    }

    suspend fun start() {
        jobScope = supervisorScope {
            startJob {
                while (isActive) {
                    val input = inputFetch.fetch()
                    inputChannel.send(input)
                }
            }

            startJob {
                while (isActive) {
                    val input = inputChannel.receive()
                    val transformed = transformation.transform(input)
                    outputChannel.send(transformed)
                }
            }

            startJob {
                while (isActive) {
                    val output = transformationChannel.receive()
                    outputter.operate(output)
                }
            }
        }
    }

    suspend fun stop() {
        if (this::jobScope.isInitialized) {
            try {
                jobScope.cancelChildren()
            } catch (e: Exception) {
                println(e) // TODO logs??
            } finally {
                jobScope.join()
            }
        }
    }
}

abstract class Fetcher<T> {
    abstract suspend fun fetch(): T
}

interface Outputter<T, U> {
    suspend fun operate(input: T): U
}

interface Transformer<T, U> {
    suspend fun transform(input: T): U
}
