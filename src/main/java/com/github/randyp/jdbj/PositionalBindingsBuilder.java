package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.Binding;
import com.github.randyp.jdbj.lambda.HasBindings;

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
public abstract class PositionalBindingsBuilder<P extends PositionalBindingsBuilder<P>> implements CollectionBindingsBuilder<P>, ValueBindingsBuilder<P> {

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

    /**
     * Allows you to bind objects who can create bindings for themselves, or at the very least lambdas.
     * @param hasBindings
     * @return self with new bindings
     * @throws SQLException
     */
    public P bind(HasBindings hasBindings) throws SQLException {
        final PositionalBindings bindings = hasBindings.bindings();
        statement.checkNoExtraBindings(bindings);
        return factory.make(statement, this.bindings.addAll(bindings));
    }
    
    @Override
    public P bind(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }
        return factory.make(statement, bindings.bind(name, binding));
    }

    @Override
    public P bindCollection(String name, List<Binding> bindings) {
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }
        return factory.make(statement, this.bindings.bindCollection(name, bindings));
    }
}
