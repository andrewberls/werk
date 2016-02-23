package com.andrewberls.werk

import java.lang.reflect.Constructor
import java.util.Optional
import java.util.HashMap
import kotlin.collections.Set
import redis.clients.jedis.JedisPool
import com.andrewberls.werk.Config
import com.andrewberls.werk.IJobHandler
import com.andrewberls.werk.Job
import com.andrewberls.werk.RedisUtils

class Executor(val config: Config, val pool: JedisPool) {
    private val KEY = "werk::jobs"

    // className -> Constructor
    private val ctorCache = HashMap<String, Constructor<*>>()

    @Suppress("UNCHECKED_CAST")
    private fun acquireJob(): Optional<Job> {
        pool.getResource().use { jedis ->
            return RedisUtils.zPopMin(jedis, KEY).map { rawMember ->
                val member = Json.parse(rawMember) as HashMap<Any?, Any?>
                val jobId = member.get("jobId")!! as String
                val className = member.get("className")!! as String
                val args = member.get("args")
                Job(jobId, className, args)
            }
        }
    }

    private fun getCtor(className: String): Constructor<*> {
        val cachedCtor = ctorCache.get(className)
        if (cachedCtor == null) {
            val klass = Class.forName(className)
            val ctor = klass.getConstructor()
            ctorCache.put(className, ctor)
            return ctor
        } else {
            return cachedCtor
        }
    }

    private fun execute(job: Job): Unit {
        val ctor = getCtor(job.className)
        val handler = ctor.newInstance() as IJobHandler
        handler.handle(job)
    }

    fun start(): Unit {
        val sleepMs = config.getSleepMs()

        while (true) {
            val job = acquireJob()
            if (job.isPresent()) {
                execute(job.get())
            } else {
                Thread.sleep(sleepMs)
            }
        }
    }
}
