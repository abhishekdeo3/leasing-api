package com.allane.leasingapi.service;

import com.allane.leasingapi.AbstractIT;
import com.allane.leasingapi.LeasingApiApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LeasingApiApplication.class)
@ActiveProfiles("test")
public class VehicleServiceTest extends AbstractIT {
}
