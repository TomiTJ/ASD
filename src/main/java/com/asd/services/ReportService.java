package com.asd.services;

import java.io.IOException;

public interface ReportService {
    byte[] generateReport(String type, String format) throws IOException;
}
