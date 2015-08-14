package jdbj;

import jdbj.binding.PositionalBinding;
import jdbj.lexer.NamedParameterStatementLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.concurrent.Immutable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Immutable
public final class NamedParameterStatement {

    static NamedParameterStatement make(String sql){
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

    public String jdbcSql(ValueBindings bindings) {
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

    public void bind(PreparedStatement ps, ValueBindings bindings) throws SQLException {
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

    public void checkAllBindingsPresent(ValueBindings bindings) {
        if(bindings == null){
            throw new IllegalArgumentException("bindings cannot be null");
        }
        final Set<String> missingBindings = new HashSet<>(namedParameters);
        missingBindings.removeAll(bindings.keys());
        if(!missingBindings.isEmpty()){
            throw new IllegalStateException("missing bindings, cannot proceed: " + missingBindings);
        }
    }
}
