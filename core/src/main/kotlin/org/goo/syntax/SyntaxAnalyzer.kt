package org.goo.syntax

import org.goo.scanner.Token
import java.util.*

class SyntaxAnalyzer {

    private var currentState: State = InitState(this, LinkedList())

    fun analyze(tokens: List<Token>): List<Token> {
        for (token in tokens) {
            currentState.check(token)
        }
        if (currentState is WaitFirstOperatorState || currentState is WaitFirstOperatorState) {
            currentState.errors.add(tokens.last())
        }
        return currentState.errors
    }

    fun reset() {
        currentState = InitState(this, LinkedList())
    }

    fun changeState(newState: State) {
        currentState = newState
    }
}