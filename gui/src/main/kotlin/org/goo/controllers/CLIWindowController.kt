package org.goo.controllers

import org.goo.debugger.Debugger
import org.goo.interpreter.Interpreter
import org.goo.scanner.Scanner
import org.goo.semantic.SemanticAnalyzer
import org.goo.syntax.SyntaxAnalyzer
import org.goo.view.*
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
    private var out: PipedOutputStream? = null
    private val currentStopPoints = mutableListOf<Int>()

    private var debugger: Debugger? = null
    private var interpreter: Interpreter? = null
    private val outputStrategy: OutputToConsoleWindow by inject()
    private var debuggerThread: Thread? = null
    private var interpreterThread: Thread? = null

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
            ConsoleCommand.HELP.text -> help()
            else -> fire(OutputEventLn("No such command"))
        }
    }

    /**
     * Debug command
     */
    private fun debug() {
        debuggerThread = Thread {
            val text = editor.codeArea.text
            val tokens = scanner.scan(text)
            syntaxAnalyzer.reset()
            val errors = syntaxAnalyzer.analyze(tokens)
            if (errors.isEmpty()) {
                val semanticError = semanticAnalyzer.analyze(tokens)
                if (semanticError) {
                    val out = PipedOutputStream()
                    this@CLIWindowController.out = out
                    val inputConsoleWindow: InputConsoleWindow = find(mapOf(InputConsoleWindow::input to PipedInputStream(out)))
                    val interpreter = Interpreter(outputStrategy)

                    val debugger = Debugger(inputConsoleWindow, interpreter)
                    debugger.reset()
                    debugger.stopPoints = currentStopPoints
                    debugger.isRunning = true
                    this.debugger = debugger
                    debugger.debug(tokens)
                    debugger.isRunning = false

                    fire(OutputEventLn("End debug session"))
                }
            }
        }
        debuggerThread?.start()
    }

    /**
     * Stop current run or debug session
     */
    private fun stop() {
        try {
            debuggerThread?.interrupt()
            debugger?.isRunning = false
        } catch (e: Exception) {
            println(e)
        }
        try {
            interpreterThread?.interrupt()
            interpreter?.isRunning = false
        } catch (e: Exception) {
            println(e)
        }
        fire(OutputEventLn("End session"))
        fire(RestoreColor())
    }

    /**
     * Add stop point to memory
     */
    private fun add(split: List<String>) {
        if (split.size == 1) {
            fire(OutputEventLn("[ERROR] Add number in add expression"))
            return
        }
        val number = split[1].toIntOrNull() ?: let {
            fire(OutputEventLn("[ERROR] Add number in add expression"))
            return
        }
        currentStopPoints.add(number)
        fire(OutputEventLn("Successfully add point at $number"))
    }

    /**
     * Remove stop point from memory
     */
    private fun remove(split: List<String>) {
        val number = split[1].toIntOrNull() ?: let {
            fire(OutputEventLn("Add number in add expression"))
            return
        }
        currentStopPoints.remove(number)
        fire(OutputEventLn("Successfully remove point at $number"))
    }

    private fun outputPoints() {
        val sb = StringBuilder()
        currentStopPoints.forEach {
            sb.appendln("line $it")
        }
        fire(OutputEventLn(sb.toString()))
    }

    private fun debuggerCommand(line: String) {
        out?.write("$line\n".toByteArray())
        out?.flush()
    }

    /**
     * Run code in editor
     */
    private fun run() {
        interpreterThread = Thread {
            val text = editor.codeArea.text
            val tokens = scanner.scan(text)
            syntaxAnalyzer.reset()
            val errors = syntaxAnalyzer.analyze(tokens)
            if (errors.isEmpty()) {
                val semanticError = semanticAnalyzer.analyze(tokens)
                if (semanticError) {
                    val interpreter = Interpreter(outputStrategy)
                    this@CLIWindowController.interpreter = interpreter
                    interpreter.interpret(tokens)
                    fire(OutputEventLn("[INFO] End with run session"))
                } else {
                    fire(OutputEventLn("[ERROR] semantic exceptions"))
                }
            } else {
                fire(OutputEventLn("[ERROR] syntax exceptions"))
            }
        }
        interpreterThread?.start()
    }

    /**
     * Print help to user
     */
    private fun help() {
        val sb = StringBuilder()
        sb.appendln("[INFO] step - go to next line")
        sb.appendln("[INFO] step_over - go to next line skip current")
        sb.appendln("[INFO] add <number> - add debug line")
        sb.appendln("[INFO] remove <number> - remove debug line")
        sb.appendln("[INFO] debug - start debug session")
        sb.appendln("[INFO] run - run code")
        sb.appendln("[INFO] clear - clear console output")
        fire(OutputEventLn(sb.toString()))
    }
}