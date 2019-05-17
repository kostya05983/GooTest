package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.view.OutputEventLn
import org.goo.view.RestoreColor
import tornadofx.Controller

class StopCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
        try {
            if (memory.debuggerThread != null && memory.debuggerThread!!.isAlive) {
                memory.debuggerThread?.interrupt()
                memory.debugger?.isRunning = false
                fire(OutputEventLn("[INFO] start to stop debugger session"))
            }
        } catch (e: Exception) {
            println(e)
        }
        try {
            if (memory.interpreterThread != null && memory.interpreterThread!!.isAlive) {
                memory.interpreterThread?.interrupt()
                memory.interpreter?.isRunning = false
                fire(OutputEventLn("[INFO] start to stop interpreter session"))
            }
        } catch (e: Exception) {
            println(e)
        }
        fire(RestoreColor())
    }
}