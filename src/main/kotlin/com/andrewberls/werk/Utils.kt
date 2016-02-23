package com.andrewberls.werk

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.*

object Utils {
    private val YAML_MAPPER: ObjectMapper =
        ObjectMapper(YAMLFactory()).registerModule(KotlinModule())

    /**
     * Return the contents of file at `path` as a String
     */
    fun slurp(path: String): String {
        val encoded = Files.readAllBytes(Paths.get(path))
        return String(encoded, StandardCharsets.UTF_8)
    }

    /**
     * Decode YAML file at `path` into an object of class `T`
     *
     * Example:
     * ```
     * data class Person(name: String, age: Int)
     * val bob = readYaml<Person>("/bob.yml")
     * ```
     */
    fun <T> readYaml(path: String, klass: Class<T>): T {
        val contents = slurp(path)
        return YAML_MAPPER.readValue(contents, klass)
    }
}
