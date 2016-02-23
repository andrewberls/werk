package com.andrewberls.werk

import java.util.Optional
import kotlin.collections.Set
import redis.clients.jedis.Jedis

object RedisUtils {
    private val LOCK = Object()

    /**
     * Remove and return the member with the minimum score from sorted set
     * at `key` (holds an exclusive lock)
     */
     fun zPopMin(jedis: Jedis, key: String): Optional<String> =
         synchronized(LOCK, {
             val members: Set<String> =
                 jedis.zrangeByScore(key, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 1)

             if (members.isEmpty()) {
                 return Optional.empty()
             } else {
                 require(members.size == 1)
                 val member = members.toList().first()
                 jedis.zrem(key, member)
                 return Optional.of(member)
             }
         })
}
