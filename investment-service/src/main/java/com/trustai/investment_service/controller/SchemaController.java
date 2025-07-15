package com.trustai.investment_service.controller;

import com.trustai.investment_service.entity.InvestmentSchema;
import com.trustai.investment_service.service.SchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/investment-schemas")
@RequiredArgsConstructor
@Slf4j
public class SchemaController {
    private final SchemaService schemaService;

    @GetMapping
    public ResponseEntity<Page<InvestmentSchema>> getAllSchemas(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Received request for all investment schemas");
        Pageable pageable = PageRequest.of(page, size);
        Page<InvestmentSchema> schemas = schemaService.getAllSchemas(pageable);
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
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<InvestmentSchema> updateSchema(@PathVariable Long id,  @RequestBody Map<String, Object> updates) {
        log.info("Received request to update InvestmentSchema with ID: {} and updates: {}", id, updates.keySet());
        InvestmentSchema updatedSchema = schemaService.updateSchema(id, updates);
        log.info("Successfully updated InvestmentSchema with ID: {}", id);
        return ResponseEntity.ok(updatedSchema );
    }
}
