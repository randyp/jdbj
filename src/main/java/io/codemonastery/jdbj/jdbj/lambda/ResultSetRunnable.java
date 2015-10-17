package io.codemonastery.jdbj.jdbj.lambda;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Similar {@link Runnable} except receives a {@link ResultSet} and possible throws {@link SQLException}.
 * <p>
 * It is expected that the caller will not call {@link ResultSet#close()}.
 */
public interface ResultSetRunnable {

    void run(ResultSet rs) throws SQLException;

}
