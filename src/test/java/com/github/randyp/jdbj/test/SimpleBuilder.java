package com.github.randyp.jdbj.test;

import com.github.randyp.jdbj.NamedParameterStatement;
import com.github.randyp.jdbj.PositionalBindings;
import com.github.randyp.jdbj.PositionalBindingsBuilder;
import com.github.randyp.jdbj.SmartResultSet;
import com.github.randyp.jdbj.lambda.ResultSetMapper;

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
        this(defaultStatement, PositionalBindings.empty());
    }

    public SimpleBuilder(String castType) {
        this(NamedParameterStatement.make("SELECT CAST(:binding as " + castType + ") as bound"),
                PositionalBindings.empty());
    }

    public SimpleBuilder(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    public SimpleBuilder(NamedParameterStatement statement, PositionalBindings bindings){
        super(statement, bindings, SimpleBuilder::new);
    }

    public <R> R execute(DataSource db, ResultSetMapper<R> mapper) throws SQLException {
        checkAllBindingsPresent();

        try(Connection connection = db.getConnection()){
            return execute(connection, mapper);
        }
    }

    public <R> R execute(Connection connection, ResultSetMapper<R> mapper) throws SQLException {
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

