package com.github.randyp.jdbj.db.postgres_9_4;

import org.junit.ClassRule;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;

@RunWith(Enclosed.class)
public class ExecuteQueryTest {

    @ClassRule
    public static final PGRule db = new PGRule();

    public static class ExecuteQuery extends com.github.randyp.jdbj.test.query.ExecuteQueryTest {

        @Override
        public DataSource db() {
            return db;
        }
    }


}
