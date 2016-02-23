package com.andrewberls.werk

import kotlin.test.*
import org.junit.Test

import java.util.ArrayList

class JsonTest {
    @Test
    @Suppress("UNCHECKED_CAST")
    fun testParseFromNull(): Unit {
        assertEquals(null, Json.parse("null"))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun testParseFromNumber(): Unit {
        assertEquals(2, (Json.parse("2") as Int))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun testParseFromString(): Unit {
        assertEquals("Hello, World!", (Json.parse("\"Hello, World!\"") as String))
    }


    @Test
    @Suppress("UNCHECKED_CAST")
    fun testParseFromArray(): Unit {
        assertEquals(
            listOf(1,2,3),
            (Json.parse("[1,2,3]") as ArrayList<Int>))
    }

    @Test
    fun testParseFailure(): Unit {
        assertFailsWith(
            Exception::class,
            { Json.parse("[1,") })
    }
}
