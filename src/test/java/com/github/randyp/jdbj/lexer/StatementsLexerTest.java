package com.github.randyp.jdbj.lexer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class StatementsLexerTest {

    @Test
     public void singleStatement() throws Exception {
        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream("select id, name from students limit 0 offset 100"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(1, tokens.size());

        assertToken(StatementsLexer.LITERAL, "select id, name from students limit 0 offset 100", tokens.get(0));
    }

    @Test
    public void singleStatementAndEnd() throws Exception {
        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream("select id, name from students limit 0 offset 100;"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(2, tokens.size());

        assertToken(StatementsLexer.LITERAL, "select id, name from students limit 0 offset 100", tokens.get(0));
        assertToken(StatementsLexer.STATEMENT_END, ";", tokens.get(1));
    }

    @Test
    public void multipleStatements() throws Exception {
        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream("select id, name from students limit 0 offset 100; select 2"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(3, tokens.size());

        assertToken(StatementsLexer.LITERAL, "select id, name from students limit 0 offset 100", tokens.get(0));
        assertToken(StatementsLexer.STATEMENT_END, ";", tokens.get(1));
        assertToken(StatementsLexer.LITERAL, " select 2", tokens.get(2));
    }

    @Test
    public void quotedStrings() throws Exception {
        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream("select id, 'Mr aa;' || name from students; select 2"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(5, tokens.size());

        assertToken(StatementsLexer.LITERAL, "select id, ", tokens.get(0));
        assertToken(StatementsLexer.QUOTED_TEXT, "'Mr aa;'", tokens.get(1));
        assertToken(StatementsLexer.LITERAL, " || name from students", tokens.get(2));
        assertToken(StatementsLexer.STATEMENT_END, ";", tokens.get(3));
        assertToken(StatementsLexer.LITERAL, " select 2", tokens.get(4));
    }

    @Test
    public void doubleQuotedStrings() throws Exception {
        final StatementsLexer lexer = new StatementsLexer(new ANTLRInputStream("select id, \"Mr aa;\" || name from students; select 2"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(5, tokens.size());

        assertToken(StatementsLexer.LITERAL, "select id, ", tokens.get(0));
        assertToken(StatementsLexer.DOUBLE_QUOTED_TEXT, "\"Mr aa;\"", tokens.get(1));
        assertToken(StatementsLexer.LITERAL, " || name from students", tokens.get(2));
        assertToken(StatementsLexer.STATEMENT_END, ";", tokens.get(3));
        assertToken(StatementsLexer.LITERAL, " select 2", tokens.get(4));
    }

    private static void assertToken(int type, String text, Token token) {
        assertEquals(text, token.getText());
        assertEquals(type, token.getType());
    }

}