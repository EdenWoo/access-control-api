package com.github.leon.sms.service

import com.github.leon.sms.domain.MessageLog

interface MessageProvider {

    fun send(messageLog: MessageLog): Pair<String?, String>
}
