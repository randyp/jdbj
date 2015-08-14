package com.github.randyp.jdbj.binding;

import com.github.randyp.jdbj.PreparedColumn;
import com.github.randyp.jdbj.lambda.Binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public final class ListBinding implements PositionalBinding {

    private final List<Binding> bindings;

    public ListBinding(List<Binding> bindings) {
        this.bindings = bindings;
    }

    @Override
    public int bind(PreparedStatement ps, int parameterIndex) throws SQLException {
        for (int i = 0; i < bindings.size(); i++) {
            final PreparedColumn preparedColumn = new PreparedColumn(ps, parameterIndex + i);
            bindings.get(i).bind(preparedColumn);
            preparedColumn.setNullIfNotSet();
        }
        return parameterIndex + bindings.size();
    }

    @Override
    public void appendPositionalParametersToQueryString(StringBuilder builder) {
        builder.append('(');
        for (int i = 0; i < bindings.size(); i++) {
            if(i > 0){
                builder.append(',');
            }
            builder.append('?');
        }
        builder.append(')');
    }

}
