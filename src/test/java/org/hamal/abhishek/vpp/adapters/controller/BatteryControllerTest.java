package org.hamal.abhishek.vpp.adapters.controller;

import org.hamal.abhishek.vpp.VppApplication;
import org.hamal.abhishek.vpp.application.dto.BatteryDto;
import org.hamal.abhishek.vpp.application.service.BatteryService;
import org.hamal.abhishek.vpp.domain.model.Battery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest(classes = { VppApplication.class })
@Testcontainers
class BatteryControllerTest {

    @Autowired
    BatteryService batteryService;

    @Test
    void registerBatteriesWith() {
        var request = List.of(new BatteryDto("Cybernetics", "2800", 123456));

        var result = batteryService.register(request);

        StepVerifier.create(result)
                .assertNext(BatteryControllerTest::assertRequestIsSaved)
                .verifyComplete();
    }

    private static void assertRequestIsSaved(Battery battery) {
        assert battery.getId() != null;
        assert battery.getName().equalsIgnoreCase("Cybernetics");
    }
}