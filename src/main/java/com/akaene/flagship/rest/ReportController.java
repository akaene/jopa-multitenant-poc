package com.akaene.flagship.rest;

import com.akaene.flagship.dto.ReportDto;
import com.akaene.flagship.exception.NotFoundException;
import com.akaene.flagship.model.Report;
import com.akaene.flagship.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest/reports")
@PreAuthorize("isAuthenticated()")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportDto> getAll() {
        return reportService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody ReportDto report) {
        reportService.createReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/{fragment}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportDto getById(@PathVariable String fragment,
                             @RequestParam String namespace) {
        final URI reportUri = URI.create(namespace + fragment);
        final ReportDto result = reportService.find(reportUri);
        if (result == null) {
            throw NotFoundException.create(Report.class, reportUri);
        }
        return result;
    }
}
