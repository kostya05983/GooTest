package org.goo.syntax

import org.goo.scanner.Token
import org.goo.scanner.Tokens

class EndLineState(override val analyzer: SyntaxAnalyzer, override val errors: MutableList<Token>) : State {
    override fun check(token: Token) {
        when (token.token) {
            Tokens.NEWLINE -> {
                analyzer.changeState(ContinuousStartState(analyzer, errors))
            }
            else -> {
                errors.add(token)
            }
        }
    }
}