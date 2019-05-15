package org.goo.syntax

import org.goo.scanner.Token
import org.goo.scanner.Tokens

class ContinuousStartState(override val analyzer: SyntaxAnalyzer, override val errors: MutableList<Token>) : State {
    override fun check(token: Token) {
        when (token.token) {
            Tokens.PRINT -> {
                analyzer.changeState(BinaryRightState(analyzer, errors))
            }
            Tokens.SUB -> {
                analyzer.changeState(BinaryRightState(analyzer, errors))
            }
            Tokens.CALL -> {
                analyzer.changeState(BinaryRightState(analyzer, errors))
            }
            Tokens.SET -> {
                analyzer.changeState(TernarInternalState(analyzer, errors))
            }
            else -> {
                errors.add(token)
            }
        }
    }
}