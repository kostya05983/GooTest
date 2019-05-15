package org.goo.api

class OutputConsole : OutputStrategy {
    override fun print(s: String) {
        println(s)
    }
}