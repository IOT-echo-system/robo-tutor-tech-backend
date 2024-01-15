package com.shiviraj.iot.communication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.shiviraj.iot"])
@ConfigurationPropertiesScan(basePackages = ["com.shiviraj.iot"])
class CommunicationApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(CommunicationApplication::class.java).run(*args)
        }
    }
}


