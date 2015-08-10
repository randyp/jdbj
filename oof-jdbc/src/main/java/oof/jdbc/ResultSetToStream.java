package oof.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public interface ResultSetToStream<R> {

    Stream<R> stream(ResultSet rs) throws SQLException;

}
