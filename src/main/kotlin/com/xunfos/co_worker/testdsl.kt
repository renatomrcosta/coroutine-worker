package com.xunfos.co_worker

data class Banana(var name: String = "", var age: String = "")

fun banana(block: Banana.() -> Unit): Banana = Banana().apply(block)

fun main() {
    val banana = banana {
        name = "asd"
        age = "123"
    }
}
