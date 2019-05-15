package org.goo

import java.io.BufferedReader
import java.io.InputStreamReader

class InputConsole : InputStrategy {
    override fun wait(func: (input: String) -> Unit) {
        val reader = BufferedReader(InputStreamReader(System.`in`))
        val line = reader.readLine()
        func.invoke(line)
    }
}