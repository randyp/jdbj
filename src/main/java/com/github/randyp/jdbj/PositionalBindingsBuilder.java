package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.Binding;
import com.github.randyp.jdbj.lambda.PositionalBindingsBuilderFactory;

import javax.annotation.concurrent.Immutable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Immutable
public class PositionalBindingsBuilder<E extends PositionalBindingsBuilder<E>> implements ListBindingsBuilder<E> {

    final NamedParameterStatement statement;
    final PositionalBindings bindings;
    final PositionalBindingsBuilderFactory<E> factory;

    PositionalBindingsBuilder(NamedParameterStatement statement, PositionalBindings bindings, PositionalBindingsBuilderFactory<E> factory){
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
    public E bind(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, bindings.valueBinding(name, binding));
    }

    public E bindDefault(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, bindings.defaultValueBinding(name, binding));
    }

    @Override
    public E bindList(String name, List<Binding> bindings) {
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, this.bindings.listBinding(name, bindings));
    }

    public E bindDefaultList(String name, List<Binding> bindings) {
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, this.bindings.defaultListBinding(name, bindings));
    }
}
