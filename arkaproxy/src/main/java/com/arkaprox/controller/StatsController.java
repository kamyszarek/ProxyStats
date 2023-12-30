package com.arkaprox.controller;

import com.arkaprox.SchemaDetails;
import com.arkaprox.query.QueryCounter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatsController {

    private final SchemaDetails schemaDetails;
    private final QueryCounter queryCounter;

    public StatsController() {
        this.queryCounter = QueryCounter.getInstance();
        this.schemaDetails = SchemaDetails.getInstance();
    }

    @GetMapping("/tables")
    public List<String> getTables() {
        return schemaDetails.getTablesInSchema();
    }

    @GetMapping("/queries-count")
    public Map<String, Long> getQueriesNumber() {
        Map<String, Long> queriesCountMap = new HashMap<>();
        queriesCountMap.put("select", queryCounter.getSelectCount());
        queriesCountMap.put("insert", queryCounter.getInsertCount());
        queriesCountMap.put("update", queryCounter.getUpdateCount());
        queriesCountMap.put("delete", queryCounter.getDeleteCount());
        queryCounter.resetQueryCounts();
        return queriesCountMap;
    }

}
