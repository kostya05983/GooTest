package org.goo.view

import javafx.scene.Parent
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCombination
import org.goo.debugger.Debugger
import org.goo.interpreter.Interpreter
import org.goo.scanner.Scanner
import org.goo.semantic.SemanticAnalyzer
import org.goo.syntax.SyntaxAnalyzer
import tornadofx.View
import tornadofx.textarea
import java.io.PipedInputStream
import java.io.PipedOutputStream

class CLIWindow : View() {

    private val scanner = Scanner()
    private val syntaxAnalyzer = SyntaxAnalyzer()
    private val semanticAnalyzer = SemanticAnalyzer()
    override val root: TextArea = textarea {
        loadShortCut()
        maxHeight = 20.0
    }

    private val out = PipedOutputStream()
    private val inputConsoleWindow: InputConsoleWindow by inject(params = mapOf("test" to PipedInputStream(out)))

    private val interpreter: Interpreter = Interpreter()
    private val debugger: Debugger = Debugger(inputConsoleWindow, interpreter)

    private fun loadShortCut() {
        shortcut(KeyCombination.valueOf("Ctrl+ Enter")) {
            val lastLine = root.text.split("\n").last()
            cli(lastLine)
            root.text = ""
        }
    }

    private fun cli(line: String) {
        val split = line.split(" ")
        when (split[0]) {
            ConsoleCommand.CLEAR.text -> {
                fire(ClearOutputEvent())
            }
            ConsoleCommand.START.text -> {
//                val text = editor.codeArea.text
//                val tokens = scanner.scan(text)
//                val errors = syntaxAnalyzer.analyze(tokens)
//                if (errors.isNotEmpty()) {
//                    val semanticError = semanticAnalyzer.analyze(tokens)
//                    if (semanticError) {
//                        debugger.debug(tokens)
//                    }
//                }
            }
            ConsoleCommand.ADD.text -> {
                val number = split[1].toIntOrNull() ?: let {
                    fire(OutputEvent("Add number in add expression"))
                    return
                }
                debugger.stopPoints.add(number)
                fire(OutputEvent("Successfully add point at ${number}"))
            }
            ConsoleCommand.REMOVE.text -> {
                val number = split[1].toIntOrNull() ?: let {
                    fire(OutputEvent("Add number in add expression"))
                    return
                }
                debugger.stopPoints.remove(number)
            }
            ConsoleCommand.OUTPUT_POINTS.text -> {
                val sb = StringBuilder()
                val points = debugger.stopPoints
                for (point in points) {
                    sb.append("line $point")
                }
                fire(OutputEvent(sb.toString()))
            }
            ConsoleCommand.VAR.text, ConsoleCommand.STEP_INTO.text,
            ConsoleCommand.TRACE.text, ConsoleCommand.STEP_OVER.text -> {
                out.write(line.toByteArray())
                out.flush()
            }
            else -> {
                fire(OutputEventLn("No such command"))
            }
        }
    }


}