package org.hamal.abhishek.vpp.application.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import org.hamal.abhishek.vpp.application.dto.BatteryDto;
import org.hamal.abhishek.vpp.application.mapper.BatteryMapper;
import org.hamal.abhishek.vpp.domain.criteria.BatteryQueryCriteria;
import org.hamal.abhishek.vpp.domain.model.Battery;
import org.hamal.abhishek.vpp.infra.persistence.BatteryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatteryService {

    private final BatteryMapper mapper;
    private final BatteryRepository repository;

    public Flux<Battery> register(List<BatteryDto> batteryDtoList) {
        log.info("Registering {} batteries", batteryDtoList.size());
        return repository
                .saveAll(mapper.toDomainList(batteryDtoList))
                .doOnError(e ->
                        log.error("Error while saving batteries{}", e.getMessage(), e)
                );
    }

    @SneakyThrows
    public Mono<BatteryAggregateResponse> fetchBatteriesInRange(
            String rangeStart, String rangeEnd,
            Optional<Integer> minWatt,
            Optional<Integer> maxWatt
    ) {
        log.info("Querying batteries between postCodes {} and {}, with watt range: {} - {}",
                rangeStart, rangeEnd, minWatt.orElse(null), maxWatt.orElse(null));

        try {
            int start = Integer.parseInt(rangeStart);
            int end = Integer.parseInt(rangeEnd);
            return repository
                    .findBatteryAggregates(new BatteryQueryCriteria(start, end, minWatt, maxWatt))
                    .doOnError(e -> log.error("Error fetching aggregates", e));
        } catch (NumberFormatException e) {
            return Mono.error(new IllegalArgumentException("Invalid postcode format", e));
        }
    }

}
