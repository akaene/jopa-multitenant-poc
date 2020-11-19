package com.akaene.flagship.service;

import com.akaene.flagship.dto.ReportDto;
import com.akaene.flagship.dto.mapper.ReportMapper;
import com.akaene.flagship.service.repository.ReportRepositoryService;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepositoryService repositoryService;

    private final ReportMapper mapper;

    public ReportService(ReportRepositoryService repositoryService, ReportMapper mapper) {
        this.repositoryService = repositoryService;
        this.mapper = mapper;
    }

    public List<ReportDto> findAll() {
        return repositoryService.findAll().stream().map(mapper::reportToDto).collect(Collectors.toList());
    }

    public void createReport(ReportDto report) {
        repositoryService.createReport(mapper.dtoToReport(report));
    }

    public ReportDto find(URI reportUri) {
        return mapper.reportToDto(repositoryService.find(reportUri));
    }
}
