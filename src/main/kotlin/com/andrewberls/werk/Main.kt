package com.andrewberls.werk

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import com.andrewberls.werk.Config
import com.andrewberls.werk.Worker

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val config =
            if (args.size > 0) {
                Config.fromPath(args.get(0))
            } else {
                Config()
            }
        val pool = JedisPool(JedisPoolConfig(),
                             config.getHost(),
                             config.getPort())
        val worker = Worker(config, pool)
        worker.start()
    }
}
