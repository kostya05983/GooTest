package org.goo.controllers

import org.goo.api.OutputStrategy
import org.goo.view.OutputEventLn
import tornadofx.Controller

/**
 * Realization of debugger api to output in console
 * @author kostya05983
 */
class OutputToConsoleWindow : OutputStrategy, Controller() {
    override fun print(s: String) {
        fire(OutputEventLn(s))
    }
}