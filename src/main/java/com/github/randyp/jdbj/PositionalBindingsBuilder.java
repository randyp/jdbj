package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Stores bindings, has convenience binding methods for common jdbj types.
 * <p>
 * Abstract class for most all query builder classes such as {@link ExecuteQuery} which are (almost) always {@link Immutable}. 
 * @param <P> prototype type
 */
@Immutable
@ThreadSafe
abstract class PositionalBindingsBuilder<P extends PositionalBindingsBuilder<P>> extends CollectionBindingsBuilder<P> implements ValueBindingsBuilder<P> {

    final NamedParameterStatement statement;
    final PositionalBindings bindings;
    final PositionalBindingsBuilderFactory<P> factory;

    protected PositionalBindingsBuilder(NamedParameterStatement statement, PositionalBindings bindings, PositionalBindingsBuilderFactory<P> factory){
        this.statement = statement;
        this.bindings = bindings;
        this.factory = factory;
    }

    public void checkAllBindingsPresent(){
        statement.checkAllBindingsPresent(bindings);
    }

    public String buildSql() {
        return statement.jdbcSql(bindings);
    }

    public void bindToStatement(PreparedStatement ps) throws SQLException {
        statement.bind(ps, bindings);
    }

    @Override
    public P bind(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, bindings.valueBinding(name, binding));
    }

    @Override
    public P bindList(String name, List<Binding> bindings) {
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, this.bindings.collectionBinding(name, bindings));
    }
}
