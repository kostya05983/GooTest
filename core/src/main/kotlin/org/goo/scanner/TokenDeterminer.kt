package org.goo.scanner

class TokenDeterminer {

    fun getToken(token: String, line: Int): Token {
        return when (token) {
            Tokens.SET.text -> {
                Token(Tokens.SET, token, line)
            }
            Tokens.PRINT.text -> {
                Token(Tokens.PRINT, token, line)
            }
            Tokens.CALL.text -> {
                Token(Tokens.CALL, token, line)
            }
            Tokens.SUB.text -> {
                Token(Tokens.SUB, token, line)
            }
            else -> {
                if (token.toIntOrNull() == null) {
                    Token(Tokens.IDENTIFIER, token, line)
                } else {
                    Token(Tokens.NUMERIC, token, line)
                }
            }
        }
    }
}