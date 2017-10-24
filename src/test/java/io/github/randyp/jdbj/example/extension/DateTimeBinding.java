package io.github.randyp.jdbj.example.extension;

import io.github.randyp.jdbj.PreparedColumn;
import io.github.randyp.jdbj.lambda.Binding;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DateTimeBinding implements Binding {

    private final DateTime x;

    public DateTimeBinding(@Nullable DateTime x) {
        this.x = x;
    }

    @Override
    public void bind(PreparedColumn preparedColumn) throws SQLException {
        Timestamp t = x == null ? null : new Timestamp(x.getMillis());
        preparedColumn.setTimestamp(t);
    }
}
