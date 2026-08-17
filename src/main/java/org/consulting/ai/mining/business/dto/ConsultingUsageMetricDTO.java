package org.consulting.ai.mining.business.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsultingUsageMetricDTO(
        LocalDateTime executionTimestamp,
        BigDecimal matchScore,
        String seniorityDetected,
        Integer technologiesMatchedCount,
        Integer gapsCount,
        String status
) {
}
