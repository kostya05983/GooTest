package org.goo.interpreter

import org.goo.scanner.Scanner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class InterpreterTest {

    @Test
    fun testOneSub() {
        val text = """
            sub main
                set a 5
                print a
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val interpreter = Interpreter()
        interpreter.interpret(tokens)
    }

    @Test
    fun testMultipleSubs() {
        val text = """
            sub test
                print a
            sub main
                set a 6
                call test
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val interpreter = Interpreter()
        interpreter.interpret(tokens)
    }

    @Test
    fun testMutipleSubs() {
        val text = """
            sub main
                set a 5
                print a
                set b 9
                print b
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val interpreter = Interpreter()
        interpreter.interpret(tokens)
    }

    @Test
    fun testRecursion() {
        val text = """
            sub test
                print a
                call test
            sub main
                set a 5
                call test
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val interpreter = Interpreter()
        interpreter.interpret(tokens)
    }
}