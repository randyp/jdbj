package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lexer.StatementsLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Immutable
public final class ExecuteScript extends PositionalBindingsBuilder<ExecuteScript> {

    public static ExecuteScript from(String script) {
        final List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();

        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream(script));
        Token token = lexer.nextToken();
        while (token.getType() != StatementsLexer.EOF) {
            switch (token.getType()) {
                case StatementsLexer.STATEMENT_END:
                    statements.add(currentStatement.toString());
                    currentStatement = new StringBuilder();
                    break;
                case StatementsLexer.LITERAL:
                case StatementsLexer.QUOTED_TEXT:
                case StatementsLexer.DOUBLE_QUOTED_TEXT:
                default:
                    currentStatement.append(token.getText());
                    break;
            }
            token = lexer.nextToken();
        }
        statements.add(currentStatement.toString());

        final NamedParameterStatement scriptStatement = NamedParameterStatement.make(script);
        final List<NamedParameterStatement> scriptStatements = statements.stream()
                .filter(s -> !s.trim().isEmpty())
                .map(NamedParameterStatement::make)
                .collect(Collectors.toList());

        return new ExecuteScript(scriptStatement, scriptStatements);
    }

    private final List<NamedParameterStatement> statements;


    ExecuteScript(NamedParameterStatement script, List<NamedParameterStatement> statements) {
        this(script, PositionalBindings.empty(), statements);
    }

    ExecuteScript(NamedParameterStatement script, PositionalBindings bindings, List<NamedParameterStatement> statements) {
        super(script, bindings, (s,b) -> new ExecuteScript(s, b, statements));
        this.statements = statements;
    }

    public boolean[] execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();
        boolean[] results = new boolean[statements.size()];
        for (int i = 0; i < statements.size(); i++) {
            NamedParameterStatement statement = statements.get(i);
            try (PreparedStatement ps = connection.prepareStatement(statement.jdbcSql(bindings))) {
                statement.bind(ps, bindings);
                results[i] = ps.execute();
            }
        }
        return results;
    }
}
