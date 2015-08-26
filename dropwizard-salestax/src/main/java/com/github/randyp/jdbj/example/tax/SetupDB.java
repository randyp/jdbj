package com.github.randyp.jdbj.example.tax;

import com.github.randyp.jdbj.JDBJ;

import javax.sql.DataSource;
import java.sql.Connection;

public class SetupDB {

    public static void main(String[] args) throws Exception {
        final DataSource db = SalesTaxDB.cleanStart();
        try (Connection connection = db.getConnection()) {
            JDBJ.resource("setup_schema.sql").script().execute(connection);
            JDBJ.resource("setup_data_jurisdiction.sql").script().execute(connection);
        }
    }

}
