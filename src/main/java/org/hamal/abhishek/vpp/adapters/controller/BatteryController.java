package org.hamal.abhishek.vpp.adapters.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import org.hamal.abhishek.vpp.application.dto.BatteryDto;
import org.hamal.abhishek.vpp.application.service.BatteryService;
import org.hamal.abhishek.vpp.domain.model.Battery;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batteries")
public class BatteryController {

    private final BatteryService service;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Battery> registerBatteries(
            @RequestBody List<BatteryDto> batteries
    ) {
        log.info("Received request to register {} batteries", batteries.size());
        return service.register(batteries);
    }

    @GetMapping(
            value = "/query",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<BatteryAggregateResponse> getBatteriesByPostCodeRange(
            @RequestParam String postCodeStart,
            @RequestParam String postCodeEnd,
            @RequestParam(required = false) Integer minWatt,
            @RequestParam(required = false) Integer maxWatt
    ) {
        log.info(
                "Received query for batteries in post code range {}-{}, minWatt={}, maxWatt={}",
                postCodeStart, postCodeEnd, minWatt, maxWatt
        );
        return service.fetchBatteriesInRange(
                postCodeStart, postCodeEnd, ofNullable(minWatt), ofNullable(maxWatt)
        );
    }

}
