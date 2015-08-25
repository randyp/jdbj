package com.github.randyp.jdbj.example.tax;

import java.math.BigDecimal;

public class Cost {
    
    private final BigDecimal pennies;

    public Cost(BigDecimal pennies) {
        this.pennies = pennies.setScale(3, BigDecimal.ROUND_HALF_EVEN);
    }
    
    
}
