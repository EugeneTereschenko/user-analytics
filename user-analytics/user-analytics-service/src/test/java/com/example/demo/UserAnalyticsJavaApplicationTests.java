package com.example.demo;

import com.example.demo.dto.UserEventDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAnalyticsJavaApplicationTests {

	@MockBean
	private KafkaTemplate<String, UserEventDTO> kafkaTemplate;

	@Test
	void contextLoads() {
	}

}
