package org.goo.semantic

import org.goo.scanner.Scanner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SemanticAnalyzerTest {

    @Test
    fun testWithoutIdenticalVariables() {
        val text = """
            sub main
                set a 5
        """.trimIndent()

        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val semanticAnalyzer = SemanticAnalyzer()
        assertTrue(semanticAnalyzer.analyze(tokens))
    }

    @Test
    fun testIdenticalVariable() {
        val text = """
            sub main
                set a 5
                set a 6
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val semanticAnalyzer = SemanticAnalyzer()
        assertTrue(semanticAnalyzer.analyze(tokens))
    }

    @Test
    fun testIdenticalSub() {
        val text = """
            sub test
                set b 4
            sub test
                set c 5
            sub main
                set a 5
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val semanticAnalyzer = SemanticAnalyzer()
        assertFalse(semanticAnalyzer.analyze(tokens))
    }

    @Test
    fun testWithoutMain() {
        val text = """
            sub test
                set a 5
        """.trimIndent()
        val scanner = Scanner()
        val tokens = scanner.scan(text)

        val semanticAnalyzer = SemanticAnalyzer()
        assertFalse(semanticAnalyzer.analyze(tokens))
    }
}