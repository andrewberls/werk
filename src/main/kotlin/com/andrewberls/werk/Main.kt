package com.andrewberls.werk

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import com.andrewberls.werk.Worker

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val pool = JedisPool(JedisPoolConfig(), "localhost");
        val worker = Worker(pool)
        worker.start()
    }
}
