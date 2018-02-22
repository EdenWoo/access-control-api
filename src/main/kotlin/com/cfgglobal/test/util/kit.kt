package com.cfgglobal.test.util

import arrow.core.getOrElse
import arrow.syntax.option.toOption
import com.cfgglobal.test.domain.BaseEntity
import org.apache.commons.lang3.StringUtils


fun handleStatus(ids: String, longConsumer: (Long) -> BaseEntity) {
    ids.toOption().map { StringUtils.split(it, ",") }
            .map { it.toList() }
            .getOrElse { emptyList() }
            .map { it.toLong() }
            .forEach { longConsumer.invoke(it) }
}


fun handleStatus2(ids: String, longConsumer: (Long) -> Unit) {
    ids.toOption().map { StringUtils.split(it, ",") }
            .map { it.toList() }
            .getOrElse { emptyList() }
            .map { it.toLong() }
            .forEach { longConsumer.invoke(it) }
}



