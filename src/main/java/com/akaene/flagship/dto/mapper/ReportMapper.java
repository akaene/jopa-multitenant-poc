package com.akaene.flagship.dto.mapper;

import com.akaene.flagship.dto.ReportDto;
import com.akaene.flagship.model.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    Report dtoToReport(ReportDto dto);

    ReportDto reportToDto(Report report);
}
