package com.andrewberls.werk

import redis.clients.jedis.Protocol
import com.andrewberls.werk.Utils

data class Config(
        private val host: String? = null,
        private val port: Int? = null,
        private val numThreads: Int? = null,
        private val sleepMs: Long? = null) {
    companion object {
        fun fromPath(path: String): Config = Utils.readYaml<Config>(path)
    }

    fun getHost(): String =
        host ?: Protocol.DEFAULT_HOST

    fun getPort(): Int =
        port ?: Protocol.DEFAULT_PORT

    fun getNumThreads(): Int =
        numThreads ?: Runtime.getRuntime().availableProcessors()

    fun getSleepMs(): Long =
        sleepMs ?: 1000L
}
