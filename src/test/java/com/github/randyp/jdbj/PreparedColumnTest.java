package com.github.randyp.jdbj;

import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreparedColumnTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    @Test
    public void isNotSet() throws Exception {
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){

            final PreparedColumn pc = new PreparedColumn(ps, 1);
            assertFalse(pc.isSet());
            pc.setObject(null);
            assertTrue(pc.isSet());
        }
    }
}