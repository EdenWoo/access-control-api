package com.github.leon.sms.clicktale

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.leon.sms.HttpRequestProxy
import com.github.leon.sms.clicktale.config.ClicktaleProperties
import com.github.leon.sms.domain.MessageLog
import com.github.leon.sms.service.MessageProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.text.MessageFormat

@Component
@EnableConfigurationProperties(ClicktaleProperties::class)
class ClicktaleMessageProvider(
        @Autowired
        val clicktaleProperties: ClicktaleProperties,
        @Autowired
        var objectMapper: ObjectMapper

) : MessageProvider {
    override fun send(messageLog: MessageLog): Pair<String?, String> {

        var urlParameters = MessageFormat.format("apiKey=${clicktaleProperties.apiKey}&to={0}&content={1}",
                URLEncoder.encode(messageLog.sendTo, "UTF-8"),
                URLEncoder.encode(messageLog.message, "UTF-8")
        )
        val hrp = HttpRequestProxy()
        val map: Map<String, String>? = null
        val result = hrp.doRequest("${clicktaleProperties.apiUrl}?$urlParameters", map, map, "UTF-8") as String
        val resp = objectMapper.readValue<Map<String, Any?>>(result)
        val error = resp["error"] as String?
        return Pair(error, result)
    }

}