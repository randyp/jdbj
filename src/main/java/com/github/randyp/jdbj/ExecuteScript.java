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
public final class ExecuteScript {

    public static ExecuteScript from(String script) {
        final List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();

        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream(script));
        Token token = lexer.nextToken();
        while(token.getType() != StatementsLexer.EOF){
            switch (token.getType()){
                case StatementsLexer.STATEMENT_END:
                    statements.add(currentStatement.toString());
                    currentStatement = new StringBuilder();
                    break;
                case StatementsLexer.LITERAL:
                case StatementsLexer.QUOTED_TEXT:
                case StatementsLexer.DOUBLE_QUOTED_TEXT:
                    currentStatement.append(token.getText());
                    break;
                default:
                    throw new IllegalStateException("unhandled token: " + token);
            }
            token = lexer.nextToken();
        }
        statements.add(currentStatement.toString());

        final List<String> fewerStatements = statements.stream()
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());
        return new ExecuteScript(fewerStatements);
    }

    private final List<String> statements;

    ExecuteScript(List<String> statements) {
        this.statements = new ArrayList<>(statements);
    }

    public boolean[] execute(Connection connection) throws SQLException {
        boolean[] results = new boolean[statements.size()];
        for (int i = 0; i < statements.size(); i++) {
            String statement = statements.get(i);
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                results[i] = ps.execute();
            }
        }
        return results;
    }
}
