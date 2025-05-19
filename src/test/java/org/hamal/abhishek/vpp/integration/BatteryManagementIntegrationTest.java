package org.hamal.abhishek.vpp.integration;

import org.hamal.abhishek.vpp.VppApplication;
import org.hamal.abhishek.vpp.domain.criteria.BatteryQueryCriteria;
import org.hamal.abhishek.vpp.domain.model.Battery;
import org.hamal.abhishek.vpp.infra.persistence.BatteryRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Optional;

@Testcontainers
@SpringBootTest(classes = { VppApplication.class })
class BatteryManagementIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(BatteryManagementIntegrationTest.class);

    @Autowired
    BatteryRepository repository;

    @Test
    void testBatterySaveAndAggregateFetch() {
        var battery = new Battery("Cybernetics", 3000, 200);

        var savedMono = repository.deleteAll()
                .then(repository.save(battery));

        StepVerifier.create(savedMono)
                .expectNextMatches(saved -> {
                    LOG.info("Saved battery: {}", saved.getName());
                    return saved.getId() != null;
                })
                .verifyComplete();

        var fetchAggregateMono = repository.findBatteryAggregates(new BatteryQueryCriteria(
                2900, 3100, Optional.of(100), Optional.of(300)
                ))
                .delaySubscription(Duration.ofSeconds(1));

        StepVerifier
                .create(fetchAggregateMono)
                .expectNextMatches(response -> {
                    LOG.info("Aggregate: names={}, total={}, average={}",
                            response.batteryNames(), response.totalWattCapacity(), response.averageWattCapacity());

                    return response.batteryNames().contains("Cybernetics")
                           && response.totalWattCapacity() == 200
                           && response.averageWattCapacity() == 200.0;
                })
                .verifyComplete();
    }

}
