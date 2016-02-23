package com.andrewberls.werk

import kotlin.collections.List
import kotlin.concurrent.thread
import redis.clients.jedis.JedisPool
import com.andrewberls.werk.Config
import com.andrewberls.werk.Executor

// A worker manages a resource pool and spins off a number
// of executor threads to perform actual tasks
class Worker(val pool: JedisPool) {
    private var executors: List<Thread>? = null

    fun start(): Unit {
        try {
            executors = (1..Config.numThreads()).map {
                thread { Executor(pool).start() }
            }
            executors!!.forEach { it.join() }
        } finally {
            pool.destroy()
        }
    }
}
