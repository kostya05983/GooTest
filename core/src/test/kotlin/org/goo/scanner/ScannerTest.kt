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
        assertEquals(Token(Tokens.SUB, "sub", 0), tokens[0])
        assertEquals(Token(Tokens.IDENTIFIER, "main", 0), tokens[1])
        assertEquals(Token(Tokens.NEWLINE, "\n", 0), tokens[2])
        assertEquals(Token(Tokens.CALL, "call", 1), tokens[3])
        assertEquals(Token(Tokens.IDENTIFIER, "main", 1), tokens[4])
        assertEquals(Token(Tokens.NEWLINE, "\n", 1), tokens[5])
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

        assertEquals(Token(Tokens.SUB, "sub", 0), tokens[0])
        assertEquals(Token(Tokens.IDENTIFIER, "main", 0), tokens[1])
        assertEquals(Token(Tokens.NEWLINE, "\n", 0), tokens[2])
        assertEquals(Token(Tokens.CALL, "call", 1), tokens[3])
        assertEquals(Token(Tokens.IDENTIFIER, "fun", 1), tokens[4])
        assertEquals(Token(Tokens.NEWLINE, "\n", 1), tokens[5])
        assertEquals(Token(Tokens.PRINT, "print", 2), tokens[6])
        assertEquals(Token(Tokens.IDENTIFIER, "a", 2), tokens[7])
        assertEquals(Token(Tokens.NEWLINE, "\n", 2), tokens[8])
        assertEquals(Token(Tokens.SUB, "sub", 3), tokens[9])
        assertEquals(Token(Tokens.IDENTIFIER, "fun", 3), tokens[10])
        assertEquals(Token(Tokens.NEWLINE, "\n", 3), tokens[11])
        assertEquals(Token(Tokens.SET, "set", 4), tokens[12])
        assertEquals(Token(Tokens.IDENTIFIER, "a", 4), tokens[13])
        assertEquals(Token(Tokens.NUMERIC, "5", 4), tokens[14])
    }

    @Test
    fun testMoreEnters() {
        val text = """
            sub test
                set b 5
                print a
                call test2

            sub test2
                print b

            sub main
                set c 8
                call test
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        assertEquals(Token(Tokens.SUB, "sub", 0), tokens[0])
        assertEquals(Token(Tokens.IDENTIFIER, "test", 0), tokens[1])
        assertEquals(Token(Tokens.NEWLINE, "\n", 0), tokens[2])

        assertEquals(Token(Tokens.SET, "set", 1), tokens[3])
        assertEquals(Token(Tokens.IDENTIFIER, "b", 1), tokens[4])
        assertEquals(Token(Tokens.NUMERIC, "5", 1), tokens[5])
        assertEquals(Token(Tokens.NEWLINE, "\n", 1), tokens[6])

        assertEquals(Token(Tokens.PRINT, "print", 2), tokens[7])
        assertEquals(Token(Tokens.IDENTIFIER, "a", 2), tokens[8])
        assertEquals(Token(Tokens.NEWLINE, "\n", 2), tokens[9])

        assertEquals(Token(Tokens.CALL, "call", 3), tokens[10])
        assertEquals(Token(Tokens.IDENTIFIER, "test2", 3), tokens[11])
        assertEquals(Token(Tokens.NEWLINE, "\n", 3), tokens[12])

        assertEquals(Token(Tokens.NEWLINE, "\n", 4), tokens[13])

        assertEquals(Token(Tokens.SUB, "sub", 5), tokens[14])
    }

}