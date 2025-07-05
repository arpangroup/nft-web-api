package com.trustai.product_service.controller;

import com.trustai.product_service.entity.investment.InvestmentSchema;
import com.trustai.product_service.service.InvestmentSchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investment-schemas")
@RequiredArgsConstructor
@Slf4j
public class InvestmentController {
    private final InvestmentSchemaService schemaService;

    @GetMapping
    public ResponseEntity<List<InvestmentSchema>> getAllSchemas() {
        List<InvestmentSchema> schemas = schemaService.getAllSchemas();
        return ResponseEntity.ok(schemas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentSchema> getSchemaById(@PathVariable Long id) {
        InvestmentSchema schema = schemaService.getSchemaById(id);
        return ResponseEntity.ok(schema);
    }

    @PostMapping
    public ResponseEntity<InvestmentSchema> createSchema(@RequestBody InvestmentSchema investmentSchema) {
        InvestmentSchema created = schemaService.createSchema(investmentSchema);
        return ResponseEntity.status(201).body(created);
    }
}
