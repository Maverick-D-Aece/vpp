package org.hamal.abhishek.vpp.infra.persistence;

import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import org.hamal.abhishek.vpp.domain.criteria.BatteryQueryCriteria;
import reactor.core.publisher.Mono;

public interface BatteryComplexFetchRepository {

    Mono<BatteryAggregateResponse> findBatteryAggregates(BatteryQueryCriteria criteria);

}
