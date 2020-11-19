package com.akaene.flagship.service.repository;

import com.akaene.flagship.model.Report;
import com.akaene.flagship.persistence.dao.ReportDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
public class ReportRepositoryService {

    private final ReportDao reportDao;

    public ReportRepositoryService(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public List<Report> findAll() {
        return reportDao.findAll();
    }

    @Transactional
    public void createReport(Report report) {
        reportDao.persist(report);
    }

    public Report find(URI reportUri) {
        return reportDao.find(reportUri);
    }
}
