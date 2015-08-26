package com.github.randyp.jdbj.example.tax.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.randyp.jdbj.example.tax.Cost;

import java.io.IOException;

public class CostSerializer extends JsonSerializer<Cost> {
    @Override
    public void serialize(Cost cost, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(cost.toString());
    }
}
