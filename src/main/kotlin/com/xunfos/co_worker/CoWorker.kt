package com.xunfos.co_worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Design ideas
 *
 * Probably use a builder -> Interestingly, how do I do that?
 * Require instead lambdas instead of interfaces?
 */

class CoWorker<InputType, OutputType>(
    private val fetchAction: suspend () -> InputType,
    private val transformAction: suspend (InputType) ->  OutputType,
    private val outputAction: suspend (OutputType) -> Unit
) {
    private lateinit var jobScope: Job
    private val inputChannel: Channel<InputType> by lazy { Channel<InputType>() }
    private val transformationChannel: Channel<OutputType> by lazy { Channel<OutputType>() }
    private val outputChannel: Channel<OutputType> by lazy { Channel<OutputType>() }

    fun <T> CoroutineScope.startJob(job: suspend () -> T) = launch {
        job()
    }

    suspend fun work() {
        jobScope = supervisorScope {
            startJob {
                while (isActive) {
                    val input = fetchAction()
                    inputChannel.send(input)

                    println("sent $input")
                }
            }

            startJob {
                while (isActive) {
                    val input = inputChannel.receive()
                    val transformed = transformAction(input)
                    outputChannel.send(transformed)

                    println("sent $transformed")
                }
            }

            startJob {
                while (isActive) {
                    val output = outputChannel.receive()
                    outputAction(output)
                }
            }
        }
    }

    fun stop () {
        inputChannel.close()
        transformationChannel.close()
        outputChannel.close()
    }

    suspend fun cancel() {
        if (this::jobScope.isInitialized) {
            try {
                jobScope.cancelChildren()
            } catch (e: Exception) {
                throw e
            } finally {
                jobScope.join()
            }
        }
    }
}
