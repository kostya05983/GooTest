package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.view.OutputEventLn
import tornadofx.Controller

/**
 * Output help to user
 * @author kostya05983
 */
class HelpCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
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