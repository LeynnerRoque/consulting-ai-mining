package org.consulting.ai.mining.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AnaliseVagaDTO(
        @JsonProperty("cargo_vaga") String cargoVaga,
        @JsonProperty("score_compatibilidade") Integer scoreCompatibilidade,
        @JsonProperty("classificacao_tier") String classificacaoTier,
        @JsonProperty("pontos_match_forte") List<String> pontosMatchForte,
        @JsonProperty("gaps_identificados") List<String> gapsIdentificados,
        @JsonProperty("dicas_customizacao") List<String> dicasCustomizacao
) {}