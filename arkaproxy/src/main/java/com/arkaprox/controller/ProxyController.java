package com.arkaprox.controller;

import com.arkaprox.SchemaDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProxyController {

    @GetMapping("/tables")
    public List<String> getTables() {
        return SchemaDetails.getSchemaDetails("test").getTablesInSchema();
    }

}
