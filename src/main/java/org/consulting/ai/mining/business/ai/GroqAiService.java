package org.consulting.ai.mining.business.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.consulting.ai.mining.business.dto.AnaliseVagaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GroqAiService {

    @ConfigProperty(name = "groq.api.key")
    String groqApiKey;

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public AnaliseVagaDTO analisarCurriculoComGroq(String textoCv, String descricaoVaga) {
        String systemPrompt = """
            Você é um Recrutador e Especialista em Triagem ATS de nível sênior, com expertise em recrutamento transversal para qualquer área de mercado (Tecnologia, Negócios, Gestão, Saúde, Criativa, Operacional, etc.).
            Analise a compatibilidade do currículo recebido com a descrição da vaga informada, independentemente da área de atuação.
            Seja minucioso ao identificar hard skills, soft skills, ferramentas, certificações, metodologias ou experiências prévias relevantes exigidas pela vaga.
            
            Responda OBRIGATORIAMENTE em formato JSON válido seguindo exatamente este schema:
            {
              "cargo_vaga": "string",
              "score_compatibilidade": 0,
              "classificacao_tier": "string",
              "pontos_match_forte": ["string"],
              "gaps_identificados": ["string"],
              "dicas_customizacao": ["string"]
            }
            """;

        String userPrompt = """
            --- CURRÍCULO DO CANDIDATO ---
            %s

            --- DESCRIÇÃO DA VAGA ---
            %s
            """.formatted(textoCv, descricaoVaga);

        try {
            Map<String, Object> requestBodyMap = Map.of(
                    "model", "openai/gpt-oss-120b",
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    ),
                    "response_format", Map.of("type", "json_object"),
                    "temperature", 0.2
            );

            String jsonBody = objectMapper.writeValueAsString(requestBodyMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro na API do Groq (Status " + response.statusCode() + "): " + response.body());
            }

            // Parse da resposta do OpenAI/Groq format
            Map<String, Object> rootResponse = objectMapper.readValue(response.body(), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) rootResponse.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String contentJson = (String) message.get("content");

            // Desserializa o conteúdo gerado pelo Llama direto para o nosso Record DTO
            return objectMapper.readValue(contentJson, AnaliseVagaDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com a API do Groq: " + e.getMessage(), e);
        }
    }
}