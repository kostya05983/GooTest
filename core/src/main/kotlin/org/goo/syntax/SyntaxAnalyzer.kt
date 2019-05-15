package org.goo.syntax

import org.goo.scanner.Token
import java.util.*

class SyntaxAnalyzer {

    private var currentState: State = InitState(this, LinkedList())

    fun analyze(tokens: List<Token>): List<Token> {
        for (token in tokens) {
            currentState.check(token)
        }
        return currentState.errors
    }

    fun changeState(newState: State) {
        currentState = newState
    }
}