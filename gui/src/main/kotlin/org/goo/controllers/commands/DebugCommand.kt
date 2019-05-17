package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.controllers.InputConsoleWindow
import org.goo.debugger.Debugger
import org.goo.interpreter.Interpreter
import org.goo.scanner.Scanner
import org.goo.scanner.Token
import org.goo.semantic.SemanticAnalyzer
import org.goo.syntax.SyntaxAnalyzer
import org.goo.view.OutputEventLn
import tornadofx.Controller
import java.io.PipedInputStream
import java.io.PipedOutputStream

/**
 * Debug command
 */
class DebugCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    private val scanner = Scanner()
    private val syntaxAnalyzer = SyntaxAnalyzer()
    private val semanticAnalyzer = SemanticAnalyzer()

    override fun execute(split: List<String>) {
        if (memory.debuggerThread != null && memory.debuggerThread!!.isAlive)
            fire(OutputEventLn("[INFO] You can run only one session"))

        memory.debuggerThread = Thread {
            val text = memory.editor.codeArea.text

            val tokens = scanner.scan(text)
            if (isErrors(tokens)) {
                val out = PipedOutputStream()
                memory.out = out
                val inputConsoleWindow: InputConsoleWindow = find(mapOf(InputConsoleWindow::input to PipedInputStream(out)))
                val interpreter = Interpreter(memory.outputStrategy)

                val debugger = Debugger(inputConsoleWindow, interpreter)
                debugger.reset()
                debugger.stopPoints = memory.currentStopPoints
                debugger.isRunning = true
                memory.debugger = debugger
                debugger.debug(tokens)
                debugger.isRunning = false

                fire(OutputEventLn("[INFO] End debug session"))
            }
        }
        memory.debuggerThread?.start()
    }

    /**
     * Check for errors in code
     */
    private fun isErrors(tokens: List<Token>): Boolean {
        syntaxAnalyzer.reset()
        val errors = syntaxAnalyzer.analyze(tokens)
        if (errors.isNotEmpty()) {
            fire(OutputEventLn("[ERROR] find syntax error \n${toLnStr(errors)}"))
            return false
        }
        val semanticError = semanticAnalyzer.analyze(tokens)
        if (!semanticError) {
            fire(OutputEventLn("[ERROR] found semantic error, check for similar sub identifiers"))
            return false
        }
        return true
    }

    private fun toLnStr(errors: List<Token>): String {
        val sb = StringBuilder()
        for (error in errors) {
            sb.appendln(error)
        }
        return sb.toString()
    }
}