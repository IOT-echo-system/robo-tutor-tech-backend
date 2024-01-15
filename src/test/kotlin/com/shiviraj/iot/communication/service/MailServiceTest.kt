package com.shiviraj.iot.communication.service

import com.shiviraj.iot.communication.config.CommunicationConfig
import com.shiviraj.iot.mqtt.model.*
import com.shiviraj.iot.mqtt.service.MqttPublisher
import io.mockk.*
import jakarta.mail.Session
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mail.MailException
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class MailServiceTest {
    private val mailSender = mockk<JavaMailSender>()
    private val mqttPublisher = mockk<MqttPublisher>()

    private val mailService = MailService(
        mailSender = mailSender,
        communicationConfig = CommunicationConfig(from = "from@email.com"),
        mqttPublisher = mqttPublisher
    )
    private val mockTime = LocalDateTime.of(2024, 1, 1, 1, 1)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        mockkStatic(LocalDateTime::class)
        every { mqttPublisher.publish(any(), any()) } returns Unit
        every { LocalDateTime.now(ZoneId.of("UTC")) } returns mockTime
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should send email and audit success message`() {
        val message = CommunicationMessage(
            type = CommunicationType.OTP,
            to = "to@email.com",
            userId = "userId",
            metadata = mapOf("otp" to "123456", "name" to "Name")
        )
        val mimeMessage = MimeMessage(Session.getInstance(Properties()))

        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(any<MimeMessage>()) } returns Unit
        every { mqttPublisher.publish(any(), any()) } returns Unit

        mailService.sendEmail(message = message)

        verify(exactly = 1) {
            mailSender.createMimeMessage()
            mailSender.send(mimeMessage)
            mqttPublisher.publish(
                MqttTopicName.AUDIT, AuditMessage(
                    status = AuditStatus.SUCCESS,
                    userId = "userId",
                    metadata = mapOf("type" to CommunicationType.OTP),
                    event = AuditEvent.SEND_EMAIL,
                    timestamp = mockTime
                )
            )
        }
    }

    @Test
    fun `should not send email and audit failure message when error occurred while sending email`() {
        val message = CommunicationMessage(
            type = CommunicationType.OTP,
            to = "to@email.com",
            userId = "userId",
            metadata = mapOf("otp" to "123456", "name" to "Name")
        )
        val mimeMessage = MimeMessage(Session.getInstance(Properties()))

        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(any<MimeMessage>()) } throws MailSendException("connection failure")
        every { mqttPublisher.publish(any(), any()) } returns Unit

        mailService.sendEmail(message = message)

        verify(exactly = 1) {
            mailSender.createMimeMessage()
            mailSender.send(mimeMessage)
            mqttPublisher.publish(
                MqttTopicName.AUDIT, AuditMessage(
                    status = AuditStatus.FAILURE,
                    userId = "userId",
                    metadata = mapOf("type" to CommunicationType.OTP),
                    event = AuditEvent.SEND_EMAIL,
                    timestamp = mockTime
                )
            )
        }
    }
}

