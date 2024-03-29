package io.github.randyp.jdbj.example.extension;

import io.github.randyp.jdbj.SmartResult;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.sql.Timestamp;

public class ResultSetGetters {

    public static DateTime getDateTime(SmartResult rs, String columnName) throws SQLException {
        final Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp == null ? null : new DateTime(timestamp.getTime());
    }

    private ResultSetGetters() {
    }
}
