package de.otto.codingchallenge.ipranges;

import de.otto.codingchallenge.ipranges.controller.IpRangesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IprangesApplicationTests {

	@Autowired
	private IpRangesController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
