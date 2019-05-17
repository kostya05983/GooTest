package org.goo.controllers

import org.goo.ConsoleCommand
import org.goo.controllers.commands.*
import org.goo.view.Editor
import org.goo.view.OutputEventLn
import tornadofx.Controller
import tornadofx.FXEvent

/**
 * ModelView control cli command processing of gui
 * @author kostya05983
 */
class CLIWindowController : Controller() {
    val editor: Editor by param()

    private val outputStrategy: OutputToConsoleWindow by inject()
    private val currentSessionMemoryWrapper = CurrentSessionMemoryWrapper(editor, outputStrategy)

    private val debuggerCommand = DebuggerCommand(currentSessionMemoryWrapper)
    private val commands = mapOf<String, Command>(
            ConsoleCommand.DEBUG.text to DebugCommand(currentSessionMemoryWrapper),
            ConsoleCommand.STOP.text to StopCommand(currentSessionMemoryWrapper),
            ConsoleCommand.ADD.text to AddCommand(currentSessionMemoryWrapper),
            ConsoleCommand.REMOVE.text to RemoveCommand(currentSessionMemoryWrapper),
            ConsoleCommand.OUTPUT_POINTS.text to PointsCommand(currentSessionMemoryWrapper),
            ConsoleCommand.VAR.text to debuggerCommand,
            ConsoleCommand.TRACE.text to debuggerCommand,
            ConsoleCommand.STEP_INTO.text to debuggerCommand,
            ConsoleCommand.STEP_OVER.text to debuggerCommand,
            ConsoleCommand.RUN.text to InterpreterCommand(currentSessionMemoryWrapper),
            ConsoleCommand.HELP.text to HelpCommand(currentSessionMemoryWrapper),
            ConsoleCommand.CLEAR.text to ClearCommand(currentSessionMemoryWrapper)
    )

    init {
        loadSubscribers()
    }

    fun cli(line: String) {
        val split = line.split(" ")
        val command = commands[split[0]]
        if (command != null) {
            command.execute(split)
        } else {
            fire(OutputEventLn("[ERROR] No such command"))
        }
    }

    private fun loadSubscribers() {
        subscribe<CommandEvent> {
            cli(it.line)
        }
    }
}

class CommandEvent(val line: String) : FXEvent()