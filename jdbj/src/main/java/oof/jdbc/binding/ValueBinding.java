package oof.jdbc.binding;

import oof.jdbc.PreparedColumn;
import oof.jdbc.lambda.Binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class ValueBinding implements PositionalBinding {

    private final Binding binding;

    public ValueBinding(Binding binding) {
        this.binding = binding;
    }

    @Override
    public int bind(PreparedStatement ps, int parameterIndex) throws SQLException {
        binding.bind(new PreparedColumn(ps, parameterIndex));
        return parameterIndex+1;
    }

    @Override
    public void appendPositionalParametersToQueryString(StringBuilder builder) {
        builder.append("?");
    }

}
