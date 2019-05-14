package org.goo.scanner

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class ScannerTest {

    @Test
    fun testOnlyOneFun() {
        val text = """
            sub main
                call main
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)
        assertEquals(Token(Tokens.SUB, "sub"), tokens[0])
        assertEquals(Token(Tokens.IDENTIFIER, "main"), tokens[1])
        assertEquals(Token(Tokens.NEWLINE, "\n"), tokens[2])
        assertEquals(Token(Tokens.CALL, "call"), tokens[3])
        assertEquals(Token(Tokens.IDENTIFIER, "main"), tokens[4])
        assertEquals(Token(Tokens.NEWLINE, "\n"), tokens[5])
    }

    @Test
    fun testMultiFun() {
        val text = """
            sub main
                call fun
                print a
            sub fun
                set a 5
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        assertEquals(Token(Tokens.SUB, "sub"), tokens[0])
        assertEquals(Token(Tokens.IDENTIFIER, "main"), tokens[1])
        assertEquals(Token(Tokens.NEWLINE, "\n"), tokens[2])
        assertEquals(Token(Tokens.CALL, "call"), tokens[3])
        assertEquals(Token(Tokens.IDENTIFIER, "fun"), tokens[4])
        assertEquals(Token(Tokens.NEWLINE, "\n"), tokens[5])
        assertEquals(Token(Tokens.PRINT, "print"), tokens[6])
        assertEquals(Token(Tokens.IDENTIFIER, "a"), tokens[7])
        assertEquals(Token(Tokens.NEWLINE, "\n"), tokens[8])
        assertEquals(Token(Tokens.SUB, "sub"), tokens[9])
        assertEquals(Token(Tokens.IDENTIFIER, "fun"), tokens[10])
        assertEquals(Token(Tokens.NEWLINE, "\n"), tokens[11])
        assertEquals(Token(Tokens.SET, "set"), tokens[12])
        assertEquals(Token(Tokens.IDENTIFIER, "a"), tokens[13])
        assertEquals(Token(Tokens.NUMERIC, "5"), tokens[14])
    }

}