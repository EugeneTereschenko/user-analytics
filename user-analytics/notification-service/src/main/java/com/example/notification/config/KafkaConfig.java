/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.config;

import com.example.notification.dto.AppointmentEventDTO;
import com.example.notification.dto.PrescriptionEventDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, AppointmentEventDTO> appointmentConsumerFactory() {
        JsonDeserializer<AppointmentEventDTO> deserializer =
                new JsonDeserializer<>(AppointmentEventDTO.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AppointmentEventDTO>
    appointmentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AppointmentEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(appointmentConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PrescriptionEventDTO> prescriptionConsumerFactory() {
        JsonDeserializer<PrescriptionEventDTO> deserializer =
                new JsonDeserializer<>(PrescriptionEventDTO.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PrescriptionEventDTO>
    prescriptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PrescriptionEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(prescriptionConsumerFactory());
        return factory;
    }
}
