package org.goo.scanner

/**
 * Responsible for determine scanned token
 */
class TokenDeterminer {

    fun getToken(token: String, line: Int): Token? {
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
            Tokens.NEWLINE.text -> {
                Token(Tokens.NEWLINE, token, line)
            }
            Tokens.RANDOM.text -> {
                Token(Tokens.RANDOM, token, line)
            }
            "" -> {
                null
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