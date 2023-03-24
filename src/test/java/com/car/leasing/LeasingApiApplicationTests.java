package com.car.leasing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LeasingApiApplication.class)
@ActiveProfiles("test")
class LeasingApiApplicationTests extends AbstractIT {

    @Test
    void contextLoads() {
    }

}
