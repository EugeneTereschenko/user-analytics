/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.testutil;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;

@TestConfiguration
public class NoKafkaListenerConfig {
    @Bean
    public KafkaListenerAnnotationBeanPostProcessor kafkaListenerAnnotationBeanPostProcessor() {
        return new KafkaListenerAnnotationBeanPostProcessor() {
            @Override
            public void afterSingletonsInstantiated() {
                // Do nothing to prevent listener registration
            }
        };
    }
}

