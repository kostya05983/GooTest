package org.goo.syntax

import org.goo.scanner.Token
import org.goo.scanner.Tokens

class BinaryRightSubState(override val analyzer: SyntaxAnalyzer, override val errors: MutableList<Token>) : State {
    override fun check(token: Token) {
        when (token.token) {
            Tokens.IDENTIFIER -> {
                analyzer.changeState(WaitFirstOperatorState(analyzer, errors))
            }
            else -> {
                errors.add(token)
            }
        }
    }
}