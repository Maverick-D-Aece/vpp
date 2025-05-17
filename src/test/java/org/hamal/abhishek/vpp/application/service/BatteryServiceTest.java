package org.hamal.abhishek.vpp.application.service;

import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import org.hamal.abhishek.vpp.application.mapper.BatteryMapper;
import org.hamal.abhishek.vpp.infra.persistence.BatteryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BatteryServiceTest {

    private BatteryRepository repository;
    private BatteryService batteryService;

    @BeforeEach
    void setup() {
        repository = mock(BatteryRepository.class);

        batteryService = new BatteryService(
                mock(BatteryMapper.class),
                repository
        );
    }

    @Test
    void testQueryBatteriesInRange() {
        var response = new BatteryAggregateResponse(
                List.of("Dartmouth", "SingleBurg"),
                250,
                125.0
        );

        Mockito.when(repository.findBatteryAggregates(
                        2000, 2200,
                        Optional.empty(), Optional.empty()))
                .thenReturn(Mono.just(response));

        var responseMono = batteryService.fetchBatteriesInRange(
                "2000", "2200", Optional.empty(), Optional.empty()
        );

        StepVerifier.create(responseMono)
                .expectNextMatches(r ->
                        r.batteryNames().equals(List.of("Dartmouth", "SingleBurg")) &&
                        r.totalWattCapacity() == 250 &&
                        Math.abs(r.averageWattCapacity() - 125.0) < 0.001
                )
                .verifyComplete();
    }
}