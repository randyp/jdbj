package io.github.randyp.jdbj;

import io.github.randyp.jdbj.lexer.NamedParameterStatementLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Knows the named parameters in a jdbj sql statement, generates jdbc sql and binds to a {@link PreparedStatement} given a complete {@link PositionalBindings}.
 * <p>
 * Encapsulates tokenized jdbj SQL so we know the named parameters and where to bind them.
 */
@Immutable
@ThreadSafe
public final class NamedParameterStatement {

    public static NamedParameterStatement make(String sql){
        final NamedParameterStatementLexer lexer = new NamedParameterStatementLexer(new ANTLRInputStream(sql));
        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        final Set<String> namedParameters = tokens.stream()
                .filter(t -> t.getType() == NamedParameterStatementLexer.NAMED_PARAM)
                .map(Token::getText)
                .collect(Collectors.toSet());

        return new NamedParameterStatement(namedParameters, tokens);
    }

    private final Set<String> namedParameters;
    private final List<Token> tokens;
    private final List<String> parametersToBind;

    private NamedParameterStatement(Set<String> namedParameters, List<Token> tokens) {
        this.namedParameters = Collections.unmodifiableSet(namedParameters);
        this.tokens = Collections.unmodifiableList(tokens);
        this.parametersToBind = Collections.unmodifiableList(
                tokens.stream()
                        .filter(t -> t.getType() == NamedParameterStatementLexer.NAMED_PARAM)
                        .map(Token::getText)
                        .collect(Collectors.toList())
        );
    }

    public boolean containsParameter(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return namedParameters.contains(name);
    }

    public String jdbcSql(Bindings bindings) {
        Objects.requireNonNull(bindings, "bindings must not be null");
        final StringBuilder builder = new StringBuilder();

        for (Token token : tokens) {
            if(token.getType() == NamedParameterStatementLexer.NAMED_PARAM){
                final PositionalBinding positionalBinding = bindings.get(token.getText());
                positionalBinding.appendPositionalParametersToQueryString(builder);
            }else{
                builder.append(token.getText());
            }
        }

        return builder.toString();
    }

    public void bind(PreparedStatement ps, Bindings bindings) throws SQLException {
        Objects.requireNonNull(ps, "ps must not be null");
        Objects.requireNonNull(bindings, "bindings must not be null");
        int parameterIndex = 1;
        for (String namedParameter : parametersToBind) {
            final PositionalBinding binding = bindings.get(namedParameter);
            parameterIndex = binding.bind(ps, parameterIndex);
        }
    }

    public void checkAllBindingsPresent(Bindings bindings) {
        Objects.requireNonNull(bindings, "bindings must not be null");
        final Set<String> missingBindings = new HashSet<>(namedParameters);
        missingBindings.removeAll(bindings.keys());
        if(!missingBindings.isEmpty()){
            throw new IllegalStateException("missing bindings, cannot proceed: " + missingBindings);
        }
    }

    public void checkNoExtraBindings(Bindings bindings) {
        Objects.requireNonNull(bindings, "bindings must not be null");
        final HashSet<String> extraKeys = new HashSet<>(bindings.keys());
        extraKeys.removeAll(namedParameters);
        if(!extraKeys.isEmpty()){
            throw new IllegalStateException("extra bindings not present in statement, cannot proceed: " + extraKeys);
        }
    }
}
