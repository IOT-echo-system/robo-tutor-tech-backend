package com.shiviraj.iot.communication.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.config")
data class CommunicationConfig(val from: String)
