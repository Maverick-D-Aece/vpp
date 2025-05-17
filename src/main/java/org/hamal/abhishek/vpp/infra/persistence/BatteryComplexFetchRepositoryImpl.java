package org.hamal.abhishek.vpp.infra.persistence;

import org.hamal.abhishek.vpp.application.dto.BatteryAggregateResponse;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

public class BatteryComplexFetchRepositoryImpl implements BatteryComplexFetchRepository {

    private final DatabaseClient client;

    public BatteryComplexFetchRepositoryImpl(DatabaseClient client) {
        this.client = client;
    }


    @Override
    public Mono<BatteryAggregateResponse> findBatteryAggregates(
            int rangeStart, int rangeEnd,
            Optional<Integer> minWatt, Optional<Integer> maxWatt
    ) {
        StringBuilder whereClause = new StringBuilder("""
            WHERE post_code BETWEEN :start AND :end
        """);

        if (minWatt.isPresent()) {
            whereClause.append(" AND watt_capacity >= :minWatt");
        }
        if (maxWatt.isPresent()) {
            whereClause.append(" AND watt_capacity <= :maxWatt");
        }

        String nameQuery = "SELECT name FROM battery " + whereClause + " ORDER BY name ASC";
        var nameSql = client.sql(nameQuery)
                .bind("start", rangeStart)
                .bind("end", rangeEnd);
        if (minWatt.isPresent()) nameSql = nameSql.bind("minWatt", minWatt.get());
        if (maxWatt.isPresent()) nameSql = nameSql.bind("maxWatt", maxWatt.get());

        Mono<List<String>> sortedNamesMono = nameSql
                .map((row, meta) -> row.get("name", String.class))
                .all()
                .collectList();

        String aggQuery = "SELECT SUM(watt_capacity) AS total, AVG(watt_capacity) AS average FROM battery " + whereClause;
        var aggSql = client.sql(aggQuery)
                .bind("start", rangeStart)
                .bind("end", rangeEnd);
        if (minWatt.isPresent()) aggSql = aggSql.bind("minWatt", minWatt.get());
        if (maxWatt.isPresent()) aggSql = aggSql.bind("maxWatt", maxWatt.get());

        return aggSql
                .map((row, meta) -> {
                    Integer total = row.get("total", Integer.class);
                    Double avg = row.get("average", Double.class);
                    return new AbstractMap.SimpleEntry<>(total != null ? total : 0, avg != null ? avg : 0.0);
                })
                .one()
                .zipWith(sortedNamesMono)
                .flatMap(tuple -> {
                    var total = tuple.getT1().getKey();
                    var average = tuple.getT1().getValue();
                    var names = tuple.getT2();

                    if (names.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("No batteries found in the specified range"));
                    }

                    return Mono.just(new BatteryAggregateResponse(names, total, average));
                });
    }

}
