package org.goo.scanner

import java.util.*

class Scanner {
    private val tokenDeterminer = TokenDeterminer()

    fun scan(s: String): List<Token> {
        val lines = s.split("\n")

        val result = LinkedList<Token>()

        for (line in lines) {
            val trimmedLine = line.trim()
            val expectedTokens = trimmedLine.split(" ")

            for (token in expectedTokens) {
                result.add(tokenDeterminer.getToken(token))
            }
            result.add(Token(Tokens.NEWLINE, "\n"))
        }
        return result
    }
}