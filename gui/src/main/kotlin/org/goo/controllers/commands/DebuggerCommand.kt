package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import tornadofx.Controller

/**
 * Give signal to debugger
 */
class DebuggerCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
        memory.out?.write("${split[0]}\n".toByteArray())
        memory.out?.flush()
    }
}