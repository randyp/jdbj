package com.github.randyp.jdbj.example.tax.dao;

import com.github.randyp.jdbj.example.tax.dto.Invoice;

import java.sql.Connection;

public class InvoiceDAO {
    
    private final Connection connection;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
    }
    
    public Invoice insert(Invoice invoice){
        throw new UnsupportedOperationException();
    }
}
