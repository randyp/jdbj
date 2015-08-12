package oof.jdbc.lexer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColonStatementLexerTest {

    @Test
     public void namedParams() throws Exception {
        final ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRInputStream("select id, name from students limit :limit offset :offset"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(4, tokens.size());

        assertToken(ColonStatementLexer.LITERAL, "select id, name from students limit ", tokens.get(0));
        assertToken(ColonStatementLexer.NAMED_PARAM, ":limit", tokens.get(1));
        assertToken(ColonStatementLexer.LITERAL, " offset ", tokens.get(2));
        assertToken(ColonStatementLexer.NAMED_PARAM, ":offset", tokens.get(3));
    }

    @Test
    public void quotedStrings() throws Exception {
        final ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRInputStream("select id, 'Mr :aa' || name from students limit :limit offset :offset"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(6, tokens.size());

        assertToken(ColonStatementLexer.LITERAL, "select id, ", tokens.get(0));
        assertToken(ColonStatementLexer.QUOTED_TEXT, "'Mr :aa'", tokens.get(1));
        assertToken(ColonStatementLexer.LITERAL, " || name from students limit ", tokens.get(2));
        assertToken(ColonStatementLexer.NAMED_PARAM, ":limit", tokens.get(3));
        assertToken(ColonStatementLexer.LITERAL, " offset ", tokens.get(4));
        assertToken(ColonStatementLexer.NAMED_PARAM, ":offset", tokens.get(5));
    }

    @Test
    public void doubleQuotedStrings() throws Exception {
        final ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRInputStream("select id, \"Mr :aa\" || name from students limit :limit offset :offset"));

        //noinspection unchecked
        final List<Token> tokens = (List<Token>) lexer.getAllTokens();
        assertEquals(6, tokens.size());

        assertToken(ColonStatementLexer.LITERAL, "select id, ", tokens.get(0));
        assertToken(ColonStatementLexer.DOUBLE_QUOTED_TEXT, "\"Mr :aa\"", tokens.get(1));
        assertToken(ColonStatementLexer.LITERAL, " || name from students limit ", tokens.get(2));
        assertToken(ColonStatementLexer.NAMED_PARAM, ":limit", tokens.get(3));
        assertToken(ColonStatementLexer.LITERAL, " offset ", tokens.get(4));
        assertToken(ColonStatementLexer.NAMED_PARAM, ":offset", tokens.get(5));
    }

    private static void assertToken(int type, String text, Token token) {
        assertEquals(text, token.getText());
        assertEquals(type, token.getType());
    }

}