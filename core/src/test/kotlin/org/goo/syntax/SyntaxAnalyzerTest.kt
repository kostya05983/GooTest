package org.goo.syntax

import org.goo.scanner.Scanner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SyntaxAnalyzerTest {


    @Nested
    inner class TestWithOneFun {
        @Test
        fun testCall() {
            val text = """
            sub main
                call test
        """.trimIndent()

            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertEquals(0, errors.size)
        }

        @Test
        fun testMultipleCall() {
            val text = """
                sub main
                    call test
                    call main
                    call t
            """.trimIndent()

            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertEquals(0, errors.size)
        }

        @Test
        fun testOneSet() {
            val text = """
                sub main
                    set a 5
            """.trimIndent()

            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertEquals(0, errors.size)
        }

        @Test
        fun testMultipleSet() {
            val text = """
                sub main
                    set a 5
                    set b 6
                    set t 8
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertEquals(0, errors.size)
        }

        @Test
        fun testPrint() {
            val text = """
                sub main
                    set a 5
                    print a
                    print a
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertEquals(0, errors.size)
        }

        @Test
        fun testMultiple() {
            val text = """
                sub foo
                    print a
                sub main
                    set a 5
                    call foo
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertEquals(0, errors.size)
        }
    }

    @Nested
    inner class TestWithErrors {
        @Test
        fun withoutFun() {
            val text = """
                set a 5
            """.trimIndent()
            val scanner = Scanner()

            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertNotEquals(0, errors.size)
        }

        @Test
        fun testNotEndSet() {
            val text = """
                set a
            """.trimIndent()
            val scanner = Scanner()

            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertNotEquals(0, errors.size)
        }

        @Test
        fun testNotEndSub() {
            val text = """
                sub
            """.trimIndent()
            val scanner = Scanner()

            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertNotEquals(0, errors.size)
        }

        @Test
        fun testNotEndCall() {
            val text = """
                call
            """.trimIndent()
            val scanner = Scanner()

            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertNotEquals(0, errors.size)
        }

        @Test
        fun testNotEndPrint() {
            val text = """
                print
            """.trimIndent()
            val scanner = Scanner()

            val tokens = scanner.scan(text)

            val analyzer = SyntaxAnalyzer()

            val errors = analyzer.analyze(tokens)

            assertNotEquals(0, errors.size)
        }
    }

    @Test
    fun longSub() {
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

        val analyzer = SyntaxAnalyzer()

        val errors = analyzer.analyze(tokens)

        assertEquals(0, errors.size)
    }

}