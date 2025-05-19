package org.hamal.abhishek.vpp.infra.persistence;

import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import org.hamal.abhishek.vpp.domain.criteria.BatteryQueryCriteria;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BatteryComplexFetchRepositoryImpl implements BatteryComplexFetchRepository {

    private final DatabaseClient client;

    public BatteryComplexFetchRepositoryImpl(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Mono<BatteryAggregateResponse> findBatteryAggregates(BatteryQueryCriteria criteria) {
        Mono<List<String>> sortedNamesMono = fetchBatteryNames(criteria);
        Mono<AggregateResult> aggregateMono = fetchBatteryAggregates(criteria);

        return Mono.zip(aggregateMono, sortedNamesMono)
                .flatMap(tuple -> {
                    var aggregate = tuple.getT1();
                    var names = tuple.getT2();

                    if (names.isEmpty()) {
                        return Mono.just(new BatteryAggregateResponse(
                                Collections.emptyList(), 0, 0.0
                        ));
                    }

                    return Mono.just(new BatteryAggregateResponse(
                            names, aggregate.total(), aggregate.average()
                    ));
                });
    }

    private Mono<List<String>> fetchBatteryNames(BatteryQueryCriteria criteria) {
        StringBuilder clause = new StringBuilder("WHERE post_code BETWEEN :start AND :end");
        criteria.minWatt().ifPresent(min -> clause.append(" AND watt_capacity >= :minWatt"));
        criteria.maxWatt().ifPresent(max -> clause.append(" AND watt_capacity <= :maxWatt"));

        String query = "SELECT name FROM battery " + clause + " ORDER BY name ASC";

        DatabaseClient.GenericExecuteSpec spec = bindParameters(client.sql(query), criteria);
        return spec.map((row, meta) -> row.get("name", String.class))
                .all()
                .collectList();
    }

    private Mono<AggregateResult> fetchBatteryAggregates(BatteryQueryCriteria criteria) {
        StringBuilder clause = new StringBuilder("WHERE post_code BETWEEN :start AND :end");
        criteria.minWatt().ifPresent(min -> clause.append(" AND watt_capacity >= :minWatt"));
        criteria.maxWatt().ifPresent(max -> clause.append(" AND watt_capacity <= :maxWatt"));

        String query = "SELECT SUM(watt_capacity) AS total, AVG(watt_capacity) AS average FROM battery " + clause;

        DatabaseClient.GenericExecuteSpec spec = bindParameters(client.sql(query), criteria);
        return spec.map((row, meta) -> {
                    Integer total = row.get("total", Integer.class);
                    Double average = row.get("average", Double.class);
                    return new AggregateResult(
                            total != null ? total : 0,
                            average != null ? average : 0.0
                    );
                })
                .one();
    }

    private DatabaseClient.GenericExecuteSpec bindParameters(
            DatabaseClient.GenericExecuteSpec spec,
            BatteryQueryCriteria criteria
    ) {
        spec = spec
                .bind("start", criteria.rangeStart())
                .bind("end", criteria.rangeEnd());

        Optional<Integer> optionalMinWatt = criteria.minWatt();
        Optional<Integer> optionalMaxWatt = criteria.maxWatt();

        if (optionalMinWatt.isPresent()) {
            spec = spec.bind("minWatt", optionalMinWatt.get());
        }

        if (optionalMaxWatt.isPresent()) {
            spec = spec.bind("maxWatt", optionalMaxWatt.get());
        }

        return spec;
    }

    private record AggregateResult(int total, double average) {}

}
