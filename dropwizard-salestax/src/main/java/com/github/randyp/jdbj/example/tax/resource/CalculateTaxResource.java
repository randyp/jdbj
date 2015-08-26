package com.github.randyp.jdbj.example.tax.resource;

import com.github.randyp.jdbj.example.tax.dto.Invoice;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalculateTaxResource {
    
    @POST
    public Invoice calculate(@Valid Invoice.Builder invoice){
        return invoice.id(1).build();
    }
}
