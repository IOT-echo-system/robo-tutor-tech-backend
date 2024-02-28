package com.shiviraj.iot.communication.controllers

import com.shiviraj.iot.communication.controllers.view.ContactRequest
import com.shiviraj.iot.communication.model.Communication
import com.shiviraj.iot.communication.service.CommunicationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class Controller(val communicationService: CommunicationService) {

    @PostMapping("/contact")
    fun contact(@RequestBody contactRequest: ContactRequest): Mono<Communication> {
        return communicationService.sendMessage(contactRequest)
    }
}
