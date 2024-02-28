package com.shiviraj.iot.communication.model

import com.shiviraj.iot.utils.service.IdSequenceType

enum class IdType(override val length: Int) : IdSequenceType {
    COMMUNICATION_ID(8)
}
