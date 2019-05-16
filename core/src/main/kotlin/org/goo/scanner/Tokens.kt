package org.goo.scanner

enum class Tokens(val text: String) {
    SUB("sub"), CALL("call"), FUNC_DECLARATION("undefined"), SET("set"), PRINT("print"),
    NEWLINE("\n"), IDENTIFIER("undefined"), NUMERIC("undefined"), ;
}