package com.xunfos.coworker.util

fun trace(msg: Any) = println("[${getThreadName()} - $msg]")

fun getThreadName() = Thread.currentThread().name
