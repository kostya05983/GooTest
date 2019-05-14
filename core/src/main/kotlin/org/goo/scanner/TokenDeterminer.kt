package org.goo.scanner

class TokenDeterminer {

    fun getToken(token: String): Token {
        return when (token) {
            Tokens.SET.text -> {
                Token(Tokens.SET, token)
            }
            Tokens.PRINT.text -> {
                Token(Tokens.PRINT, token)
            }
            Tokens.CALL.text -> {
                Token(Tokens.CALL, token)
            }
            Tokens.SUB.text -> {
                Token(Tokens.SUB, token)
            }
            else -> {
                if (token.toIntOrNull() == null) {
                    Token(Tokens.IDENTIFIER, token)
                } else {
                    Token(Tokens.NUMERIC, token)
                }
            }
        }
    }
}