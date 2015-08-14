lexer grammar NamedParameterStatementLexer;

LITERAL: ('a'..'z' | 'A'..'Z' | ' ' | '\t' | '\n' | '\r' | '0'..'9' | ',' | '*' | '#' | '.' | '@' | '_' | '!'
          | '=' | ';' | '(' | ')' | '[' | ']' | '+' | '-' | '/' | '>' | '<' | '%' | '&' | '^' | '|'
          | '$' | '~' | '{' | '}' | '`')+ ;
NAMED_PARAM: ':' ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '.' | '#')+;
QUOTED_TEXT: ('\'' ( ESCAPE_SEQUENCE | ~'\'')* '\'');
DOUBLE_QUOTED_TEXT: ('"' (~'"')+ '"');
ESCAPED_TEXT : '\\' . ;

fragment ESCAPE_SEQUENCE:   '\\' '\'';
