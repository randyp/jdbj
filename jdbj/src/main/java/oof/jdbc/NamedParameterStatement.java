package oof.jdbc;

import oof.jdbc.lambda.Binding;
import oof.jdbc.lexer.ColonStatementLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.concurrent.Immutable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Immutable
public final class NamedParameterStatement {


    static NamedParameterStatement make(String sql){
        final ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRInputStream(sql));
        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        final Set<String> namedParameters = tokens.stream()
                .filter(t -> t.getType() == ColonStatementLexer.NAMED_PARAM)
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
                        .filter(t -> t.getType() == ColonStatementLexer.NAMED_PARAM)
                        .map(Token::getText)
                        .collect(Collectors.toList())
        );
    }

    public Set<String> getNamedParameters() {
        return namedParameters;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public boolean containsParameter(String name) {
        return namedParameters.contains(name);
    }

    public String jdbcSql(Map<String, Binding> bindings) {
        final StringBuilder builder = new StringBuilder();

        for (Token token : tokens) {
            if(token.getType() == ColonStatementLexer.NAMED_PARAM){
                builder.append("?");
            }else{
                builder.append(token.getText());
            }
        }

        return builder.toString();
    }

    public void bind(Map<String, Binding> bindings, PreparedStatement ps) throws SQLException {
        for (int parameterIndex = 1; parameterIndex <= parametersToBind.size(); parameterIndex++) {
            final String namedParameter = parametersToBind.get(parameterIndex - 1);
            final Binding binding = bindings.get(namedParameter);
            if(binding == null){
                throw new IllegalStateException();
            }
            binding.bind(new PreparedColumn(ps, parameterIndex));
        }
    }
}
