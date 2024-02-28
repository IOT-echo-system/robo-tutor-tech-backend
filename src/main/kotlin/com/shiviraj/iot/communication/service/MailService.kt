package com.shiviraj.iot.communication.service

import com.shiviraj.iot.communication.config.CommunicationConfig
import com.shiviraj.iot.communication.model.Communication
import com.shiviraj.iot.loggingstarter.details.LogDetails
import com.shiviraj.iot.loggingstarter.logger.Logger
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets
import kotlin.reflect.full.memberProperties

@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val communicationConfig: CommunicationConfig,
) {
    private val logger = Logger(this::class.java)
    fun sendEmail(message: Communication) {
        try {
            val contentAndSubject = getTemplate()
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true)
            helper.setFrom(communicationConfig.from)
            helper.setTo(message.email)
            helper.setCc(communicationConfig.from)
            helper.setText(updateContent(contentAndSubject.first, message), true)
            helper.setSubject(contentAndSubject.second + "  Ticket no.: ${message.communicationId}")
            mailSender.send(mimeMessage)
            logger.info(LogDetails(message = "Successfully sent email"))
        } catch (e: Exception) {
            logger.error(LogDetails(message = "Error occurred while sending email"), e)
        }
    }

    private fun updateContent(content: String, message: Communication): String {
        var updatedContent = content
        objectToMap(message).entries.forEach {
            val regex = Regex("\\{\\{${it.key}}}", RegexOption.IGNORE_CASE)
            updatedContent = updatedContent.replace(regex, it.value.toString())
        }
        return updatedContent
    }

    private fun getTemplate(): Pair<String, String> {
        val resource = ClassPathResource("contactReply.html")
        val templateStream = resource.inputStream
        val content = StreamUtils.copyToString(templateStream, StandardCharsets.UTF_8)
        return Pair(content, "Thank You for Contacting Robotutor Tech!")
    }
}


inline fun <reified T : Any> objectToMap(obj: T): Map<String, Any?> {
    return T::class.memberProperties.associateBy({ it.name }, { it.get(obj) })
}
