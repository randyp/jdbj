package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.Binding;
import com.github.randyp.jdbj.lambda.PositionalBindingsBuilderFactory;

import javax.annotation.concurrent.Immutable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Immutable
public class PositionalBindingsBuilder<E extends PositionalBindingsBuilder<E>> extends DefaultCollectionBindingsBuilder<E> implements OptionalValueBindingsBuilder<E> {

    final NamedParameterStatement statement;
    final PositionalBindings bindings;
    final PositionalBindingsBuilderFactory<E> factory;

    protected PositionalBindingsBuilder(NamedParameterStatement statement, PositionalBindings bindings, PositionalBindingsBuilderFactory<E> factory){
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

    @Override
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

        return factory.make(statement, this.bindings.collectionBinding(name, bindings));
    }

    @Override
    public E bindDefaultList(String name, List<Binding> bindings) {
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, this.bindings.defaultCollectionBinding(name, bindings));
    }

    @Override
    public E requireDefaultedBindingForOptional(String name) {
        if(!bindings.containsDefaultedBinding(name)){
            throw new IllegalArgumentException("default binding " + name + " not present. when binding optionals, you must have bound a default value eg: bindDefaultLong(\":limit\", 10); this ensures there will always be a binding and never a rebinding");
        }
        try {
            //noinspection unchecked
            return (E) this;
        } catch (ClassCastException e) {
            return factory.make(statement, bindings);
        }
    }
}
