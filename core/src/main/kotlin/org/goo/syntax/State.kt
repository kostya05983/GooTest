package org.goo.syntax

import org.goo.scanner.Token

interface State {
    val analyzer: SyntaxAnalyzer

    val errors: MutableList<Token>

    fun check(token: Token)
}