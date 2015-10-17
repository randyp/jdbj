package io.codemonastery.jdbj;

import io.codemonastery.jdbj.lambda.Binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Not intended for external use.
 */
final class ListBinding implements PositionalBinding {

    private final List<Binding> bindings;

    ListBinding(List<Binding> bindings) {
        this.bindings = Collections.unmodifiableList(new ArrayList<>(bindings));
    }

    List<Binding> getBindings() {
        return bindings;
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
