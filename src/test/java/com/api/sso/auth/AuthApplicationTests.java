package com.api.sso.auth;

import com.api.sso.auth.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthApplicationTests {

	@Autowired
	private HttpGraphQlTester graphQlTester;

	@Test
	void contextLoads() {
	}

	@Test
	void register() {
		this.graphQlTester
				.documentName("register")
				.variable("name", "Test User")
				.variable("email", "test@example.com")
				.variable("password", "password")
				.execute()
				.path("register")
				.entity(UserEntity.class)
				.satisfies(user -> {
					assertThat(user.getUsername()).isEqualTo("Test User");
					assertThat(user.getEmail()).isEqualTo("test@example.com");
				});
	}

}
