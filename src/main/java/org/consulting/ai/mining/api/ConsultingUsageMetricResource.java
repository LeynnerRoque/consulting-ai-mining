package org.consulting.ai.mining.resource;

import org.consulting.ai.mining.business.services.MatchService;
import org.consulting.ai.mining.business.services.PdfService;
import org.consulting.ai.mining.business.dto.ConsultingUsageMetricDTO;
import io.smallrye.common.annotation.Blocking;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import java.io.InputStream;

@Path("/api/mining")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class ConsultingUsageMetricResource {

    private final MatchService matchService;
    private final PdfService pdfService;

    public ConsultingUsageMetricResource(MatchService matchService, PdfService pdfService) {
        this.matchService = matchService;
        this.pdfService = pdfService;
    }

    @POST
    @Path("/analyze")
    @Blocking
    public Response analyzeResume(@BeanParam ResumeUploadForm form) {
        try {
            String resumeText = pdfService.extractTextFromPdf(form.resumeFile);
            ConsultingUsageMetricDTO result = matchService.analyzeAndSaveMetric(resumeText, form.jobDescription);
            return Response.ok(result).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar o currículo: " + e.getMessage())
                    .build();
        }
    }

    public static class ResumeUploadForm {
        @RestForm("resume")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public InputStream resumeFile;

        @RestForm("jobDescription")
        @PartType(MediaType.TEXT_PLAIN)
        public String jobDescription;
    }
}