package com.akaene.flagship.persistence.dao;

import com.akaene.flagship.model.Report;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao extends BaseDao<Report> {

    public ReportDao(EntityManager em) {
        super(Report.class, em);
    }
}
