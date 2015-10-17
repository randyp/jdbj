package io.codemonastery.jdbj.test.binding.value;

import javax.sql.DataSource;

public interface DBSupplier {

    DataSource db();

}
