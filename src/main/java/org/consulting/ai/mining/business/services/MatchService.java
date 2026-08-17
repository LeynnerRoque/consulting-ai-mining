package org.consulting.ai.mining.business.services;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.consulting.ai.mining.business.ai.GroqAiService;
import org.consulting.ai.mining.business.dto.AnaliseVagaDTO;
import org.consulting.ai.mining.business.dto.ConsultingUsageMetricDTO;
import org.consulting.ai.mining.business.mappers.ConsultingUsageMetricMapper;
import org.consulting.ai.mining.domain.model.ConsultingUsageMetric;
import org.consulting.ai.mining.domain.repository.ConsultingUsageRepository;

import java.math.BigDecimal;

@ApplicationScoped
public class MatchService {

    private final ConsultingUsageRepository repository;
    private final ConsultingUsageMetricMapper mapper;
    private final GroqAiService groqAiService;

    public MatchService(ConsultingUsageRepository repository, ConsultingUsageMetricMapper mapper, GroqAiService groqAiService) {
        this.repository = repository;
        this.mapper = mapper;
        this.groqAiService = groqAiService;
    }

    @Transactional
    public ConsultingUsageMetricDTO analyzeAndSaveMetric(String resumeText, String jobDescription) {
        AnaliseVagaDTO analise = groqAiService.analisarCurriculoComGroq(resumeText, jobDescription);
        var entity = mapper.toEntity(analise);
        repository.persist(entity);
        return mapper.map(entity);
    }
}
