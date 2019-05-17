package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.interpreter.Interpreter
import org.goo.scanner.Scanner
import org.goo.scanner.Token
import org.goo.semantic.SemanticAnalyzer
import org.goo.syntax.SyntaxAnalyzer
import org.goo.view.OutputEventLn
import tornadofx.Controller

/**
 * Run current code if need
 */
class InterpreterCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    private val scanner = Scanner()
    private val syntaxAnalyzer = SyntaxAnalyzer()
    private val semanticAnalyzer = SemanticAnalyzer()

    override fun execute(split: List<String>) {
        if (memory.interpreterThread != null && memory.interpreterThread!!.isAlive)
            fire(OutputEventLn("[INFO] You can run only one session"))

        memory.interpreterThread = Thread {
            val text = memory.editor.codeArea.text
            val tokens = scanner.scan(text)
            syntaxAnalyzer.reset()
            if (isErrors(tokens)) {
                val interpreter = Interpreter(memory.outputStrategy)
                memory.interpreter = interpreter
                interpreter.interpret(tokens)
                fire(OutputEventLn("[INFO] End with run session"))
            }
        }
        memory.interpreterThread?.start()
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