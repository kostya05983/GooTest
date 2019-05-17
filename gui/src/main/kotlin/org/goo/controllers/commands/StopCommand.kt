package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.view.OutputEventLn
import org.goo.view.RestoreColor
import tornadofx.Controller

class StopCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
        try {
            memory.debuggerThread?.interrupt()
            memory.debugger?.isRunning = false
        } catch (e: Exception) {
            println(e)
        }
        try {
            memory.interpreterThread?.interrupt()
            memory.interpreter?.isRunning = false
        } catch (e: Exception) {
            println(e)
        }
        fire(OutputEventLn("[INFO] start to stop session"))
        fire(RestoreColor())
    }
}