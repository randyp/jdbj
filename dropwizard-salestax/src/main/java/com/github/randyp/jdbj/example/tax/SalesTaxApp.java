package com.github.randyp.jdbj.example.tax;

import com.github.randyp.jdbj.example.tax.resource.CalculateTaxResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public class SalesTaxApp extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new SalesTaxApp().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(new CalculateTaxResource());
    }
}
