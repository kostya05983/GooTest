package org.goo

class OutputConsole : OutputStrategy {
    override fun print(s: String) {
        println(s)
    }
}