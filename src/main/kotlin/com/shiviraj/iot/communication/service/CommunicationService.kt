package com.shiviraj.iot.communication.service

import com.shiviraj.iot.communication.controllers.view.ContactRequest
import com.shiviraj.iot.communication.model.Communication
import com.shiviraj.iot.communication.model.IdType
import com.shiviraj.iot.communication.repository.CommunicationRepository
import com.shiviraj.iot.utils.service.IdGeneratorService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CommunicationService(
    val communicationRepository: CommunicationRepository,
    val idGeneratorService: IdGeneratorService,
    val mailService: MailService
) {
    fun sendMessage(contactRequest: ContactRequest): Mono<Communication> {
        return idGeneratorService.generateId(IdType.COMMUNICATION_ID)
            .flatMap { communicationId ->
                val communication = Communication.from(communicationId, contactRequest)
                communicationRepository.save(communication)
            }
            .map {
                mailService.sendEmail(it)
                it
            }
    }

}
