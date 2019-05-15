package org.goo.view

import org.goo.api.OutputStrategy
import tornadofx.Controller

class OutputToConsoleWindow : OutputStrategy, Controller() {
    override fun print(s: String) {
        fire(OutputEventLn(s))
    }
}