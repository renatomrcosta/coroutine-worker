//package com.xunfos.coworker

//import io.kotest.core.spec.style.DescribeSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.string.shouldBeEqualIgnoringCase
//import io.kotest.matchers.string.shouldContain
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ObsoleteCoroutinesApi
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.newSingleThreadContext
//import java.util.concurrent.atomic.AtomicInteger
//
//@ObsoleteCoroutinesApi
//class WorkerTest : DescribeSpec() {
//    init {
//        describe("Worker - Execution Tests") {
//
//            // EVALUATE -> DO I REALLY WANT THIS SUSPENSEFUL?
//            it("should execute in a suspenseful manner") {
//                // After running the worker block, the immediate next line can execute while the worker is suspended
//                // This is a really weird test, maybe I can think of something better later
//                val sharedMutableCounter = AtomicInteger(0)
//
//                val worker = Worker {
//                    //Allows suspense
//                    delay(200)
//                    sharedMutableCounter.addAndGet(1)
//                    sharedMutableCounter.get() shouldBe 1
//                }
//                 worker.run() // DO I WANT RUN TO JUST RETURN ME A JOB OR DO I WANT TO CONTROL THE EXECUTION?
//                sharedMutableCounter.get() shouldBe 0
//                delay(200)
//                sharedMutableCounter.get() shouldBe 1
//
//            }
//        }
//        describe("Worker - Context Scope Tests") {
//            it("should execute on the default thread when no contexts are specified") {
//                Worker {
//                    Thread.currentThread().name shouldContain TEST_SCENARIO_MAIN_THREAD_NAME
//                }.run()
//            }
//            it("should execute on the dispatcher specified in Run when no global context is specified ") {
//                val threadName = "banana"
//                Worker {
//                    Thread.currentThread().name shouldBeEqualIgnoringCase threadName
//                }.run(newSingleThreadContext(threadName))
//            }
//            it("should execute on the dispatcher specified when a global context is specified ") {
//                Worker(Dispatchers.IO) {
//                    Thread.currentThread().name shouldContain DISPATCHER_MAIN_THREAD_NAME
//                }.run()
//            }
//            it("should execute on the dispatcher specified in Run, overriding a global context specified ") {
//                val threadName = "banana"
//                Worker(Dispatchers.IO) {
//                    Thread.currentThread().name shouldBeEqualIgnoringCase threadName
//                }.run(newSingleThreadContext(threadName))
//            }
//        }
//    }
//
//    companion object {
//        private const val TEST_SCENARIO_MAIN_THREAD_NAME = "SpecRunner"
//        private const val DISPATCHER_MAIN_THREAD_NAME = "DefaultDispatcher-worker"
//    }
//}
