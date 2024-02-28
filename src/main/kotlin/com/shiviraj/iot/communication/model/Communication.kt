package com.shiviraj.iot.communication.model

import com.shiviraj.iot.communication.controllers.view.ContactRequest
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

const val COMMUNICATION_COLLECTION = "communications"

@TypeAlias("Communication")
@Document(COMMUNICATION_COLLECTION)
data class Communication(
    @Id
    val communicationId: String? = null,
    val name: String,
    val email: String,
    val subject: String?,
    val message: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val status: TicketStatus = TicketStatus.OPEN
) {
    companion object {
        fun from(communicationId: String, contactRequest: ContactRequest): Communication {
            return Communication(
                communicationId = communicationId,
                name = contactRequest.name,
                email = contactRequest.email,
                subject = contactRequest.subject,
                message = contactRequest.message
            )
        }
    }
}

enum class TicketStatus {
    OPEN,
    CLOSE
}
