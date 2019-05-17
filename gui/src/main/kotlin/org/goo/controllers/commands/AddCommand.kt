package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.view.OutputEventLn
import tornadofx.Controller

class AddCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
        if (split.size == 1) {
            fire(OutputEventLn("[ERROR] Add number in add expression"))
            return
        }
        val number = split[1].toIntOrNull() ?: let {
            fire(OutputEventLn("[ERROR] Add number in add expression"))
            return
        }
        memory.currentStopPoints.add(number)
        fire(OutputEventLn("[INFO] Successfully add point at $number"))
    }
}