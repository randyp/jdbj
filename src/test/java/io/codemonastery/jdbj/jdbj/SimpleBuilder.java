package io.codemonastery.jdbj.jdbj;

import io.codemonastery.jdbj.jdbj.lambda.ResultMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleBuilder extends PositionalBindingsBuilder<SimpleBuilder> {

    private static final NamedParameterStatement defaultStatement =
            NamedParameterStatement.make("SELECT CAST(:binding as varchar) as bound");

    public SimpleBuilder() {
        this(defaultStatement, new PositionalBindings());
    }

    public SimpleBuilder(String castType) {
        this(NamedParameterStatement.make("SELECT CAST(:binding as " + castType + ") as bound"),
                new PositionalBindings());
    }

    public SimpleBuilder(NamedParameterStatement statement) {
        this(statement, new PositionalBindings());
    }

    public SimpleBuilder(NamedParameterStatement statement, PositionalBindings bindings){
        super(statement, bindings, SimpleBuilder::new);
    }

    public <R> R execute(DataSource db, ResultMapper<R> mapper) throws SQLException {
        checkAllBindingsPresent();

        try(Connection connection = db.getConnection()){
            return execute(connection, mapper);
        }
    }

    public <R> R execute(Connection connection, ResultMapper<R> mapper) throws SQLException {
        checkAllBindingsPresent();

        final R value;
        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            try (SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                assertTrue(rs.next());
                value = mapper.map(rs);
                assertFalse(rs.next());
            }
        }
        return value;
    }
}

