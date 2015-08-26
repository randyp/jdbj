package com.github.randyp.jdbj.example.tax.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.randyp.jdbj.example.tax.Cost;

import java.io.IOException;
import java.math.BigDecimal;

public class CostDeserializer extends JsonDeserializer<Cost> {
    @Override
    public Cost deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String valueAsString = jsonParser.getValueAsString();
        final BigDecimal dollars;
        try {
            dollars = new BigDecimal(valueAsString); 
        } catch (NumberFormatException e){
            throw new InvalidFormatException("Could not parse cost string as number", jsonParser.getCurrentLocation(), valueAsString, Cost.class);
        }
        return new Cost(dollars);
    }
}
