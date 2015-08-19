package com.github.randyp.jdbj.db.postgres_9_4;

import com.github.randyp.jdbj.test.query.ExecuteQueryRunnableTest;
import com.github.randyp.jdbj.test.query.ExecuteQueryTest;
import org.junit.ClassRule;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;

@RunWith(Enclosed.class)
public class QueriesTest {

    @ClassRule
    public static final PGRule db = new PGRule();

    public static class ExecuteQuery extends ExecuteQueryTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteQueryRunnable extends ExecuteQueryRunnableTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

}
