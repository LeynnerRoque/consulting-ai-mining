package org.consulting.ai.mining.api;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class HealthController {

    @GET
    @Path("/health")
    public Response getHealth(){
        return Response.ok("API is healthy").build();
    }





}
