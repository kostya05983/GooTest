package org.goo.scanner

import java.util.*

class Scanner {
    private val tokenDeterminer = TokenDeterminer()

    fun scan(s: String): List<Token> {
        val lines = s.split("\n")

        val result = LinkedList<Token>()

        for (i in 0 until lines.size) {
            val trimmedLine = lines[i].trim()
            val expectedTokens = trimmedLine.split(" ")

            for (token in expectedTokens) {
                val determined = tokenDeterminer.getToken(token, i)
                if (determined != null) {
                    result.add(determined)
                }
            }
            result.add(Token(Tokens.NEWLINE, "\n", i))
        }
        return result
    }
}