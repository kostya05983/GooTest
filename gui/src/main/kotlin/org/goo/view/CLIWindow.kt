package org.goo.view

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
    val editor: Editor by param()
    private val out = PipedOutputStream()
    private val inputConsoleWindow: InputConsoleWindow = find(mapOf(InputConsoleWindow::input to PipedInputStream(out)))


    private val outputStrategy: OutputToConsoleWindow by inject()
    private val interpreter: Interpreter = Interpreter(outputStrategy)
    private val debugger: Debugger = Debugger(inputConsoleWindow, interpreter)

    override val root: TextArea = textarea {
        loadShortCut()
        maxHeight = 20.0
    }

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
                runAsync {
                    val text = editor.codeArea.text
                    val tokens = scanner.scan(text)
                    val errors = syntaxAnalyzer.analyze(tokens)
                    if (errors.isEmpty()) {
                        val semanticError = semanticAnalyzer.analyze(tokens)
                        if (semanticError) {
                            debugger.isRunning = true
                            debugger.debug(tokens)
                            debugger.isRunning = false
                        }
                    }
                    fire(OutputEventLn("End debug session"))
                }
            }
            ConsoleCommand.ADD.text -> {
                val number = split[1].toIntOrNull() ?: let {
                    fire(OutputEventLn("Add number in add expression"))
                    return
                }
                debugger.stopPoints.add(number)
                fire(OutputEventLn("Successfully add point at $number"))
            }
            ConsoleCommand.REMOVE.text -> {
                val number = split[1].toIntOrNull() ?: let {
                    fire(OutputEventLn("Add number in add expression"))
                    return
                }
                debugger.stopPoints.remove(number)
                fire(OutputEventLn("Successfully remove point at $number"))
            }
            ConsoleCommand.OUTPUT_POINTS.text -> {
                val sb = StringBuilder()
                val points = debugger.stopPoints
                for (point in points) {
                    sb.append("line $point")
                }
                fire(OutputEventLn(sb.toString()))
            }
            ConsoleCommand.VAR.text, ConsoleCommand.STEP_INTO.text,
            ConsoleCommand.TRACE.text, ConsoleCommand.STEP_OVER.text -> {
                out.write("$line\n".toByteArray())
                out.flush()
            }
            else -> {
                fire(OutputEventLn("No such command"))
            }
        }
    }


}