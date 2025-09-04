grammar AeroScript;

@header {
package no.uio.aeroscript.antlr;
}

// Whitespace and comments added
WS           : [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT      : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

LCURL   : '{';
RCURL   : '}';
LSQUARE : '[';
RSQUARE : ']';
LPAREN  : '(';
RPAREN  : ')';

SEMI    : ';';
COMMA   : ',';

NEG     : '--';
PLUS    : '+';
MINUS   : '-';
TIMES   : '*';
RANDOM  : 'random';
POINT   : 'point';
PRINT   : 'print';

// Keywords
NUMBER: '-'?[0-9]+('.'[0-9]+)?;

// Entry point
program : (statement)* EOF;

statement : 
    expression SEMI?                              # ExprStmt
    | PRINT expression SEMI?                      # PrintStmt
    ;

expression : 
    NUMBER                                    # NumberExpr
    | LPAREN expression RPAREN               # ParenExpr
    | expression PLUS expression             # PlusExpr
    | expression MINUS expression            # MinusExpr
    | expression TIMES expression            # TimesExpr
    | NEG expression                         # NegExpr
    | RANDOM range                           # RandomExpr
    | POINT expression expression            # PointExpr
    ;

point : LPAREN expression COMMA expression RPAREN;

range : LSQUARE expression COMMA expression RSQUARE;