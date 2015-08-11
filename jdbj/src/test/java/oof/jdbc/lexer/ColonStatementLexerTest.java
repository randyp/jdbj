package oof.jdbc.lexer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

public class ColonStatementLexerTest {

    @Test
    public void namedParams() throws Exception {
        final ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRInputStream("select id, name from students limit :limit offset :offset"));

        Token token = lexer.nextToken();
        while(token.getType() != ColonStatementLexer.EOF){
            System.out.println(token);

            token = lexer.nextToken();
        }

    }
}