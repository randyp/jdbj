package com.github.randyp.jdbj.example.tax;

import io.dropwizard.db.DataSourceFactory;

public class SalesTaxDataSourceFactory extends DataSourceFactory {

    public SalesTaxDataSourceFactory() {
        super();
        setUrl("jdbc:derby:./salestaxdb");
        setMaxSize(12);
        setMinSize(1);
        setInitialSize(1);
        setUser("");
    }
}
