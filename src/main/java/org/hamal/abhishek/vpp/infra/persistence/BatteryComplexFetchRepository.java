package org.hamal.abhishek.vpp.infra.persistence;

import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface BatteryComplexFetchRepository {

    Mono<BatteryAggregateResponse> findBatteryAggregates(
            int rangeStart, int rangeEnd,
            Optional<Integer> minWatt, Optional<Integer> maxWatt
    );

}
