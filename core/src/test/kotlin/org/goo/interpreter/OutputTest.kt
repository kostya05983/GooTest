package org.goo.interpreter

import org.goo.api.OutputStrategy

class OutputTest : OutputStrategy {
    override fun print(s: String) {
        println(s)
    }
}