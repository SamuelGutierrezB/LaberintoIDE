package analizador;

import java_cup.runtime.Symbol;

%%
%class AnalizadorLexico
%unicode
%line
%column
%cup

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

// Definiciones léxicas
DIGITO = [0-9]
NUMERO = {DIGITO}+
LETRA = [a-zA-Z]
ID = {LETRA}({LETRA}|{DIGITO})*
ESPACIO = [ \t\r\n]
COMA = ","
PAREN_IZQ = "("
PAREN_DER = ")"

%%
// Reglas léxicas
<YYINITIAL> {
    "room"          { return symbol(sym.ROOM); }
    "wall"          { return symbol(sym.WALL); }
    "door"          { return symbol(sym.DOOR); }
    "monster"       { return symbol(sym.MONSTER); }
    "item"          { return symbol(sym.ITEM); }
    "START"         { return symbol(sym.START); }
    "END"           { return symbol(sym.END); }
    "H"|"V"         { return symbol(sym.DIRECCION, yytext()); }
    {NUMERO}        { return symbol(sym.NUMERO, Integer.parseInt(yytext())); }
    {COMA}          { return symbol(sym.COMA); }
    {PAREN_IZQ}     { return symbol(sym.PAREN_IZQ); }
    {PAREN_DER}     { return symbol(sym.PAREN_DER); }
    {ESPACIO}       { /* Ignorar */ }
    .               { System.err.println("Error léxico: '" + yytext() + "' en línea " + (yyline+1)); }
}
