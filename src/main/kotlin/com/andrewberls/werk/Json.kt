package com.andrewberls.werk

import java.io.StringReader
import java.util.ArrayList
import java.util.HashMap
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

object Json {
    private val factory =
        JsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)

    private fun parseFrom(parser: JsonParser, consumeFirst: Boolean = false): Any? {
        if (consumeFirst) {
            parser.nextToken()
            if (parser.getCurrentToken() == null) {
                return null
            }
        }
        return when (parser.getCurrentToken()) {
            JsonToken.START_OBJECT -> {
                val map = HashMap<Any?, Any?>()
                parser.nextToken()
                while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
                    val key = parser.getText()
                    parser.nextToken()
                    map.put(key, parseFrom(parser))
                    parser.nextToken()
                }
                map
            }
            JsonToken.START_ARRAY -> {
                val ary = ArrayList<Any?>()
                parser.nextToken()
                while (parser.getCurrentToken() != JsonToken.END_ARRAY) {
                    ary.add(parseFrom(parser))
                    parser.nextToken()
                }
                ary
            }
            JsonToken.VALUE_STRING -> parser.getText()
            JsonToken.VALUE_NUMBER_INT -> parser.getNumberValue()
            JsonToken.VALUE_NUMBER_FLOAT -> parser.getDoubleValue()
            JsonToken.VALUE_TRUE -> java.lang.Boolean.TRUE
            JsonToken.VALUE_FALSE -> java.lang.Boolean.FALSE
            JsonToken.VALUE_NULL -> null
            else -> throw Exception("Cannot parse " + parser.getCurrentToken())
        }
    }

    fun parse(string: String): Any? =
        parseFrom(factory.createParser(StringReader(string)), true)
}
