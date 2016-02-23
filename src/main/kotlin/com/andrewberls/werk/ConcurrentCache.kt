package com.andrewberls.werk

import java.util.HashMap

class ConcurrentCache<K, V>() {
    private val map = HashMap<K, V>()

    /**
     * Return the value stored at key `k`, or generate it
     * with `action()` and store it before returning it
     */
    fun fetch(k: K, action: () -> V): V =
        synchronized(map, {
            if (map.containsKey(k)) {
                map.get(k)!!
            } else {
                val v = action()
                map.put(k, v)
                v
            }
        })
}
