package org.goo.debugger

import org.goo.scanner.Scanner
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DebuggerTest {

    private val outContent = ByteArrayOutputStream()

    @BeforeAll
    fun setUp() {
        System.setOut(PrintStream(outContent))
    }

    @BeforeEach
    fun setUpEach() {
        outContent.reset()
    }

    @Nested
    inner class MemoryTest {
        @Test
        fun oneVariable() {
            val text = """
                sub main
                    set a 5
                    print a
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val debugger = Debugger(InputTest("var"))
            debugger.stopPoints.add(1)
            debugger.debug(tokens)

            assertEquals("a -> 5\n", outContent.toString())
        }

        @Test
        fun multipleVariables() {
            val text = """
                sub test
                    set b 8
                sub main
                    set a 6
                    call test
                    print b
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val debugger = Debugger(InputTest("var"))
            debugger.stopPoints.add(4)
            debugger.debug(tokens)
            assertEquals("""
                a -> 6
                b -> 8

            """.trimIndent(), outContent.toString())
        }
    }

    @Nested
    inner class StackTraceTest {
        @Test
        fun onlyMain() {
            val text = """
                sub main
                    set a 5
                    set b 6
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)


            val debugger = Debugger(InputTest("trace"))
            debugger.stopPoints.add(2)
            debugger.debug(tokens)

            assertEquals("Line=0 name=main\n", outContent.toString())
        }

        @Test
        fun testMultipleCalls() {
            val text = """
                sub test
                    set c 7
                    call pop
                sub pop
                    set d 8
                    call fun3
                sub fun3
                    set y 5
                sub main
                    set a 5
                    set b 6
                    call test
            """.trimIndent()
            val scanner = Scanner()
            val tokens = scanner.scan(text)

            val debugger = Debugger(InputTest("trace"))
            debugger.stopPoints.add(6)
            debugger.debug(tokens)

            assertEquals("""
                Line=5 name=fun3
                Line=2 name=pop
                Line=11 name=test
                Line=8 name=main

            """.trimIndent(), outContent.toString())
        }
    }
}