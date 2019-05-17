package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.view.ClearOutputEvent
import tornadofx.Controller

class ClearCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
        fire(ClearOutputEvent())
    }
}