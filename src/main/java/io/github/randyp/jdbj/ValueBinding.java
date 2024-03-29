package io.github.randyp.jdbj;

import io.github.randyp.jdbj.lambda.Binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Not intended for external use.
 */
final class ValueBinding implements PositionalBinding {

    private final Binding binding;

    ValueBinding(Binding binding) {
        this.binding = binding;
    }

    Binding getBinding() {
        return binding;
    }

    @Override
    public int bind(PreparedStatement ps, int parameterIndex) throws SQLException {
        final PreparedColumn preparedColumn = new PreparedColumn(ps, parameterIndex);
        binding.bind(preparedColumn);
        preparedColumn.setNullIfNotSet();
        return parameterIndex+1;
    }

    @Override
    public void appendPositionalParametersToQueryString(StringBuilder builder) {
        builder.append("?");
    }

}
