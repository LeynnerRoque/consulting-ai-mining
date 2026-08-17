package org.consulting.ai.mining.business.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.consulting.ai.mining.business.dto.AnaliseVagaDTO;
import org.consulting.ai.mining.business.dto.ConsultingUsageMetricDTO;
import org.consulting.ai.mining.domain.model.ConsultingUsageMetric;

import java.math.BigDecimal;

@ApplicationScoped
public class ConsultingUsageMetricMapper {

    public ConsultingUsageMetricDTO map(ConsultingUsageMetric metric) {
        if (metric == null) {
            return null;
        }
        return  new ConsultingUsageMetricDTO(
                metric.executionTimestamp,
                metric.matchScore,
                metric.seniorityDetected,
                metric.technologiesMatchedCount,
                metric.gapsCount,
                metric.status
        );
    }


    public  ConsultingUsageMetric toEntity(AnaliseVagaDTO dto) {
        ConsultingUsageMetric metric = new ConsultingUsageMetric();
        metric.matchScore = BigDecimal.valueOf(dto.scoreCompatibilidade());
        metric.seniorityDetected = dto.classificacaoTier();
        metric.technologiesMatchedCount = dto.pontosMatchForte() != null ? dto.pontosMatchForte().size() : 0;
        metric.gapsCount = dto.gapsIdentificados() != null ? dto.gapsIdentificados().size() : 0;
        metric.status = "SUCCESS";
        return metric;
    }
}
