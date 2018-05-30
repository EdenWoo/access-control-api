package com.github.leon.generator.entity

data class CodeEnv(
        var taskes: List<Task> = listOf(),
        var entities: List<CodeEntity> = listOf()
)