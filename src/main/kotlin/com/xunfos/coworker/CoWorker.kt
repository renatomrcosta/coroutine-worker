package com.xunfos.coworker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Design ideas
 */
fun <InputType, OutputType> coworker(lambda: CoWorker<InputType, OutputType>.() -> Unit) =
    CoWorker<InputType, OutputType>().apply(lambda)

class CoWorker<InputType, OutputType> {
    lateinit var fetchAction: suspend () -> InputType
    lateinit var transformAction: suspend (InputType) -> OutputType
    lateinit var outputAction: suspend (OutputType) -> Unit

    private lateinit var jobScope: Job
    private val inputChannel: Channel<InputType> by lazy { Channel<InputType>() }
    private val transformationChannel: Channel<OutputType> by lazy { Channel<OutputType>() }
    private val outputChannel: Channel<OutputType> by lazy { Channel<OutputType>() }

    private fun <T> CoroutineScope.startJob(job: suspend () -> T) = launch {
        job()
    }

    suspend fun start() {
        jobScope = supervisorScope {
            startJob {
                while (isActive) {
                    val input = fetchAction()
                    inputChannel.send(input)
                }
            }

            startJob {
                while (isActive) {
                    val input = inputChannel.receive()
                    val transformed = transformAction(input)
                    outputChannel.send(transformed)
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

    suspend fun stop() {
        inputChannel.close()
        transformationChannel.close()
        outputChannel.close()

        jobScope.cancelAndJoin()
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
