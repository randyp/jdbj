lexer grammar ColonStatementLexer;

LITERAL: ('a'..'z' | 'A'..'Z' | ' ' | '\t' | '\n' | '\r' | '0'..'9' | ',' | '*' | '#' | '.' | '@' | '_' | '!'
          | '=' | ';' | '(' | ')' | '[' | ']' | '+' | '-' | '/' | '>' | '<' | '%' | '&' | '^' | '|'
          | '$' | '~' | '{' | '}' | '`')+ ;
COLON: ':';
NAMED_PARAM: COLON ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '.' | '#')+;
QUOTED_TEXT: ('\'' ( ESCAPE_SEQUENCE | ~'\'')* '\'');
DOUBLE_QUOTED_TEXT: ('"' (~'"')+ '"');
ESCAPED_TEXT : '\\' . ;

fragment ESCAPE_SEQUENCE:   '\\' '\'';
