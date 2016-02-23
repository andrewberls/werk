package com.andrewberls.werk

import java.lang.reflect.Constructor
import kotlin.collections.List
import kotlin.concurrent.thread
import redis.clients.jedis.JedisPool
import com.andrewberls.werk.ConcurrentCache
import com.andrewberls.werk.Config
import com.andrewberls.werk.Executor

// A worker manages a resource pool and spins off a number
// of executor threads to perform actual tasks
class Worker(val config: Config, val pool: JedisPool) {
    private var executors: List<Thread>? = null

    // className -> Constructor cache shared across executors to
    // mitigate reflection cost
    private val ctorCache = ConcurrentCache<String, Constructor<*>>()

    fun start(): Unit {
        try {
            executors = (1..config.getNumThreads()).map {
                thread { Executor(config, pool, ctorCache).start() }
            }
            executors!!.forEach { it.join() }
        } finally {
            pool.destroy()
        }
    }
}
