package com.github.randyp.jdbj.example.tax.resource;

import com.github.randyp.jdbj.ExecuteInsert;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.example.tax.dto.Invoice;
import com.github.randyp.jdbj.example.tax.dto.Shipping;
import io.dropwizard.db.ManagedDataSource;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Optional;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalculateTaxResource {

    private static  final ExecuteInsert<Long> insertInvoice = JDBJ.resource("dao/invoice/insert.sql").insert(rs -> rs.getLong(1));
    private static final ExecuteInsert<Long> insertShipping = JDBJ.resource("dao/shipping/insert.sql").insert(rs -> rs.getLong(1));
    
    private final ManagedDataSource salesTaxDB;

    public CalculateTaxResource(ManagedDataSource salesTaxDB) {
        this.salesTaxDB = salesTaxDB;
    }

    @POST
    public Invoice calculate(@Valid Invoice.Builder invoiceBuilder) throws SQLException {
        final Invoice invoice = invoiceBuilder.build();
        return JDBJ.transaction(connection -> {
            final long invoiceId;
            {
                final Optional<Long> optional = insertInvoice.bind(invoice::bindInsert).execute(connection).stream().findFirst();
                invoiceId = optional.get();
            }
            for (Shipping shipping : invoice.getShippings()) {
                final long shippindId;
                {
                    final Optional<Long> optional = insertShipping.bind(":shipping_id").bind(shipping::bindInsert).execute(connection).stream().findFirst();
                    shippindId = optional.get();
                }
                
            }
            return (Invoice) null;
        }).execute(salesTaxDB::getConnection);
    }
}
