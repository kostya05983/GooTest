package org.goo.controllers

import org.goo.debugger.Debugger
import org.goo.interpreter.Interpreter
import org.goo.scanner.Scanner
import org.goo.semantic.SemanticAnalyzer
import org.goo.syntax.SyntaxAnalyzer
import org.goo.view.ClearOutputEvent
import org.goo.view.ConsoleCommand
import org.goo.view.Editor
import org.goo.view.OutputEventLn
import tornadofx.Controller
import java.io.PipedInputStream
import java.io.PipedOutputStream

/**
 * ModelView control cli command processing of gui
 * @author kostya05983
 */
class CLIWindowController : Controller() {
    private val scanner = Scanner()
    private val syntaxAnalyzer = SyntaxAnalyzer()
    private val semanticAnalyzer = SemanticAnalyzer()
    val editor: Editor by param()
    private val out = PipedOutputStream()
    private val inputConsoleWindow: InputConsoleWindow = find(mapOf(InputConsoleWindow::input to PipedInputStream(out)))

    private val outputStrategy: OutputToConsoleWindow by inject()
    private val interpreter: Interpreter = Interpreter(outputStrategy)
    private val debugger: Debugger = Debugger(inputConsoleWindow, interpreter)

    fun cli(line: String) {
        val split = line.split(" ")
        when (split[0]) {
            ConsoleCommand.CLEAR.text -> fire(ClearOutputEvent())
            ConsoleCommand.DEBUG.text -> debug()
            ConsoleCommand.STOP.text -> stop()
            ConsoleCommand.ADD.text -> add(split)
            ConsoleCommand.REMOVE.text -> remove(split)
            ConsoleCommand.OUTPUT_POINTS.text -> outputPoints()
            ConsoleCommand.VAR.text, ConsoleCommand.STEP_INTO.text,
            ConsoleCommand.TRACE.text, ConsoleCommand.STEP_OVER.text -> debuggerCommand(line)
            ConsoleCommand.RUN.text -> run()
            else -> fire(OutputEventLn("No such command"))
        }
    }

    private fun debug() {
        Thread {
            val text = editor.codeArea.text
            val tokens = scanner.scan(text)
            syntaxAnalyzer.reset()
            val errors = syntaxAnalyzer.analyze(tokens)
            if (errors.isEmpty()) {
                val semanticError = semanticAnalyzer.analyze(tokens)
                if (semanticError) {
                    debugger.reset()
                    debugger.isRunning = true
                    debugger.debug(tokens)
                    debugger.isRunning = false
                    fire(OutputEventLn("End debug session"))
                }
            }

        }.start()
    }

    private fun stop() {
        debugger.isRunning = false
        interpreter.isRunning = false
//        out.write("$line\n".toByteArray())
//        out.flush()
        fire(OutputEventLn("End session"))
    }

    private fun add(split: List<String>) {
        val number = split[1].toIntOrNull() ?: let {
            fire(OutputEventLn("Add number in add expression"))
            return
        }
        debugger.stopPoints.add(number)
        fire(OutputEventLn("Successfully add point at $number"))
    }

    private fun remove(split: List<String>) {
        val number = split[1].toIntOrNull() ?: let {
            fire(OutputEventLn("Add number in add expression"))
            return
        }
        debugger.stopPoints.remove(number)
        fire(OutputEventLn("Successfully remove point at $number"))
    }

    private fun outputPoints() {
        val sb = StringBuilder()
        val points = debugger.stopPoints
        for (point in points) {
            sb.append("line $point")
        }
        fire(OutputEventLn(sb.toString()))
    }

    private fun debuggerCommand(line: String) {
        out.write("$line\n".toByteArray())
        out.flush()
    }

    private fun run() {
        Thread {
            val text = editor.codeArea.text
            val tokens = scanner.scan(text)
            syntaxAnalyzer.reset()
            val errors = syntaxAnalyzer.analyze(tokens)
            if (errors.isEmpty()) {
                val semanticError = semanticAnalyzer.analyze(tokens)
                if (semanticError) {
                    interpreter.interpret(tokens)
                    fire(OutputEventLn("[INFO] End with run session"))
                } else {
                    fire(OutputEventLn("[ERROR] semantic exceptions"))
                }
            } else {
                fire(OutputEventLn("[ERROR] syntax exceptions"))
            }
        }.start()
    }
}