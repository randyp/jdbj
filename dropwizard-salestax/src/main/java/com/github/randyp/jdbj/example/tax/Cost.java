package com.github.randyp.jdbj.example.tax;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.randyp.jdbj.example.tax.dto.CostDeserializer;
import com.github.randyp.jdbj.example.tax.dto.CostSerializer;

import java.math.BigDecimal;

@JsonSerialize(using = CostSerializer.class)
@JsonDeserialize(using = CostDeserializer.class)
public class Cost {

    private final BigDecimal dollars;

    public Cost(BigDecimal dollars) {
        this.dollars = dollars.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    @Override
    public String toString() {
        return dollars.toString();
    }

}
