package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lexer.StatementsLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Execute a series of sql statements separated by {@code ';'}. Example:
 * <pre>
 * {@code
 * String create = "CREATE TABLE student(id SERIAL, first_name VARCHAR, last_name VARCHAR, gpa VARCHAR)";
 * String addPrimaryKey = "ALTER student add PRIMARY KEY(id)"
 * JDBJ.script(create + ";" + addPrimaryKey)
 *     .execute(db);
 * }    
 * </pre>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * <p>
 * Encapsulates the execution of multiple {@link PreparedStatement#execute()} by parsing the script into multiple statements and executing them, while adding JDBJ features.
 * <p>
 * This is useful because {@link PreparedStatement} cannot execute multiple statements.
 * @see JDBJ#script(String) 
 * @see ExecuteStatement
 */
@Immutable
@ThreadSafe
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
        this(script, new PositionalBindings(), statements);
    }

    ExecuteScript(NamedParameterStatement script, PositionalBindings bindings, List<NamedParameterStatement> statements) {
        super(script, bindings, (s,b) -> new ExecuteScript(s, b, statements));
        this.statements = statements;
    }

    public boolean[] execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public boolean[] execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
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
