package com.asd.services;

import java.io.IOException;

public interface ReportService {
    byte[] generateReport(String type, String format) throws IOException;
    byte[] generateReportFiltered(String type, String format, String start, String end) throws IOException;
}
