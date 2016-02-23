package com.andrewberls.werk

// TODO: class + read from file
object Config {
    private val runtime = Runtime.getRuntime()

    fun numThreads(): Int =
        runtime.availableProcessors()

    fun sleepMs(): Long =
        1000L
}
