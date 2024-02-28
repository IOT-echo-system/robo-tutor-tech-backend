package com.shiviraj.iot.communication.controllers.view

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ContactRequest(
    @field:NotBlank(message = "Name should not be blank.")
    val name: String,
    @field:Email(message = "Email should be valid.")
    val email: String,
    val subject: String?,
    @field:NotBlank(message = "Message should not be blank.")
    val message: String
)
