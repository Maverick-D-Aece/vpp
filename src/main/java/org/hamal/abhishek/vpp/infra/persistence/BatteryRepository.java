package org.hamal.abhishek.vpp.infra.persistence;

import org.hamal.abhishek.vpp.domain.model.Battery;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BatteryRepository extends R2dbcRepository<Battery, UUID>, BatteryComplexFetchRepository {
}
