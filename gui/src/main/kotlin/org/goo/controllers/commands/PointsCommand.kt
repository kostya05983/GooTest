package org.goo.controllers.commands

import org.goo.controllers.CurrentSessionMemoryWrapper
import org.goo.view.OutputEventLn
import tornadofx.Controller

/**
 * Output points to user
 */
class PointsCommand(override val memory: CurrentSessionMemoryWrapper) : Command, Controller() {
    override fun execute(split: List<String>) {
        val sb = StringBuilder()
        memory.currentStopPoints.forEach {
            sb.appendln("[INFO] point at line $it")
        }
        fire(OutputEventLn(sb.toString()))
    }
}