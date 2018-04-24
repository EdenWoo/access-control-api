package com.github.leon.aci.config.json

import com.querydsl.core.types.Path

data class JsonConfigItem(
        var type: Class<*>? = null,
        var include: MutableList<Path<*>> = mutableListOf(),
        var exclude: MutableList<Path<*>> = mutableListOf()
)

