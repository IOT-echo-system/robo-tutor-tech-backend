package com.shiviraj.iot.communication.repository

import com.shiviraj.iot.communication.model.Communication
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunicationRepository :ReactiveCrudRepository<Communication, String>
