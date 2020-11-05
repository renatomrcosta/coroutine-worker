package com.xunfos.co_worker.util

fun trace(msg: Any) = println("[${getThreadName()} - $msg]")

fun getThreadName() = Thread.currentThread().name
