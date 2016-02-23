package com.andrewberls.werk

import java.lang.reflect.Constructor
import java.util.Optional
import java.util.HashMap
import kotlin.collections.Set
import redis.clients.jedis.JedisPool
import com.andrewberls.werk.ConcurrentCache
import com.andrewberls.werk.Config
import com.andrewberls.werk.IJobHandler
import com.andrewberls.werk.Job
import com.andrewberls.werk.RedisUtils

class Executor(
        val config: Config,
        val pool: JedisPool,
        val ctorCache: ConcurrentCache<String, Constructor<*>>) {
    private val KEY = "werk::jobs"

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

    private fun execute(job: Job): Unit {
        val className = job.className
        val ctor =
            ctorCache.fetch(className, { Class.forName(className).getConstructor() })
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
