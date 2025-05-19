package org.hamal.abhishek.vpp.domain.criteria;

import java.util.Optional;

public record BatteryQueryCriteria(
        int rangeStart,
        int rangeEnd,
        Optional<Integer> minWatt,
        Optional<Integer> maxWatt
) {
}
