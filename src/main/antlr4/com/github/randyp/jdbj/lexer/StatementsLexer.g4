lexer grammar StatementsLexer;

LITERAL: ('a'..'z' | 'A'..'Z' | ' ' | '\t' | '\n' | '\r' | '0'..'9' | ',' | '*' | '#' | '.' | '@' | '_' | '!'
          | '=' | '(' | ')' | '[' | ']' | '+' | '-' | '/' | '>' | '<' | '%' | '&' | '^' | '|'
          | '$' | '~' | '{' | '}' | '`' | ':' )+ ;
QUOTED_TEXT: ('\'' ( ESCAPE_SEQUENCE | ~'\'')* '\'');
DOUBLE_QUOTED_TEXT: ('"' (~'"')+ '"');
ESCAPED_TEXT : '\\' . ;
STATEMENT_END : ';' ;

fragment ESCAPE_SEQUENCE:   '\\' '\'';
