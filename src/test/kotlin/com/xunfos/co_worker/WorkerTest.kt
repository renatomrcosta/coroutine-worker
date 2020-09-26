package com.xunfos.co_worker

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext

@ObsoleteCoroutinesApi
class WorkerTest : DescribeSpec() {
    init {
        describe("Worker - Execution Tests") {}
        describe("Worker - Context Scope Tests") {
            it("should execute on the default thread when no contexts are specified") {
                Worker {
                    Thread.currentThread().name shouldContain TEST_SCENARIO_MAIN_THREAD_NAME
                }.run()
            }
            it("should execute on the dispatcher specified in Run when no global context is specified ") {
                val threadName = "banana"
                Worker {
                    Thread.currentThread().name shouldBeEqualIgnoringCase threadName
                }.run(newSingleThreadContext(threadName))
            }
            it("should execute on the dispatcher specified when a global context is specified ") {
                Worker(Dispatchers.IO) {
                    Thread.currentThread().name shouldContain DISPATCHER_MAIN_THREAD_NAME
                }.run()
            }
            it("should execute on the dispatcher specified in Run, overriding a global context specified ") {
                val threadName = "banana"
                Worker(Dispatchers.IO) {
                    Thread.currentThread().name shouldBeEqualIgnoringCase threadName
                }.run(newSingleThreadContext(threadName))
            }
        }
    }

    companion object {
        private const val TEST_SCENARIO_MAIN_THREAD_NAME = "SpecRunner"
        private const val DISPATCHER_MAIN_THREAD_NAME = "DefaultDispatcher-worker"
    }
}
