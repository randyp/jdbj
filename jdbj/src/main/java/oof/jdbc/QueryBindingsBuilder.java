package oof.jdbc;


import javax.annotation.concurrent.Immutable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Immutable
public class QueryBindingsBuilder<R> extends DecoratesBindingBuilder<QueryBindingsBuilder<R>> {

    private final ResultSetToResult<R> toResult;

    QueryBindingsBuilder(BindingsBuilder bindingsBuilder, ResultSetToResult<R> toResult) {
        super(bindingsBuilder);
        this.toResult = toResult;
    }

    public R execute(Connection connection) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement(bindingsBuilder.buildSql())){
            bindingsBuilder.bindToStatement(ps);
            try(ResultSet rs = ps.executeQuery()){
                return toResult.from(rs);
            }
        }
    }

    @Override
    protected QueryBindingsBuilder<R> prototype(BindingsBuilder newBindings) {
        return new QueryBindingsBuilder<>(newBindings, toResult);
    }
}
