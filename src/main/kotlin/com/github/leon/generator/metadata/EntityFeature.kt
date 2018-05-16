package com.github.leon.generator.metadata

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EntityFeature(
        val code: Int = 0,
        val generated: Boolean = true,
        val versionInList: Boolean = false,
        val createdAtInList: Boolean = false,
        val updatedAtInList: Boolean = false,
        val creatorInList: Boolean = false,
        val modifierInList: Boolean = false,
        val version: Boolean = false,
        val security: Boolean = true
)


