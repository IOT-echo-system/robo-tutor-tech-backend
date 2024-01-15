package com.shiviraj.iot.communication.service

import com.shiviraj.iot.mqtt.model.CommunicationMessage
import com.shiviraj.iot.mqtt.model.MqttTopicName
import com.shiviraj.iot.mqtt.service.MqttSubscriber
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class CommunicationListener(private val mqttSubscriber: MqttSubscriber, private val mailService: MailService) {

    @PostConstruct
    fun listenForSendMail() {
        mqttSubscriber.subscribe(MqttTopicName.COMMUNICATION, CommunicationMessage::class.java) { message ->
            mailService.sendEmail(message)
        }
    }
}
