package com.asd.services;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {
    List<AuditDto> list(Action action, LocalDateTime from, LocalDateTime to);
}