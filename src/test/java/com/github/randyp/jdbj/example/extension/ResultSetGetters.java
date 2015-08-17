package com.github.randyp.jdbj.example.extension;

import com.github.randyp.jdbj.SmartResultSet;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.sql.Timestamp;

public class ResultSetGetters {

    public static DateTime getDateTime(SmartResultSet rs, String columnName) throws SQLException {
        final Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp == null ? null : new DateTime(timestamp.getTime());
    }

    private ResultSetGetters() {
    }
}
