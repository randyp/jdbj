package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lexer.NamedParameterStatementLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return namedParameters.contains(name);
    }

    public String jdbcSql(Bindings bindings) {
        if(bindings == null){
            throw new IllegalArgumentException("bindings cannot be null");
        }
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
        if (ps == null) {
            throw new IllegalArgumentException("ps cannot be null");
        }
        if(bindings == null){
            throw new IllegalArgumentException("bindings cannot be null");
        }
        int parameterIndex = 1;
        for (String namedParameter : parametersToBind) {
            final PositionalBinding binding = bindings.get(namedParameter);
            parameterIndex = binding.bind(ps, parameterIndex);
        }
    }

    /**
     * Looks for named parameters missing from the provided {@link ValueBindings} and throws {@link IllegalArgumentException} if any are missing.
     * @param bindings
     */
    public void checkAllBindingsPresent(Bindings bindings) {
        if(bindings == null){
            throw new IllegalArgumentException("bindings cannot be null");
        }
        final Set<String> missingBindings = new HashSet<>(namedParameters);
        missingBindings.removeAll(bindings.keys());
        if(!missingBindings.isEmpty()){
            throw new IllegalStateException("missing bindings, cannot proceed: " + missingBindings);
        }
    }

    public void checkNoExtraBindings(Bindings bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("bindings cannot be null");
        }
        final HashSet<String> extraKeys = new HashSet<>(bindings.keys());
        extraKeys.removeAll(namedParameters);
        if(!extraKeys.isEmpty()){
            throw new IllegalStateException("extra bindings not present in statement, cannot proceed: " + extraKeys);
        }
    }
}
