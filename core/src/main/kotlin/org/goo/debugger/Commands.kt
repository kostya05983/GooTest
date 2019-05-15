package org.goo.debugger

enum class Commands(val text: String) {
    STEP_INTO("stepOver into"),
    STEP_OVER("stepOver over"),
    TRACE("trace"),
    VAR("var")
}